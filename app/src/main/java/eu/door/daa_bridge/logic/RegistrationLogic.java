package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.security.PrivateKey;
import java.security.PublicKey;

import eu.door.daa_bridge.http.APIClient;
import eu.door.daa_bridge.http.APIInterface;
import eu.door.daa_bridge.http.pojo.EnabledFullCredentialReq;
import eu.door.daa_bridge.http.pojo.GetFullCredentialReq;
import eu.door.daa_bridge.http.pojo.GetFullCredentialRes;
import eu.door.daa_bridge.http.pojo.GetIssuerChallengeReq;
import eu.door.daa_bridge.http.pojo.GetIssuerChallengeRes;
import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.DaaInfo;
import eu.door.daa_bridge.payload.DaaRegister;
import eu.door.daa_bridge.payload.EnableRequest;
import eu.door.daa_bridge.payload.EnableResponse;
import eu.door.daa_bridge.payload.RegisterResponse;
import eu.door.daa_bridge.payload.RegnObject;
import eu.door.daa_bridge.util.KeystoreInfo;
import eu.door.daa_bridge.util.SecurityUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationLogic {

    private static final String KEYSTORE_ALIAS = "daabridge";
    private static final String KEYSTORE_PASSWORD = "p@ssw0rd!";
    private static final String KEYSTORE_FILENAME = "daabridge.keystore";

    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    private final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    private PrivateKey getPrivateKey(Context context){
        KeystoreInfo keystoreInfo = new KeystoreInfo(
                KEYSTORE_FILENAME,
                KEYSTORE_ALIAS,
                KEYSTORE_PASSWORD
        );

        return SecurityUtil.getPrivateKey(context,keystoreInfo);
    }


    public Boolean saveCertificate(String algorithm, String publicKey) {
        data.getDaaInterface().registerWalletPK(publicKey.getBytes());

        PublicKey pk = SecurityUtil.readPublicKey(publicKey);
        if(pk == null){
            return false;
        }
        data.setWalletPublicKeyPem(publicKey);
        data.setWalletPublicKey(SecurityUtil.readPublicKey(publicKey));
        data.setKeyAlgorithm(algorithm);
        return true;
    }

    public byte[] signNonce(Context context, byte[] nonce) {
        PrivateKey pk = getPrivateKey(context);

        byte[] signature = new byte[0];
        try {
            signature = SecurityUtil.sign("EC", pk, nonce);
        } catch (Exception e) {
            Log.e("signature", "Failed to sign message");
            e.printStackTrace();
        }
        return signature;
    }

    public Boolean verify(EnableRequest req) {

        PublicKey publicKey = data.getWalletPublicKey();
        Boolean verified = false;
        try {
            verified = SecurityUtil.verifySignature(
                    data.getKeyAlgorithm(),
                    publicKey,
                    req.getTime(),
                    req.getSigned()
            );
        } catch (Exception e) {
            Log.d("verifySignature", "Failed to verify signature: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        return verified;
    }

    public RegnObject enable() {
        String daaInfo = data.getDaaInterface()
                .DAAEnable();
        Log.d("daaInfo", daaInfo);
        Gson gson = new Gson();
        DaaInfo daaInfoObj = gson.fromJson(daaInfo,DaaInfo.class);
        return new RegnObject(daaInfoObj);
    }

    public RegisterResponse createRegisterResponse(byte[] signature) {
        RegisterResponse res = new RegisterResponse();
        res.setSigned(signature);
        return res;
    }

    public EnableResponse createEnableResponse(RegnObject regnObject) {
        EnableResponse res = new EnableResponse();
        regnObject.setToken(data.getToken());
        res.setRegnObject(regnObject);
        res.setTpmNonce(regnObject.getTpmNonce());
        return res;
    }


    public void daaRegister(DaaRegister daaRegister) {
        //Call CreateEnableResponse: Creates the endorsementkey and returns registration object
        //{AK, EK}
        String issreg = createEnableResponseFromTpm(daaRegister);

        // Send it to the DAA issuer
        getIssuerChallenge(issreg, data.getWalletPublicKeyPem());
    }

    private void enableDAACredential(String fcre) {
        data.getDaaInterface()
                .EnableDAACredential(fcre);
    }

    private String createEnableResponseFromTpm(DaaRegister daaRegister) {
        String issreg = data.getDaaInterface()
                .CreateEnableResponse(daaRegister.getSignedTpmNonce());
        return issreg;
    }


    private String handleIssuerChallenge(String challenge) {
        return data.getDaaInterface().HandleIssuerChallenge(challenge);
    }

    private void getIssuerChallenge(String issreg, String walletPublicKey) {
        Call<GetIssuerChallengeRes> call = apiInterface.getIssuerChallenge(
                new GetIssuerChallengeReq(issreg, walletPublicKey)
        );


        call.enqueue(new Callback<GetIssuerChallengeRes>() {
            @Override
            public void onResponse(Call<GetIssuerChallengeRes> call, Response<GetIssuerChallengeRes> response) {
                Log.d("getIssuerChallenge","CODE: " + response.code()+"");
                Log.d("getIssuerChallenge","BODY: " + response.body()+"");

                String challenge = response.body().getChallenge();

                // Call back into the core and get a response to the challenge
                String challengeResponse = handleIssuerChallenge(challenge);

                // Send challenge response back to the issuer and obtain full credential
                getFullCredential(challengeResponse);
            }

            @Override
            public void onFailure(Call<GetIssuerChallengeRes> call, Throwable t) {
                Log.d("getIssuerChallenge","Failure");
                call.cancel();
            }
        });
    }

    private void getFullCredential(String challengeResponse) {
        Call<GetFullCredentialRes> call = apiInterface.getFullCredential(
                new GetFullCredentialReq(challengeResponse)
        );

        call.enqueue(new Callback<GetFullCredentialRes>() {
            @Override
            public void onResponse(Call<GetFullCredentialRes> call, Response<GetFullCredentialRes> response) {
                Log.d("getFullCredential","CODE: " + response.code()+"");
                Log.d("getFullCredential","BODY: " + response.body()+"");

                String fcre = response.body().getFcre();

                // "Enable" the credential
                enableDAACredential(fcre);

                // Inform DAA issuer that the credential is enabled
                enabledFullCredential();
            }

            @Override
            public void onFailure(Call<GetFullCredentialRes> call, Throwable t) {
                Log.d("getFullCredential","Failure");
                call.cancel();
            }
        });
    }


    private void enabledFullCredential() {
        Call<String> call = apiInterface.enabledFullCredential(
                new EnabledFullCredentialReq("OK")
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("enabledFullCredential","CODE: " + response.code()+"");
                Log.d("enabledFullCredential","BODY: " + response.body()+"");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
            }
        });
    }

}

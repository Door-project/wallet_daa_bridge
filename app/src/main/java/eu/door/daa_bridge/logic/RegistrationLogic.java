package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import eu.door.daa_bridge.http.APIClient;
import eu.door.daa_bridge.http.APIInterface;
import eu.door.daa_bridge.http.pojo.DAAUserHandle;
import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.DaaInfo;
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
        data.setWalletPublicKey(SecurityUtil.readPublicKey(publicKey));
        data.setKeyAlgorithm(algorithm);
        return true;
    }

    public byte[] signNonce(Context context, byte[] nonce) {
        PrivateKey pk = getPrivateKey(context);

        byte[] signature = new byte[0];
        try {
            signature = SecurityUtil.sign(data.getKeyAlgorithm(), pk, nonce);
            Log.d("signature", Arrays.toString(signature));
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

    public void daaUserHandle() {
        DAAUserHandle userHandle = new DAAUserHandle("1", "user handle");

        Call<String> call = apiInterface.daaUserHandle(userHandle);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("daaUserHandle","CODE: " + response.code()+"");
                Log.d("daaUserHandle","BODY: " + response.body()+"");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("daaUserHandle","Failure");
                call.cancel();
            }
        });
    }
}

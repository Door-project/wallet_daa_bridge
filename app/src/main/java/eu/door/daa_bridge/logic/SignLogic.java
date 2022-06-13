package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.DaaInfo;
import eu.door.daa_bridge.payload.Evidence;
import eu.door.daa_bridge.payload.RegnObject;
import eu.door.daa_bridge.payload.SignErrorResponse;
import eu.door.daa_bridge.payload.SignRequest;
import eu.door.daa_bridge.payload.SignResponse;
import eu.door.daa_bridge.util.KeystoreInfo;
import eu.door.daa_bridge.util.SecurityUtil;

public class SignLogic {
    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    private static final String KEYSTORE_ALIAS = "daabridge";
    private static final String KEYSTORE_PASSWORD = "p@ssw0rd!";
    private static final String KEYSTORE_FILENAME = "daabridge.keystore";

    private PrivateKey getPrivateKey(Context context){
        KeystoreInfo keystoreInfo = new KeystoreInfo(
                KEYSTORE_FILENAME,
                KEYSTORE_ALIAS,
                KEYSTORE_PASSWORD
        );

        return SecurityUtil.getPrivateKey(context,keystoreInfo);
    }

    //integration with TPM library
    public Boolean verify(SignRequest req) {
        PublicKey publicKey = data.getWalletPublicKey();
        Boolean verified = false;
        try {
            verified = SecurityUtil.verifySignature(
                    data.getKeyAlgorithm(),
                    publicKey,
                    req.getNonce(),
                    req.getSigned()
            );
        } catch (Exception e) {
            Log.d("verifySignature", "Failed to verify signature: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        return verified;
    }

    public SignResponse createSignResponse(String daaSignature, byte[] nonce) {
        SignResponse res = new SignResponse();
        res.setDaaSignature(daaSignature);
        res.setNonce(nonce);
        return res;
    }

    //integration with TPM library
    public byte[] getTpmNonce() {
        return new byte[0];
    }

    public Boolean verifyEvidenceObjects(List<Evidence> evidenceObjects, List<Evidence> unverified) {

        int unverifiedObjects = 0;

        for(Evidence evidence : evidenceObjects){
            //verify evidence

            Log.d("verifyEvidenceObjects", "Credential ID: " + evidence.getCredentialId());
            if ( data.getDaaInterface()
                    .verifySignature(
                            evidence.getDaaSignature(),
                            evidence.getNonce()
                    ) != 1) {
                Log.d("verifyEvidenceObjects", "Verification failed");
                unverified.add(evidence);
                unverifiedObjects++;
            }
            else {
                Log.d("verifyEvidenceObjects", "Successfully verified");
            }
        }

        return unverifiedObjects == 0;
    }

    public String sign(byte[] rpNonce, byte[] signed) {
        String resp = data.getDaaInterface().DAASign(rpNonce,signed);
        return resp;
    }

    public RegnObject enable() {
        String daaInfo = data.getDaaInterface()
                .DAAEnable();
        Log.d("daaInfo", daaInfo);
        Gson gson = new Gson();
        DaaInfo daaInfoObj = gson.fromJson(daaInfo,DaaInfo.class);
        return new RegnObject(daaInfoObj);
    }

    public SignErrorResponse createSignVpErrorResponse(RegnObject regnObject, List<Evidence> unverified) {
        SignErrorResponse res = new SignErrorResponse();
        regnObject.setToken(data.getToken());
        res.setRegnObject(regnObject);
        res.setEvidenceObjects(unverified);
        return res;
    }
}

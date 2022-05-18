package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
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

    public SignResponse createSignResponse(byte[] signedRpNonce) {
        SignResponse res = new SignResponse();
        res.setSignedRpNonce(signedRpNonce);
        return res;
    }

    //integration with TPM library
    public byte[] getTpmNonce() {
        return new byte[0];
    }

    //integration with TPM library
    public Boolean verifyEvidenceObjects(List<Evidence> evidenceObjects, List<Evidence> unverified) {
        for(Evidence evidence : evidenceObjects){
            //verify evidence
        }
        return true;
    }

    //integration with TPM library
    public byte[] sign(byte[] rpNonce) {
        return new byte[0];
    }

    //integration with TPM library
    public RegnObject enable() {
        return new RegnObject();
    }

    public SignErrorResponse createSignVpErrorResponse(RegnObject regnObject, List<Evidence> unverified) {
        SignErrorResponse res = new SignErrorResponse();
        res.setRegnObject(regnObject);
        res.setEvidenceObjects(unverified);
        return res;
    }
}

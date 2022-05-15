package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.EnableRequest;
import eu.door.daa_bridge.payload.EnableResponse;
import eu.door.daa_bridge.payload.RegisterResponse;
import eu.door.daa_bridge.payload.RegnObject;
import eu.door.daa_bridge.util.KeystoreInfo;
import eu.door.daa_bridge.util.SecurityUtil;

public class RegistrationLogic {

    private static final String KEYSTORE_ALIAS = "daabridge";
    private static final String KEYSTORE_PASSWORD = "p@ssw0rd!";
    private static final String KEYSTORE_FILENAME = "daabridge.keystore";

    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    private PrivateKey getPrivateKey(Context context){
        KeystoreInfo keystoreInfo = new KeystoreInfo(
                KEYSTORE_FILENAME,
                KEYSTORE_ALIAS,
                KEYSTORE_PASSWORD
        );

        return SecurityUtil.getPrivateKey(context,keystoreInfo);
    }


    public Boolean saveCertificate(String algorithm, String publicKey) {
        //integration with TPM Library
        //set wallet public key to TPM
        //Boolean isOk = tmp.setWalletPublicKey(algorithm, publicKey);

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

    //integration with TPM library
    public RegnObject enable() {
        return new RegnObject();
    }

    public RegisterResponse createRegisterResponse(byte[] signature) {
        RegisterResponse res = new RegisterResponse();
        res.setSigned(signature);
        return res;
    }

    //integration with TPM library
    public EnableResponse createEnableResponse(RegnObject regnObject) {
        EnableResponse res = new EnableResponse();
        res.setP_EK(regnObject.getP_EK());
        res.setTpmNonce(regnObject.getTpmNonce());
        return res;
    }
}

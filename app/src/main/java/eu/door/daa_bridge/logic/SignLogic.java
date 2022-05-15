package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.RegisterResponse;
import eu.door.daa_bridge.payload.SignVpReqResponse;
import eu.door.daa_bridge.payload.SignVpRequest;
import eu.door.daa_bridge.payload.SignVpResponse;
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

    public Boolean verify(SignVpRequest req) {
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

    public SignVpResponse createSignVpResponse() {
        return new SignVpResponse();
    }

    //integration with TPM library
    public SignVpReqResponse createSignVpReqResponse() {
        return new SignVpReqResponse();
    }

}

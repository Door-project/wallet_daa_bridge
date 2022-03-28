package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import eu.door.daa_bridge.util.KeystoreInfo;
import eu.door.daa_bridge.util.SecurityUtil;

public class RegistrationLogic {
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

    public Boolean verifyChallengeSolution(Context context, byte[] encrypted, String solution) {
        PrivateKey pk = getPrivateKey(context);

        String decrypted;
        try {
            decrypted = SecurityUtil.decrypt(pk, encrypted);
            Log.d("decrypted", decrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            Log.d("decrypt", "Failed to decrypt message: "+ e.getMessage());
            e.printStackTrace();
            return false;
        }

        if(solution.equals(decrypted)) {
            return true;
        }
        Log.d("solution", "wrong, should be equals to " + solution);
        return false;
    }

    public byte[] sign(Context context, String message) {
        PrivateKey pk = getPrivateKey(context);

        byte[] signature = new byte[0];
        try {
            signature = SecurityUtil.sign(pk, message);
            Log.d("signature", Arrays.toString(signature));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            Log.e("signature", "Failed to sign message");
            e.printStackTrace();
        }
        return signature;
    }

}

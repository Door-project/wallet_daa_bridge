package eu.door.daa_bridge.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SecurityUtil {

    public static String decrypt(PrivateKey privateKey, byte[] encrypted)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(encrypted));
    }

    public static byte[] sign(PrivateKey privateKey, String message)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();
        return signature;
    }

    public static PrivateKey getPrivateKey(Context context, KeystoreInfo keystoreInfo) {
        PrivateKey privateKey = null;
        try {
            InputStream stream = context.getAssets().open(keystoreInfo.getFilename());
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load( stream, keystoreInfo.getPassword().toCharArray() );
            privateKey = (PrivateKey) ks.getKey(keystoreInfo.getAlias(), keystoreInfo.getPassword().toCharArray());
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            Log.e("getPrivateKey", e.getMessage());
            e.printStackTrace();
        }
        return privateKey;
    }
}

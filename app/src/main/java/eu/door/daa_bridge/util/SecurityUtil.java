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
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;


public class SecurityUtil {

    public static final String KEY_ALG_RSA = "RSA";
    public static final String KEY_ALG_DSA = "DSA";
    public static final String KEY_ALG_EC = "EC";


    private static String getCompatibleSigAlgName(String keyAlgName) throws Exception {
        if (KEY_ALG_DSA.equalsIgnoreCase(keyAlgName)) {
            return "SHA1WithDSA";
        } else if (KEY_ALG_RSA.equalsIgnoreCase(keyAlgName)) {
            return "SHA256WithRSA";
        } else if (KEY_ALG_EC.equalsIgnoreCase(keyAlgName)) {
            return "SHA256withECDSA";
        } else {
            throw new Exception("Cannot derive signature algorithm");
        }
    }

    public static byte[] sign(String keyAlg, PrivateKey privateKey, String message) throws Exception {
        Signature privateSignature = Signature.getInstance(getCompatibleSigAlgName(keyAlg));
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();
        return signature;
    }

    public static boolean verifySignature(String keyAlg, PublicKey publicKey, byte[] payload, byte[] signature)
            throws Exception {
        Signature sign = Signature.getInstance( getCompatibleSigAlgName(keyAlg) );
        sign.initVerify(publicKey);
        sign.update(payload);

        boolean result = sign.verify(signature);

        Log.d("MSSG verifySignature->", String.valueOf(result));

        return result;
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

    public static PublicKey getPublicKey(Context context, String filename) {
        PublicKey publicKey = null;
        try {
            InputStream stream = context.getAssets().open(filename);
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            X509Certificate cer = (X509Certificate) fact.generateCertificate(stream);
            publicKey = cer.getPublicKey();
        } catch (IOException | CertificateException e) {
            Log.e("getPublicKey", e.getMessage());
            e.printStackTrace();
        }
        return publicKey;
    }


}

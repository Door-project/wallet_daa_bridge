package eu.door.daa_bridge.util;


import android.content.Context;
import android.util.Base64;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;


public class SecurityUtil {

    public static final String KEY_ALG_RSA = "RSA";
    public static final String KEY_ALG_DSA = "DSA";
    public static final String KEY_ALG_EC = "EC";

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

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

    public static byte[] sign(String keyAlg, PrivateKey privateKey, byte[] message) throws Exception {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

        Signature privateSignature = Signature.getInstance(getCompatibleSigAlgName(keyAlg),
                new BouncyCastleProvider().getName());
        privateSignature.initSign(privateKey);
        privateSignature.update(message);
        byte[] exp = {0x00, 0x00, 0x00, 0x00};
        privateSignature.update(exp);
        byte[] signature = privateSignature.sign();

        return signature;
    }

    public static boolean verifySignature(String keyAlg, PublicKey publicKey, byte[] payload, byte[] signature)
            throws Exception {

        Signature sign = Signature.getInstance( getCompatibleSigAlgName(keyAlg) );
        sign.initVerify(publicKey);
        sign.update(payload);
        byte[] exp = {0x00, 0x00, 0x00, 0x00};
        sign.update(exp);

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


    public static PublicKey readPublicKey(String sPemPublicKey) {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

        Reader reader = new StringReader(sPemPublicKey);
        PEMParser pemParser = new PEMParser(reader);
        try {
            SubjectPublicKeyInfo parsedPk = (SubjectPublicKeyInfo) pemParser.readObject();
            PublicKey publicKey = new JcaPEMKeyConverter().getPublicKey(parsedPk);
            return publicKey;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PrivateKey readPrivateKey(String sPemPrivateKey) throws Exception {
        Log.d("sPemPrivateKey", sPemPrivateKey);

        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

        try (Reader reader = new StringReader(sPemPrivateKey);
             PemReader pemReader = new PemReader(reader)) {
            Object parsed = new org.bouncycastle.openssl.PEMParser(pemReader).readObject();

            KeyPair pair = new org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter().getKeyPair((org.bouncycastle.openssl.PEMKeyPair)parsed);
            return pair.getPrivate();
        }
    }


    public static String createPrivatePKCS1(PrivateKey pk) throws Exception {
        StringWriter stringWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(stringWriter);
        pemWriter.writeObject( pk );
        pemWriter.close();
        return stringWriter.toString();
    }

    public static String createPublicPKCS1(PublicKey pk) throws Exception {
        StringWriter stringWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(stringWriter);
        pemWriter.writeObject( pk );
        pemWriter.close();
        return stringWriter.toString();
    }

    public static String createPrivatePKCS8(PrivateKey pk) throws Exception {
        String sb = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.encodeToString(pk.getEncoded(), 0) +
                "-----END PRIVATE KEY-----\n";
        return sb;
    }

    public static String createPublicPKCS8(PublicKey pk) throws Exception {
        String sb = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.encodeToString(pk.getEncoded(), 0) +
                "-----END PUBLIC KEY-----\n";
        return sb;
    }


    public static KeyPair createKeyPair() throws Exception {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPair = keyGen.generateKeyPair();
        return keyPair;
    }



}

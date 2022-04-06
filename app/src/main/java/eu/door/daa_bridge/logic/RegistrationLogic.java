package eu.door.daa_bridge.logic;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Random;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.RegisterRequest;
import eu.door.daa_bridge.payload.RegisterResponse;
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

    public Boolean verify(Context context, byte[] signature, String message) {
        PublicKey publicKey = data.getWalletCertificate().getPublicKey();
        Boolean verified = false;
        try {
            verified = SecurityUtil.verifySignature(
                    data.getKeyAlgorithm(),
                    publicKey,
                    message.getBytes(StandardCharsets.UTF_8),
                    signature
            );
        } catch (Exception e) {
            Log.d("verifySignature", "Failed to verify signature: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        return verified;
    }


    public byte[] sign(Context context, String message) {
        PrivateKey pk = getPrivateKey(context);

        byte[] signature = new byte[0];
        try {
            signature = SecurityUtil.sign(data.getKeyAlgorithm(), pk, message);
            Log.d("signature", Arrays.toString(signature));
        } catch (Exception e) {
            Log.e("signature", "Failed to sign message");
            e.printStackTrace();
        }
        return signature;
    }

    public RegisterResponse createRegisterResponse(Context context, RegisterRequest req) {
        PrivateKey pk = getPrivateKey(context);

        RegisterResponse res = new RegisterResponse();
        res.setNonse1(req.getNonce1());
        Random r = new Random();
        int n = r.nextInt();
        String nonse2 = Integer.toString(n);
        res.setNonse2(nonse2);
        try {
            res.setSigned1( SecurityUtil.sign(data.getKeyAlgorithm(), pk, req.getNonce1()) );
            res.setSigned2( SecurityUtil.sign(data.getKeyAlgorithm(), pk, nonse2));
        } catch (Exception e) {
            Log.e("signature", "Failed to sign message");
            e.printStackTrace();
        }
        return res;
    }

    public void saveCertificate(String algorithm, byte[] certificate) {
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            X509Certificate walletCertificate = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(certificate));
            data.setWalletCertificate(walletCertificate);
            data.setKeyAlgorithm(algorithm);
        } catch (CertificateException e) {
            Log.e("saveCertificate", e.getMessage());
            e.printStackTrace();
        }
    }
}

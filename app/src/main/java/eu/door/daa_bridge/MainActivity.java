package eu.door.daa_bridge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.service.NotificationService;

import com.daaBridge.androidtpm.AndroidTPM;
import com.daaBridge.daabridgecpp.DAAInterface;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------------------------//

        // Retrieve and set the current registration token
        NotificationService.getToken();


        DAAInterface daaInterface = new DAAInterface();
        daaInterface.initiateTPM();
        WalletDaaBridgeData.getInstance()
                .setDaaInterface(daaInterface);

        System.out.println("Trying to write Public Key IS/WK");

        String ik = "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25Q\n" +
                "hTF3PuomKkE3/ET4GcPMTkYi8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
                "-----END PUBLIC KEY-----";

        String ik_priv = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIF8Cx/viWSyi0gCp/OcbMFJrbKmzO2PwlqA/RNtv9UMZoAoGCCqGSM49\n" +
                "AwEHoUQDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25QhTF3PuomKkE3/ET4GcPMTkYi\n" +
                "8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
                "-----END EC PRIVATE KEY-----\n";


        byte[] issuerPk = ik.getBytes();
        daaInterface.registerIssuerPK(issuerPk);

        byte[] issuerPriv = ik_priv.getBytes();
        daaInterface.registerIssuerPK(issuerPk);
        //---------------------------------------------------------------//


        //----------- TESTING --------------------------------------------//
//        DAAInterface daaInterface = new DAAInterface();
//        daaInterface.initiateTPM();
//
//
//        System.out.println("Trying to write Public Key IS/WK");
//        String wk_priv = "-----BEGIN RSA PRIVATE KEY-----\n"
//               + "MIIBOAIBAAJAabIOiUfWBi0Q5cc5gdqKhmDcFTIv8V08FT8rqz9kMAyTpqgDnn9T\n"
//               + "9wutDBvQSla1WNs1w3oRmFiu1CBEn/62FwIDAQABAkBfFc459iQ2nihLbrTvevmP\n"
//               + "9mdHskSrMKMywge5IWgySJWuYqwTMFrHjVgBTL8/6qsBrQLrUxYWqvuo8nFkktAB\n"
//               + "AiEAruYduR4hc5yMMC7eUwNGfUBv0x5/nr7Wcbm9Hj9vRAECIQCatPQ6GQGQY+k8\n"
//               + "jii5InFSPerkYB8mf+sdn5Nc4heaFwIgSTEfyzJiyWEAAcH7ZD4Ap7Xpli4zNhmi\n"
//               + "4GDcVeYyCAECIAIVAVxuzbiVv6PS/fVP41qJ/slICeNIwW9KoH8Vg9/NAiBuzzLf\n"
//               + "0XdUhUNkFV6n8RDcBTuLvC98XdynijrrTIokSA==\n"
//               + "-----END RSA PRIVATE KEY-----\n";
//
//        String wk = "-----BEGIN PUBLIC KEY-----\n"
//                + "MFswDQYJKoZIhvcNAQEBBQADSgAwRwJAabIOiUfWBi0Q5cc5gdqKhmDcFTIv8V08\n"
//                + "FT8rqz9kMAyTpqgDnn9T9wutDBvQSla1WNs1w3oRmFiu1CBEn/62FwIDAQAB\n"
//                + "-----END PUBLIC KEY-----\n";
//
//        String ik = "-----BEGIN PUBLIC KEY-----\n" +
//                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25Q\n" +
//                "hTF3PuomKkE3/ET4GcPMTkYi8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
//                "-----END PUBLIC KEY-----";
//
//        String ik_priv = "-----BEGIN EC PRIVATE KEY-----\n" +
//                "MHcCAQEEIF8Cx/viWSyi0gCp/OcbMFJrbKmzO2PwlqA/RNtv9UMZoAoGCCqGSM49\n" +
//                "AwEHoUQDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25QhTF3PuomKkE3/ET4GcPMTkYi\n" +
//                "8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
//                "-----END EC PRIVATE KEY-----\n";
//
//        byte[] walletKeyPriv = wk_priv.getBytes();
//        byte[] issuerPk = ik.getBytes();
//        byte[] walletPk = wk.getBytes();
//        byte[] issuerPriv = ik_priv.getBytes();
//
//        daaInterface.registerWallet_priv(walletKeyPriv);
//        daaInterface.registerWalletPK(walletPk);
//        daaInterface.registerIssuerPK(issuerPk);
//        daaInterface.registerIssuer_priv(issuerPriv);
//
//        // Boot TPM and get Endrosement Key
//        // Returns {EK + Nonce}
//        String daaInfo = daaInterface.DAAEnable();
//        Log.d("daaInfo", daaInfo);
//
//        // This is basically what the Wallet does - it signs the nonce
//        // Returns nonce_signed
//        // IORAM SIGNS HERE
//        byte[] signedEnable = daaInterface.prepareEnableResponse(daaInfo); //////////
//
//        // Call CreateEnableResponse: Creates the endorsementkey and returns registration object
//        // {AK, EK}
//        String issreg = daaInterface.CreateEnableResponse(signedEnable);
//
//        // Send it to the DAA issuer
//        String challenge = daaInterface.getIssuerChallenge(issreg);
//
//        // Call back into the core and get a response to the challenge
//        String challengeResponse = daaInterface.HandleIssuerChallenge(challenge);
//
//        // Send challenge response back to the issuer and obtain full credential
//        String fcre = daaInterface.sendChallengeResponse(challengeResponse);
//
//        // "Enable" the credential
//        daaInterface.EnableDAACredential(fcre);
//
//        // ------------------------------------------------- //
//
//
//        // Test signature
//        byte[] n = daaInterface.startDAASession();
//
//        byte[] signed = daaInterface.walletDoMeASignPlz(n);
//
//        byte[] derp = {0x00, 0x01, 0x03, 0x04};
//        String resp = daaInterface.DAASign(derp,signed);
//
//        Log.d("DAASign", resp);
//
//        if(daaInterface.verifySignature(resp,derp) == 1){
//            System.out.println("Signature Verified");
//        } else System.out.println("Signature Verification Failed");

    }
}
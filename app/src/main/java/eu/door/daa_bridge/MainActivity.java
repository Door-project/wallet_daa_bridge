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
//        NotificationService.getToken();
//
//
//        DAAInterface daaInterface = new DAAInterface();
//        daaInterface.initiateTPM();
//        WalletDaaBridgeData.getInstance()
//                .setDaaInterface(daaInterface);
//
//        System.out.println("Trying to write Public Key IS/WK");
//
//        String ik = "-----BEGIN PUBLIC KEY-----\n" +
//                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25Q\n" +
//                "hTF3PuomKkE3/ET4GcPMTkYi8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
//                "-----END PUBLIC KEY-----";
//
//        byte[] issuerPk = ik.getBytes();
//        daaInterface.registerIssuerPK(issuerPk);

        //---------------------------------------------------------------//


        //----------- TESTING --------------------------------------------//
        DAAInterface daaInterface = new DAAInterface();
        daaInterface.initiateTPM();


        System.out.println("Trying to write Public Key IS/WK");
        String wk_priv = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIFwwF26cdqGNrk52CO3Znrv5QfkVH6qJmB/1TdknDm/RoAoGCCqGSM49\n" +
                "AwEHoUQDQgAE7lux68P/xJHfp+xc07KhOmOTB6kXx1sm0+NaSql0xvbX9D/gV982\n" +
                "JG1lwk3qMKlIwYrkBJ7X2+iVBaZPU59/qQ==\n" +
                "-----END EC PRIVATE KEY-----\n";

        String wk = "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE7lux68P/xJHfp+xc07KhOmOTB6kX\n" +
                "x1sm0+NaSql0xvbX9D/gV982JG1lwk3qMKlIwYrkBJ7X2+iVBaZPU59/qQ==\n" +
                "-----END PUBLIC KEY-----\n";

        String ik = "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25Q\n" +
                "hTF3PuomKkE3/ET4GcPMTkYi8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
                "-----END PUBLIC KEY-----";

        String ik_priv = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIF8Cx/viWSyi0gCp/OcbMFJrbKmzO2PwlqA/RNtv9UMZoAoGCCqGSM49\n" +
                "AwEHoUQDQgAE4CwPPzL9DS6n2zcDsV1hOadgL25QhTF3PuomKkE3/ET4GcPMTkYi\n" +
                "8zd2IIUVI/FwY+sWTyHhCxrHkfXKksSAmA==\n" +
                "-----END EC PRIVATE KEY-----\n";

        byte[] walletKeyPriv = wk_priv.getBytes();
        byte[] issuerPk = ik.getBytes();
        byte[] walletPk = wk.getBytes();
        byte[] issuerPriv = ik_priv.getBytes();

        daaInterface.registerWallet_priv(walletKeyPriv);
        daaInterface.registerWalletPK(walletPk);
        daaInterface.registerIssuerPK(issuerPk);
        daaInterface.registerIssuer_priv(issuerPriv);

        // Boot TPM and get Endrosement Key
        // Returns {EK + Nonce}
        String daaInfo = daaInterface.DAAEnable();
        Log.d("daaInfo", daaInfo);

        // This is basically what the Wallet does - it signs the nonce
        // Returns nonce_signed
        // IORAM SIGNS HERE
        byte[] signedEnable = daaInterface.prepareEnableResponse(daaInfo); //////////

        // Call CreateEnableResponse: Creates the endorsementkey and returns registration object
        // {AK, EK}
        String issreg = daaInterface.CreateEnableResponse(signedEnable);

        // Send it to the DAA issuer
        String challenge = daaInterface.getIssuerChallenge(issreg);

        // Call back into the core and get a response to the challenge
        String challengeResponse = daaInterface.HandleIssuerChallenge(challenge);

        // Send challenge response back to the issuer and obtain full credential
        String fcre = daaInterface.sendChallengeResponse(challengeResponse);

        // "Enable" the credential
        daaInterface.EnableDAACredential(fcre);


        // Test signature
        byte[] n = daaInterface.startDAASession();

        byte[] signed = daaInterface.walletDoMeASignPlz(n);

        byte[] derp = {0x00, 0x01, 0x03, 0x04};
        daaInterface.DAASign(derp,signed);

//        if(daaInterface.verifySignature(resp,derp) == 1){
//            System.out.println("Signature Verified");
//        } else System.out.println("Signature Verification Failed");

    }
}
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
        daaInterface.registerIssuer_priv(issuerPriv);
    }
}
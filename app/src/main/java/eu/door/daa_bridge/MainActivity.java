package eu.door.daa_bridge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import eu.door.daa_bridge.service.NotificationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve and set the current registration token
        NotificationService.getToken();
    }
}
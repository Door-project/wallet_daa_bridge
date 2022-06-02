package eu.door.daa_bridge.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import eu.door.daa_bridge.logic.RegistrationLogic;
import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.DaaRegister;

public class NotificationService extends FirebaseMessagingService {

    RegistrationLogic logic = new RegistrationLogic();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();
        data.setToken(s);
        Log.d("Firebase_token", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("RemoteMessage", remoteMessage.getData().toString());

        DaaRegister daaRegister = new Gson().fromJson(
                remoteMessage.getData().toString(),
                DaaRegister.class
        );

        logic.daaRegister(daaRegister);
    }


    public static void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("Firebase_token", token);


                        WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();
                        data.setToken(token);
                    }
                });
    }
}

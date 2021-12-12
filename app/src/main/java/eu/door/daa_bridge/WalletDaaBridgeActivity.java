package eu.door.daa_bridge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eu.door.daa_bridge.payload.EnableRequest;
import eu.door.daa_bridge.payload.IssueRequest;

public class WalletDaaBridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_daa_bridge);

        Intent intent = getIntent();
        String action = intent.getAction();

        String req = intent.getStringExtra(DaaBridgeActions.EXTRA_STRING_REQ);
        if(req == null) {
            badRequest();
            return;
        }

        switch(action) {
            case DaaBridgeActions.ACTION_SIGN:
                Log.d("ACTION", "SIGN");
                sign(req);
                break;
            case DaaBridgeActions.ACTION_ENABLE:
                Log.d("ACTION", "ENABLE");
                enable(req);
                break;
            case DaaBridgeActions.ACTION_ISSUE:
                Log.d("ACTION", "ISSUE");
                issue(req);
                break;
            default:
                Log.d("ACTION", action);
                badRequest();
                break;
        }
    }

    private void sign(String request) {
        //sign nonce ...
        resultOk(MockData.signResponse());
    }

    private void enable(String request) {
        Gson gson = new Gson();
        try {
            EnableRequest req = gson.fromJson(request, EnableRequest.class);
            String response = gson.toJson(
                    MockData.enableResponse()
            );
            resultOk(response);
        }
        catch(JsonSyntaxException ex){
            Log.e("ENABLE", request);
            badRequest();
        }
    }

    private void issue(String request) {
        Gson gson = new Gson();
        try {
            IssueRequest req = gson.fromJson(request, IssueRequest.class);

            String response = new Gson().toJson(
                    MockData.issueResponse()
            );
            resultOk(response);
        }
        catch(JsonSyntaxException ex){
            Log.e("ISSUE", request);
            badRequest();
        }
    }

    private void resultOk(String response) {
        Intent result = new Intent();
        result.putExtra(DaaBridgeActions.EXTRA_STRING_RES, response);
        setResult(DaaBridgeActions.RESULT_OK, result);
        finish();
    }

    private void badRequest() {
        Intent result = new Intent();
        result.putExtra(DaaBridgeActions.EXTRA_STRING_RES, "Bad Request");
        setResult(DaaBridgeActions.RESULT_BAD_REQ, result);
        finish();
    }



}

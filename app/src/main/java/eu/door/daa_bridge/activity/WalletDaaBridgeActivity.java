package eu.door.daa_bridge.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eu.door.daa_bridge.action.DaaBridgeActions;
import eu.door.daa_bridge.R;
import eu.door.daa_bridge.logic.RegistrationLogic;
import eu.door.daa_bridge.model.ApplicationInfo;
import eu.door.daa_bridge.model.SecurityChallenge;
import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.ChallengeSolutionRequest;
import eu.door.daa_bridge.payload.ChallengeSolutionResponse;
import eu.door.daa_bridge.payload.EnableRequest;
import eu.door.daa_bridge.payload.IssueRequest;
import eu.door.daa_bridge.payload.MockData;

public class WalletDaaBridgeActivity extends AppCompatActivity {

    private static final String DEVICE_STATE_OK =  "_Device State OK_";

    WalletDaaBridgeData mData = WalletDaaBridgeData.getInstance();

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
            case DaaBridgeActions.ACTION_REGISTER:
                Log.d("ACTION", "REGISTER");
                register();
                break;
            case DaaBridgeActions.ACTION_CHALLENGE_SOLUTION:
                Log.d("ACTION", "CHALLENGE RESPONSE");
                challengeSolution(req);
                break;
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

    private ApplicationInfo getCallingApplicationInfo() {
        String packageName = this.getCallingActivity().getPackageName();
        PackageManager pm = getPackageManager();
        int uid = 0;
        try {
            uid = pm.getApplicationInfo(packageName, 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("ERROR", "calling package " + packageName + " not found.");
        }
        Log.d("CallingPackage", packageName);
        Log.d("CallingUid", ""+uid);
        return new ApplicationInfo(packageName, uid);
    }


    private void register() {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        mData.setCandidateApplicationInfo(callingApplicationInfo);
        mData.setSecurityChallenge(new SecurityChallenge());
        resultOk(mData.getSecurityChallenge().getChallenge());
    }

    private void challengeSolution(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isCandidate(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        ChallengeSolutionRequest req = gson.fromJson(request, ChallengeSolutionRequest.class);

        RegistrationLogic logic = new RegistrationLogic();
        Boolean verified = logic.verifyChallengeSolution(
                this,
                req.getSolution(),
                mData.getSecurityChallenge().getSolution()
        );

        if(!verified){
            unauthorized();
        }

        byte[] signature = logic.sign(this,  DEVICE_STATE_OK);
        ChallengeSolutionResponse challengeSolutionRes = new ChallengeSolutionResponse();
        challengeSolutionRes.setMessage(DEVICE_STATE_OK);
        challengeSolutionRes.setSignature(signature);
        String response = gson.toJson(challengeSolutionRes);
        Log.d("ChallengeSolutionRes", response);

        mData.setPairingApplicationInfo(mData.getCandidateApplicationInfo());
        resultOk(response);
    }

    private void sign(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        //sign nonce ...
        resultOk(MockData.signResponse());
    }

    private void enable(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

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
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }


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


    private void unauthorized() {
        Intent result = new Intent();
        result.putExtra(DaaBridgeActions.EXTRA_STRING_RES, "Unauthorized");
        setResult(DaaBridgeActions.RESULT_UNAUTHORIZED, result);
        finish();
    }


}
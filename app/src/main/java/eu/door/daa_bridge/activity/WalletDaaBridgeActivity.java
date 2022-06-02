package eu.door.daa_bridge.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import eu.door.daa_bridge.action.DaaBridgeActions;
import eu.door.daa_bridge.R;
import eu.door.daa_bridge.logic.IssueLogic;
import eu.door.daa_bridge.logic.RegistrationLogic;
import eu.door.daa_bridge.logic.SignLogic;
import eu.door.daa_bridge.model.ApplicationInfo;
import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.EnableRequest;
import eu.door.daa_bridge.payload.EnableResponse;
import eu.door.daa_bridge.payload.Evidence;
import eu.door.daa_bridge.payload.IssueObject;
import eu.door.daa_bridge.payload.IssueResponse;
import eu.door.daa_bridge.payload.NonceResponse;
import eu.door.daa_bridge.payload.RegisterRequest;
import eu.door.daa_bridge.payload.RegisterResponse;
import eu.door.daa_bridge.payload.RegnObject;
import eu.door.daa_bridge.payload.SignRequest;
import eu.door.daa_bridge.payload.IssueRequest;
import eu.door.daa_bridge.payload.SignErrorResponse;
import eu.door.daa_bridge.payload.SignResponse;

public class WalletDaaBridgeActivity extends AppCompatActivity {

    WalletDaaBridgeData mData = WalletDaaBridgeData.getInstance();

    RegistrationLogic registrationLogic = new RegistrationLogic();
    IssueLogic issueLogic = new IssueLogic();
    SignLogic signLogic = new SignLogic();

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
                register(req);
                break;
            case DaaBridgeActions.ACTION_ENABLE:
                Log.d("ACTION", "ENABLE");
                enable(req);
                break;
            case DaaBridgeActions.ACTION_NONCE:
                Log.d("ACTION", "NONCE");
                nonce();
                break;
            case DaaBridgeActions.ACTION_ISSUE:
                Log.d("ACTION", "ISSUE");
                issue(req);
                break;
            case DaaBridgeActions.ACTION_SIGN:
                Log.d("ACTION", "SIGN");
                sign(req);
                break;
            default:
                Log.d("ACTION", action);
                badRequest();
                break;
        }
    }

    /**
     * Return the application info(package name and UID) of the calling
     * activity.
     */
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

    /**
     * Executing the DAA_REGISTER phase
     *  - Receive public key, algorithm id and nonce from the wallet
     *  - Save public key and algorithm
     *  - Pairing of the DAA-Bridge component to this specific Wallet
     *  - Sign Nonce
     *
     * @Returns RegisterResponse with signed(nonce) or badRequest
     * if the wallet's public key is invalid
     */
    private void register(String request) {
        Gson gson = new Gson();
        RegisterRequest req = gson.fromJson(request, RegisterRequest.class);
        Log.d("Reqister req", request);

        Boolean isSaved = registrationLogic.saveCertificate(req.getAlgorithm(), req.getPublicKey());
        if(!isSaved){
            badRequest();
            return;
        }

        //Pairing with wallet as an additional layer of security
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        mData.setPairingApplicationInfo(callingApplicationInfo);

        byte[] signature = registrationLogic.signNonce(this, req.getNonce());

        RegisterResponse res = registrationLogic.createRegisterResponse(signature);
        resultOk(gson.toJson(res));
    }

    /**
     * Executing the DAA_ENABLE phase
     * - Receive signed<epochTime> from the wallet
     * - Verify calling application and signature
     * - Get the registration object
     *
     * @Returns EnableResponse with regnObject or unauthorized
     * if signature or calling application not verified
     */
    private void enable(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        EnableRequest req = gson.fromJson(request, EnableRequest.class);

        Boolean isVerified = registrationLogic.verify(req);
        if(!isVerified) {
            unauthorized();
            return;
        }

        RegnObject regnObject = registrationLogic.enable();

        EnableResponse res = registrationLogic.createEnableResponse(regnObject);
        resultOk(gson.toJson(res));
    }


    /**
     * Executing the DAA_NONCE phase
     * - Verify calling application
     * - Get TPM nonce
     *
     * @Returns NonceResponse with tpmNonce or unauthorized
     * if calling application not verified
     */
    private void nonce() {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        byte[] tpmNonce = issueLogic.getTpmNonce();

        NonceResponse res = issueLogic.createNonceResponse(tpmNonce);
        Gson gson = new Gson();
        resultOk(gson.toJson(res));
    }

    /**
     * Executing the DAA_ISSUE phase
     * - Receive signed<TPMNonce>
     * - Verify calling application and signature
     * - Get issue Object
     *
     * @Returns IssueResponse with issueObject or unauthorized
     * if signature or calling application not verified
     */
    private void issue(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        IssueRequest req = gson.fromJson(request, IssueRequest.class);

        Boolean isVerified = issueLogic.verify(req);
        if(!isVerified) {
            unauthorized();
            return;
        }

        IssueObject issueObject = issueLogic.getIssueObject(req.getNonce(), req.getSigned());

        IssueResponse res = issueLogic.createIssueResponse(issueObject);
        resultOk(gson.toJson(res));
    }

    /**
     * Executing the DAA_SIGN phase
     * - Receive SignVpRequest with signed<TPM Nonce> and RPNonce from the wallet
     * - Verify calling application and signature
     * - Verify evidence objects and sign RpNonce
     *
     * @Returns SignResponse with Signed(RPNonce) or
     * Error(RegnObject,{set of Credential-ids})
     * unauthorized if signature or calling application not verified or
     */
    private void sign(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        SignRequest req = gson.fromJson(request, SignRequest.class);

        Boolean isVerified = signLogic.verify(req);
        if(!isVerified) {
            unauthorized();
            return;
        }

        List<Evidence> unverified = new ArrayList<>();
        isVerified = signLogic.verifyEvidenceObjects(req.getEvidenceObjects(), unverified);

        if(!isVerified) {
            RegnObject regnObject = signLogic.enable();

            SignErrorResponse res = signLogic.createSignVpErrorResponse(regnObject, unverified);
            error(gson.toJson(res));
            return;
        }

        String daaSignature = signLogic.sign(req.getRPNonce(), req.getSigned());

        SignResponse res = signLogic.createSignResponse(daaSignature, req.getNonce());
        resultOk(gson.toJson(res));
    }


    private void resultOk(String response) {
        Intent result = new Intent();
        result.putExtra(DaaBridgeActions.EXTRA_STRING_RES, response);
        setResult(DaaBridgeActions.RESULT_OK, result);
        finish();
    }

    private void error(String response) {
        Intent result = new Intent();
        result.putExtra(DaaBridgeActions.EXTRA_STRING_RES, response);
        setResult(DaaBridgeActions.RESULT_NO_VALID_AIC, result);
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

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
import eu.door.daa_bridge.payload.RegisterRequest;
import eu.door.daa_bridge.payload.RegisterResponse;
import eu.door.daa_bridge.payload.RegnObject;
import eu.door.daa_bridge.payload.SignVcRequest;
import eu.door.daa_bridge.payload.SignVcResponse;
import eu.door.daa_bridge.payload.SignVpErrorResponse;
import eu.door.daa_bridge.payload.SignVpReqResponse;
import eu.door.daa_bridge.payload.SignVpRequest;
import eu.door.daa_bridge.payload.SignVpResponse;

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
            case DaaBridgeActions.ACTION_ISSUE:
                Log.d("ACTION", "ISSUE");
                issue();
                break;
            case DaaBridgeActions.ACTION_SIGN_VC:
                Log.d("ACTION", "SIGN_VC");
                signVC(req);
                break;
            case DaaBridgeActions.ACTION_SIGN_VP_REQ:
                Log.d("ACTION", "SIGN_VP_REQ");
                signVPReq();
                break;
            case DaaBridgeActions.ACTION_SIGN_VP:
                Log.d("ACTION", "SIGN_VP");
                signVP(req);
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
     * Executing the DAA_ISSUE phase
     * - Verify calling application
     * - Get TPM nonce
     *
     * @Returns IssueResponse with tpmNonce or unauthorized
     * if calling application not verified
     */
    private void issue() {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        byte[] tpmNonce = issueLogic.getTpmNonce();

        IssueResponse res = issueLogic.createIssueResponse(tpmNonce);
        Gson gson = new Gson();
        resultOk(gson.toJson(res));
    }

    /**
     * Executing the DAA_SIGN_VC phase
     * - Receive signed<TPMNonce>
     * - Verify calling application and signature
     * - Get issue Object
     *
     * @Returns SignVcResponse with issueObject or unauthorized
     * if signature or calling application not verified
     */
    private void signVC(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        SignVcRequest req = gson.fromJson(request, SignVcRequest.class);

        Boolean isVerified = issueLogic.verify(req);
        if(!isVerified) {
            unauthorized();
            return;
        }

        IssueObject issueObject = issueLogic.getIssueObject();

        SignVcResponse res = issueLogic.createSignVcResponse(issueObject);
        resultOk(gson.toJson(res));
    }


    /**
     * Executing the DAA_SIGN_VP_REQ phase
     * - Verify calling application
     * - Get TPM nonce
     *
     * @Returns SignVpReqResponse with tpmNonce or unauthorized
     * if calling application not verified
     */
    private void signVPReq() {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        byte[] tpmNonce = signLogic.getTpmNonce();

        SignVpReqResponse res = signLogic.createSignVpReqResponse(tpmNonce);
        Gson gson = new Gson();
        resultOk(gson.toJson(res));
    }


    /**
     * Executing the DAA_SIGN_VP phase
     * - Receive SignVpRequest with signed<TPM Nonce> and RPNonce from the wallet
     * - Verify calling application and signature
     * - Verify evidence objects and sign RpNonce
     *
     * @Returns SignVpResponse with Signed(RPNonce) or
     * Error(RegnObject,{set of Credential-ids})
     * unauthorized if signature or calling application not verified or
     */
    private void signVP(String request) {
        ApplicationInfo callingApplicationInfo = getCallingApplicationInfo();
        Boolean isPairing = mData.isPairing(callingApplicationInfo);
        if(!isPairing) {
            unauthorized();
            return;
        }

        Gson gson = new Gson();
        SignVpRequest req = gson.fromJson(request, SignVpRequest.class);

        Boolean isVerified = signLogic.verify(req);
        if(!isVerified) {
            unauthorized();
            return;
        }

        List<Evidence> unverified = new ArrayList<>();
        isVerified = signLogic.verifyEvidenceObjects(req.getEvidenceObjects(), unverified);

        if(!isVerified) {

            RegnObject regnObject = signLogic.enable();

            SignVpErrorResponse res = signLogic.createSignVpErrorResponse(regnObject, unverified);
            error(gson.toJson(res));
            return;
        }

        byte[] signedRpNonce = signLogic.sign(req.getRPNonce());

        SignVpResponse res = signLogic.createSignVpResponse(signedRpNonce);
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

package eu.door.daa_bridge.logic;

import android.util.Log;

import java.security.PublicKey;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.IssueResponse;
import eu.door.daa_bridge.payload.SignVcRequest;
import eu.door.daa_bridge.payload.SignVcResponse;
import eu.door.daa_bridge.util.SecurityUtil;

public class IssueLogic {

    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    public Boolean verify(SignVcRequest req) {
        PublicKey publicKey = data.getWalletPublicKey();
        Boolean verified = false;
        try {
            verified = SecurityUtil.verifySignature(
                    data.getKeyAlgorithm(),
                    publicKey,
                    req.getNonce(),
                    req.getSigned()
            );
        } catch (Exception e) {
            Log.d("verifySignature", "Failed to verify signature: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        return verified;
    }

    //integration with TPM library
    public SignVcResponse createSignVcResponse() {
        return new SignVcResponse();
    }


    //integration with TPM library
    public IssueResponse createIssueResponse() {
        return new IssueResponse();
    }

}

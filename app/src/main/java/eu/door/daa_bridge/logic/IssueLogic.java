package eu.door.daa_bridge.logic;

import android.util.Log;

import java.security.PublicKey;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.IssueObject;
import eu.door.daa_bridge.payload.IssueResponse;
import eu.door.daa_bridge.payload.NonceResponse;
import eu.door.daa_bridge.payload.IssueRequest;
import eu.door.daa_bridge.util.SecurityUtil;

public class IssueLogic {

    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    //integration with TPM library
    public Boolean verify(IssueRequest req) {
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
    public byte[] getTpmNonce() {
        return new byte[0];
    }

    //integration with TPM library
    public IssueResponse createIssueResponse(IssueObject issueObject) {
        IssueResponse response = new IssueResponse();
        response.setDaaCRD(issueObject.getDaaCRD());
        response.setNonce(issueObject.getNonce());
        response.setSignature(issueObject.getSignature());
        response.setId(issueObject.getId());
        return new IssueResponse();
    }

    //integration with TPM library
    public IssueObject getIssueObject() {
        return new IssueObject();
    }

    //integration with TPM library
    public NonceResponse createNonceResponse(byte[] tpmNonce) {
        NonceResponse res = new NonceResponse();
        res.setTpmNonce(tpmNonce);
        return res;
    }
}

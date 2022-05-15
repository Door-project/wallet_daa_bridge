package eu.door.daa_bridge.logic;

import android.util.Log;

import java.security.PublicKey;

import eu.door.daa_bridge.model.WalletDaaBridgeData;
import eu.door.daa_bridge.payload.IssueObject;
import eu.door.daa_bridge.payload.IssueResponse;
import eu.door.daa_bridge.payload.SignVcRequest;
import eu.door.daa_bridge.payload.SignVcResponse;
import eu.door.daa_bridge.util.SecurityUtil;

public class IssueLogic {

    private final WalletDaaBridgeData data = WalletDaaBridgeData.getInstance();

    //integration with TPM library
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
    public byte[] getTpmNonce() {
        return new byte[0];
    }

    //integration with TPM library
    public SignVcResponse sign(SignVcRequest req) {
        // IssueObject issueObj = tpm.sign(nonce, signature);
        return new SignVcResponse();
    }

    //integration with TPM library
    public IssueResponse createIssueResponse(byte[] tpmNonce) {
        IssueResponse res = new IssueResponse();
        res.setTpmNonce(tpmNonce);
        return res;
    }

    //integration with TPM library
    public IssueObject getIssueObject() {
        return new IssueObject();
    }

    //integration with TPM library
    public SignVcResponse createSignVcResponse(IssueObject req) {
        SignVcResponse res = new SignVcResponse();
        res.setDaaCRD(req.getDaaCRD());
        res.setId(req.getId());
        res.setSignature(req.getSignature());
        res.setNonce(req.getNonce());
        return res;
    }
}

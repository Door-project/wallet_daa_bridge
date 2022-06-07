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

    public byte[] getTpmNonce() {
        byte[] tmpNonce = data.getDaaInterface()
                .startDAASession();

        return tmpNonce;
    }

    public IssueResponse createIssueResponse(IssueObject issueObject) {
        IssueResponse response = new IssueResponse();
        response.setNonce(issueObject.getNonce());
        response.setDaaSignature(issueObject.getDaaSignature());
        return response;
    }

    public IssueObject getIssueObject(byte[] nonce, byte[] signed) {

        String resp = data.getDaaInterface().DAASign(nonce,signed);

        IssueObject issueObject = new IssueObject();
        issueObject.setDaaSignature(resp);
        issueObject.setNonce(nonce);
        return issueObject;
    }

    public NonceResponse createNonceResponse(byte[] tpmNonce) {
        NonceResponse res = new NonceResponse();
        res.setTpmNonce(tpmNonce);
        return res;
    }
}

package eu.door.daa_bridge.payload;

public class IssueResponse {
    private String daaSignature;
    private byte[] nonce;

    public IssueResponse() {
    }

    public String getDaaSignature() {
        return daaSignature;
    }

    public void setDaaSignature(String daaSignature) {
        this.daaSignature = daaSignature;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }
}

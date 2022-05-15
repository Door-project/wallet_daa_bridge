package eu.door.daa_bridge.payload;

public class IssueResponse {
    private byte[] tpmNonce;

    public IssueResponse() {
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }
}

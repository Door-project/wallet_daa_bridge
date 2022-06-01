package eu.door.daa_bridge.payload;

public class DaaInfo {
    private byte[] endorsementKey;
    private byte[] nonce;

    public DaaInfo() {
    }

    public byte[] getEndorsementKey() {
        return endorsementKey;
    }

    public void setEndorsementKey(byte[] endorsementKey) {
        this.endorsementKey = endorsementKey;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }
}

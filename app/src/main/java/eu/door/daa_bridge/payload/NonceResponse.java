package eu.door.daa_bridge.payload;

public class NonceResponse {
    private byte[] tpmNonce;

    public NonceResponse() {
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }
}

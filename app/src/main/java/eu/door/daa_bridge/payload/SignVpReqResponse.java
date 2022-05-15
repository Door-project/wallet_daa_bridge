package eu.door.daa_bridge.payload;

public class SignVpReqResponse {
    private byte[] tpmNonce;

    public SignVpReqResponse() {
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }
}

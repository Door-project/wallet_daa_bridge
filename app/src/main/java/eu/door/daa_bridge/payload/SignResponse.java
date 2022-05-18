package eu.door.daa_bridge.payload;

public class SignResponse {
    private byte[] signedRpNonce;

    public SignResponse() {
    }

    public byte[] getSignedRpNonce() {
        return signedRpNonce;
    }

    public void setSignedRpNonce(byte[] signedRpNonce) {
        this.signedRpNonce = signedRpNonce;
    }
}

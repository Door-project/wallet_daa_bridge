package eu.door.daa_bridge.payload;

public class SignVpResponse {
    private byte[] signedRpNonce;

    public SignVpResponse() {
    }

    public byte[] getSignedRpNonce() {
        return signedRpNonce;
    }

    public void setSignedRpNonce(byte[] signedRpNonce) {
        this.signedRpNonce = signedRpNonce;
    }
}

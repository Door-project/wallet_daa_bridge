package eu.door.daa_bridge.payload;

public class SignVcRequest {
    private byte[] nonce;
    private byte[] signed;

    public SignVcRequest() {
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getSigned() {
        return signed;
    }

    public void setSigned(byte[] signed) {
        this.signed = signed;
    }
}

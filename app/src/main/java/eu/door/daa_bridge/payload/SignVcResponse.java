package eu.door.daa_bridge.payload;

public class SignVcResponse {
    private byte[] daaCRD;
    private byte[] nonce;
    private byte[] signature;
    private String id;

    public SignVcResponse() {
    }

    public byte[] getDaaCRD() {
        return daaCRD;
    }

    public void setDaaCRD(byte[] daaCRD) {
        this.daaCRD = daaCRD;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

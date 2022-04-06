package eu.door.daa_bridge.payload;

public class RegisterRequest {
    private byte[] certificate;
    private String algorithm;
    private String nonce1;

    public RegisterRequest() {
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getNonce1() {
        return nonce1;
    }

    public void setNonce1(String nonce1) {
        this.nonce1 = nonce1;
    }
}

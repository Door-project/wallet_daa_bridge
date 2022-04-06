package eu.door.daa_bridge.payload;

public class VerifySignatureRequest {
    private String message;
    private byte[] signature;

    public VerifySignatureRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}

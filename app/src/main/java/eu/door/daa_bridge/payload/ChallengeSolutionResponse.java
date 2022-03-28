package eu.door.daa_bridge.payload;

public class ChallengeSolutionResponse {
    private String message;
    private byte[] signature;

    public ChallengeSolutionResponse() {
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

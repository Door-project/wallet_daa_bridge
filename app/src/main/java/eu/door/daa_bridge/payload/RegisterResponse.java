package eu.door.daa_bridge.payload;

public class RegisterResponse {
    private byte[] signed;

    public RegisterResponse() {
    }

    public byte[] getSigned() {
        return signed;
    }

    public void setSigned(byte[] signed) {
        this.signed = signed;
    }
}

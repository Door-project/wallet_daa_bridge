package eu.door.daa_bridge.payload;

public class RegisterResponse {
    private String nonse1;
    private String nonse2;
    private byte[] signed1;
    private byte[] signed2;

    public RegisterResponse() {
    }

    public String getNonse1() {
        return nonse1;
    }

    public void setNonse1(String nonse1) {
        this.nonse1 = nonse1;
    }

    public String getNonse2() {
        return nonse2;
    }

    public void setNonse2(String nonse2) {
        this.nonse2 = nonse2;
    }

    public byte[] getSigned1() {
        return signed1;
    }

    public void setSigned1(byte[] signed1) {
        this.signed1 = signed1;
    }

    public byte[] getSigned2() {
        return signed2;
    }

    public void setSigned2(byte[] signed2) {
        this.signed2 = signed2;
    }
}

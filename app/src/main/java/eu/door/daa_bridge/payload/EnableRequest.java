package eu.door.daa_bridge.payload;

public class EnableRequest {
    private byte[] time;
    private byte[] signed;

    public EnableRequest() {
    }

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public byte[] getSigned() {
        return signed;
    }

    public void setSigned(byte[] signed) {
        this.signed = signed;
    }
}

package eu.door.daa_bridge.payload;

public class IssueRequest {
    private byte[] s_AK;
    private byte[] ekHandle;
    private byte[] AIC;

    public IssueRequest() {
    }

    public byte[] getS_AK() {
        return s_AK;
    }

    public void setS_AK(byte[] s_AK) {
        this.s_AK = s_AK;
    }

    public byte[] getEkHandle() {
        return ekHandle;
    }

    public void setEkHandle(byte[] ekHandle) {
        this.ekHandle = ekHandle;
    }

    public byte[] getAIC() {
        return AIC;
    }

    public void setAIC(byte[] AIC) {
        this.AIC = AIC;
    }
}

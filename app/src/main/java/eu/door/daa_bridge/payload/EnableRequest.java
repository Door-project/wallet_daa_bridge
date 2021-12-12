package eu.door.daa_bridge.payload;

public class EnableRequest {
    private byte[] s_AK;
    private byte[] ekHandle;
    private byte[] certifyCredential;

    public EnableRequest() {
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

    public byte[] getCertifyCredential() {
        return certifyCredential;
    }

    public void setCertifyCredential(byte[] certifyCredential) {
        this.certifyCredential = certifyCredential;
    }
}

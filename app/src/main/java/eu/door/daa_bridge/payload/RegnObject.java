package eu.door.daa_bridge.payload;

public class RegnObject {
    private byte[] P_EK;
    private byte[] tpmNonce;
    private String token;

    public RegnObject() {
    }

    public RegnObject(DaaInfo daaInfoObj) {
        this.P_EK = daaInfoObj.getEndorsementKey();
        this.tpmNonce = daaInfoObj.getNonce();
    }

    public byte[] getP_EK() {
        return P_EK;
    }

    public void setP_EK(byte[] p_EK) {
        P_EK = p_EK;
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package eu.door.daa_bridge.payload;

//integration with tpm library
public class RegnObject {
    private byte[] P_EK;
    private byte[] tpmNonce;

    public RegnObject() {
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
}

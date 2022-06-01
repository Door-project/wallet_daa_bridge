package eu.door.daa_bridge.payload;

public class EnableResponse {
    private RegnObject regnObject;
    private byte[] tpmNonce;

    public EnableResponse() {
    }

    public RegnObject getRegnObject() {
        return regnObject;
    }

    public void setRegnObject(RegnObject regnObject) {
        this.regnObject = regnObject;
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }
}

package eu.door.daa_bridge.payload;

public class DaaRegister {
    private byte[] signedTpmNonce;
    private byte[] tpmNonce;
    private RegnObject regnObject;

    public DaaRegister() {
    }

    public DaaRegister(byte[] signedTpmNonce, byte[] tpmNonce, RegnObject regnObject) {
        this.signedTpmNonce = signedTpmNonce;
        this.tpmNonce = tpmNonce;
        this.regnObject = regnObject;
    }

    public byte[] getSignedTpmNonce() {
        return signedTpmNonce;
    }

    public void setSignedTpmNonce(byte[] signedTpmNonce) {
        this.signedTpmNonce = signedTpmNonce;
    }

    public byte[] getTpmNonce() {
        return tpmNonce;
    }

    public void setTpmNonce(byte[] tpmNonce) {
        this.tpmNonce = tpmNonce;
    }

    public RegnObject getRegnObject() {
        return regnObject;
    }

    public void setRegnObject(RegnObject regnObject) {
        this.regnObject = regnObject;
    }
}

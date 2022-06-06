package eu.door.daa_bridge.payload;

public class DaaRegister {
    private byte[] signedTpmNonce;

    public DaaRegister() {
    }

    public DaaRegister(byte[] signedTpmNonce) {
        this.signedTpmNonce = signedTpmNonce;
    }

    public byte[] getSignedTpmNonce() {
        return signedTpmNonce;
    }

    public void setSignedTpmNonce(byte[] signedTpmNonce) {
        this.signedTpmNonce = signedTpmNonce;
    }

}

package eu.door.daa_bridge.payload;


import java.util.List;

public class SignRequest {
    private byte[] nonce;
    private byte[] signed;
    private byte[] RPNonce;
    private List<Evidence> evidenceObjects;

    public SignRequest() {
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getSigned() {
        return signed;
    }

    public void setSigned(byte[] signed) {
        this.signed = signed;
    }

    public byte[] getRPNonce() {
        return RPNonce;
    }

    public void setRPNonce(byte[] RPNonce) {
        this.RPNonce = RPNonce;
    }

    public List<Evidence> getEvidenceObjects() {
        return evidenceObjects;
    }

    public void setEvidenceObjects(List<Evidence> evidenceObjects) {
        this.evidenceObjects = evidenceObjects;
    }
}

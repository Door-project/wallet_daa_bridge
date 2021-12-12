package eu.door.daa_bridge.payload;

import java.util.List;

public class RegistrationPackage {
    private byte[] P_AK;
    private byte[] P_EK;
    private List<String> eccpairings;
    private String commitValue;
    private String signature;

    public RegistrationPackage() {
    }

    public byte[] getP_AK() {
        return P_AK;
    }

    public void setP_AK(byte[] p_AK) {
        P_AK = p_AK;
    }

    public byte[] getP_EK() {
        return P_EK;
    }

    public void setP_EK(byte[] p_EK) {
        P_EK = p_EK;
    }

    public List<String> getEccpairings() {
        return eccpairings;
    }

    public void setEccpairings(List<String> eccpairings) {
        this.eccpairings = eccpairings;
    }

    public String getCommitValue() {
        return commitValue;
    }

    public void setCommitValue(String commitValue) {
        this.commitValue = commitValue;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

package eu.door.daa_bridge.payload;

import java.util.List;

public class EnableResponse {
    private Boolean granted;
    private byte[] P_AK;
    private byte[] P_EK;
    private List<String> eccPairings;
    private String commitValue;
    private String signature;
    private RegistrationPackage registrationpackage;

    public EnableResponse() {
    }

    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
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

    public List<String> getEccPairings() {
        return eccPairings;
    }

    public void setEccPairings(List<String> eccPairings) {
        this.eccPairings = eccPairings;
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

    public RegistrationPackage getRegistrationpackage() {
        return registrationpackage;
    }

    public void setRegistrationpackage(RegistrationPackage registrationpackage) {
        this.registrationpackage = registrationpackage;
    }
}

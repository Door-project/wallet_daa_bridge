package eu.door.daa_bridge.payload;

public class IssueResponse {
    private Boolean granted;
    private byte[] AIC;
    private byte[] P_EK;
    private String id;
    private String nonce;
    private String signature;
    private IssuePackage issuepackage;

    public IssueResponse() {
    }

    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public byte[] getAIC() {
        return AIC;
    }

    public void setAIC(byte[] AIC) {
        this.AIC = AIC;
    }

    public byte[] getP_EK() {
        return P_EK;
    }

    public void setP_EK(byte[] p_EK) {
        P_EK = p_EK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public IssuePackage getIssuepackage() {
        return issuepackage;
    }

    public void setIssuepackage(IssuePackage issuepackage) {
        this.issuepackage = issuepackage;
    }
}

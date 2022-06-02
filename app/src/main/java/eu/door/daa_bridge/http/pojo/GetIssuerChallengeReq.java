package eu.door.daa_bridge.http.pojo;

public class GetIssuerChallengeReq {
    private String issreg;

    public GetIssuerChallengeReq() {
    }

    public GetIssuerChallengeReq(String issreg) {
        this.issreg = issreg;
    }

    public String getIssreg() {
        return issreg;
    }

    public void setIssreg(String issreg) {
        this.issreg = issreg;
    }
}

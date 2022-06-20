package eu.door.daa_bridge.http.pojo;

public class GetIssuerChallengeReq {
    private String issreg;
    private String walletPublicKey;

    public GetIssuerChallengeReq() {
    }

    public GetIssuerChallengeReq(String issreg, String walletPublicKey) {
        this.issreg = issreg;
        this.walletPublicKey = walletPublicKey;
    }

    public String getIssreg() {
        return issreg;
    }

    public void setIssreg(String issreg) {
        this.issreg = issreg;
    }

    public String getWalletPublicKey() {
        return walletPublicKey;
    }

    public void setWalletPublicKey(String walletPublicKey) {
        this.walletPublicKey = walletPublicKey;
    }
}

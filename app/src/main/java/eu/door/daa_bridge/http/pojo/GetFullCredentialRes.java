package eu.door.daa_bridge.http.pojo;

public class GetFullCredentialRes {
    private String fcre;

    public GetFullCredentialRes() {
    }

    public GetFullCredentialRes(String fcre) {
        this.fcre = fcre;
    }

    public String getFcre() {
        return fcre;
    }

    public void setFcre(String fcre) {
        this.fcre = fcre;
    }
}

package eu.door.daa_bridge.http.pojo;

public class GetIssuerChallengeRes {
    private String challenge;

    public GetIssuerChallengeRes() {
    }

    public GetIssuerChallengeRes(String challenge) {
        this.challenge = challenge;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}

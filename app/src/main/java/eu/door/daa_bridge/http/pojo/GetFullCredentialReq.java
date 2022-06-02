package eu.door.daa_bridge.http.pojo;

public class GetFullCredentialReq {
    private String challengeResponse;

    public GetFullCredentialReq() {
    }

    public GetFullCredentialReq(String challengeResponse) {
        this.challengeResponse = challengeResponse;
    }

    public String getChallengeResponse() {
        return challengeResponse;
    }

    public void setChallengeResponse(String challengeResponse) {
        this.challengeResponse = challengeResponse;
    }
}

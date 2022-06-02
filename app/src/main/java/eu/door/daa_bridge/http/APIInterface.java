package eu.door.daa_bridge.http;

import eu.door.daa_bridge.http.pojo.DAAUserHandle;
import eu.door.daa_bridge.http.pojo.EnabledFullCredentialReq;
import eu.door.daa_bridge.http.pojo.GetFullCredentialReq;
import eu.door.daa_bridge.http.pojo.GetFullCredentialRes;
import eu.door.daa_bridge.http.pojo.GetIssuerChallengeReq;
import eu.door.daa_bridge.http.pojo.GetIssuerChallengeRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("/api/daaUserHandle")
    Call<String> daaUserHandle(@Body DAAUserHandle userHandle);

    @POST("/api/getIssuerChallenge")
    Call<GetIssuerChallengeRes> getIssuerChallenge(@Body GetIssuerChallengeReq getIssuerChallengeReq);

    @POST("/api/getFullCredential")
    Call<GetFullCredentialRes> getFullCredential(@Body GetFullCredentialReq getFullCredentialReq);

    @POST("/api/enabledFullCredential")
    Call<String> enabledFullCredential(@Body EnabledFullCredentialReq enabledFullCredentialReq);
}

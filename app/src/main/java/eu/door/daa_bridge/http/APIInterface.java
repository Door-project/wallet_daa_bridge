package eu.door.daa_bridge.http;

import eu.door.daa_bridge.http.pojo.DAAUserHandle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("/api/daaUserHandle")
    Call<String> daaUserHandle(@Body DAAUserHandle userHandle);
}

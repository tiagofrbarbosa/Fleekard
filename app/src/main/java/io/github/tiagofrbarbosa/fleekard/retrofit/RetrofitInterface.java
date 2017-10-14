package io.github.tiagofrbarbosa.fleekard.retrofit;

import io.github.tiagofrbarbosa.fleekard.model.ResultDistanceMatrix;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tfbarbosa on 14/10/17.
 */

public interface RetrofitInterface {

    @GET("distancematrix/json")
    Call<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins, @Query("destinations") String destinations);
}

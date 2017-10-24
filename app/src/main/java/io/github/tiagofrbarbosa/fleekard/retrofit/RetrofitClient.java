package io.github.tiagofrbarbosa.fleekard.retrofit;

import io.github.tiagofrbarbosa.fleekard.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tfbarbosa on 14/10/17.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static final String DISTANCE_MATRIX_API_KEY = BuildConfig.DISTANCE_MATRIX_API_KEY;
    public static final String api_url = "https://maps.googleapis.com/maps/api/";

    public static Retrofit getClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

package io.github.tiagofrbarbosa.fleekard.asynctask;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import io.github.tiagofrbarbosa.fleekard.model.ResultDistanceMatrix;
import io.github.tiagofrbarbosa.fleekard.retrofit.RetrofitClient;
import io.github.tiagofrbarbosa.fleekard.retrofit.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

/**
 * Created by tfbarbosa on 14/10/17.
 */

public class DistanceAsyncTask extends AsyncTask<String, Void, String> {

    private String mDistance;

    @Override
    protected String doInBackground(String... params) {

        RetrofitInterface retrofitService = RetrofitClient.getClient().create(RetrofitInterface.class);
        Call<ResultDistanceMatrix> call = retrofitService.getDistance(RetrofitClient.DISTANCE_MATRIX_API_KEY, params[0], params[1]);
        Response<ResultDistanceMatrix> response = null;

        Timber.tag("mAsyncTask").e("params[0]: " + params[0] + " params[1]: " + params[1]);
        Timber.tag("mAsyncTask").e(call.request().toString());

        try {
            response = call.execute();
        }catch (IOException e){
            Timber.tag("mAsyncTask").e(e.getMessage());
        }

        if(response.body().status.equals("OK") && response.body().rows.get(0).elements.get(0).status.equals("OK")){
            mDistance = response.body().rows.get(0).elements.get(0).distance.text;
        }else{

            double latitude_origin = Double.valueOf(params[2]);
            double longitude_origin = Double.valueOf(params[3]);
            double latitude_destination = Double.valueOf(params[4]);
            double longitude_destination = Double.valueOf(params[5]);

            double computeDistanceBetween = computeDistanceBetween(new LatLng(latitude_origin,longitude_origin)
                    , new LatLng(latitude_destination,longitude_destination));

            if(computeDistanceBetween > 1000) {
                mDistance = String.valueOf(String.format("%.0f",computeDistanceBetween / 1000));
                mDistance = mDistance.concat(" km");
            }else{
                mDistance = String.valueOf(String.format("%.0f",computeDistanceBetween));
                mDistance = mDistance.concat(" m");
            }
        }

        return mDistance;
    }

    @Override
    protected void onPostExecute(String result){
        Timber.tag("mAsyncTask").e("onPostExecute: " + result);
    }
}

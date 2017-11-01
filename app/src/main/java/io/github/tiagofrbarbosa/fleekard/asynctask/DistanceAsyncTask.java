package io.github.tiagofrbarbosa.fleekard.asynctask;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import io.github.tiagofrbarbosa.fleekard.adapter.UserAdapter;
import io.github.tiagofrbarbosa.fleekard.holder.UsersViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.ResultDistanceMatrix;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.retrofit.RetrofitClient;
import io.github.tiagofrbarbosa.fleekard.retrofit.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

/**
 * Created by tfbarbosa on 14/10/17.
 */

public class DistanceAsyncTask extends AsyncTask<Object , Void, User> {

    private String mDistance;
    private UserAdapter.DistanceAsyncListener onFinishListener;
    private UsersViewHolder holder;
    private int position;

    private static final String TAG_ASYNC = "myAsyncTask";
    public static final String TAG_LISTENER = "myAsyncListener";

    public DistanceAsyncTask(UserAdapter.DistanceAsyncListener distanceAsyncListener, UsersViewHolder holder, int position){
        this.onFinishListener = distanceAsyncListener;
        this.holder = holder;
        this.position = position;
    }

    @Override
    protected User doInBackground(Object... params) {

        String param00 = (String) params[0];
        String param01 = (String) params[1];

        RetrofitInterface retrofitService = RetrofitClient.getClient().create(RetrofitInterface.class);
        Call<ResultDistanceMatrix> call = retrofitService.getDistance(RetrofitClient.DISTANCE_MATRIX_API_KEY, param00, param01);
        Response<ResultDistanceMatrix> response = null;

        Timber.tag(TAG_ASYNC).i("params[0]: " + param00 + " params[1]: " + param01);
        Timber.tag(TAG_ASYNC).i(call.request().toString());

        try {
            response = call.execute();
        }catch (IOException e){
            Timber.tag(TAG_ASYNC).e(e.getMessage());
        }

        if(response.body().status.equals("OK") && response.body().rows.get(0).elements.get(0).status.equals("OK")){
            mDistance = response.body().rows.get(0).elements.get(0).distance.text;
        }else{

            String param02 = (String) params[2];
            String param03 = (String) params[3];
            String param04 = (String) params[4];
            String param05 = (String) params[5];

            double latitude_origin = Double.valueOf(param02);
            double longitude_origin = Double.valueOf(param03);
            double latitude_destination = Double.valueOf(param04);
            double longitude_destination = Double.valueOf(param05);

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

        User user = (User) params[06];
        user.setDistance(mDistance);

        return user;
    }

    @Override
    protected void onPostExecute(User result){
        onFinishListener.onFinish(result, holder, position);
    }
}

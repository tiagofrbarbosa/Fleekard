package io.github.tiagofrbarbosa.fleekard.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import timber.log.Timber;

/**
 * Created by tfbarbosa on 14/10/17.
 */

public class DistanceAsyncTask extends AsyncTask<String, Void, String> {

    private String mString;

    @Override
    protected String doInBackground(String... params) {
        mString = params[0] + " " + params[1];
        return mString;
    }

    @Override
    protected void onPostExecute(String result){
        Timber.tag("mAsyncTask").i(result);
    }
}

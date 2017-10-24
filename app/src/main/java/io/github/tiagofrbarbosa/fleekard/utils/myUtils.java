package io.github.tiagofrbarbosa.fleekard.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by tfbarbosa on 23/10/17.
 */

public class myUtils {

    private Context context;

    public myUtils(Context context){
        this.context = context;
    }

    private static final String TAG_CONNECTION = "myConnection";

    public boolean checkConnecton(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connectionStatus = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Timber.tag(TAG_CONNECTION).i("Connection: " + String.valueOf(connectionStatus));
        return connectionStatus;
    }

    public String longToDate(long time, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }
}

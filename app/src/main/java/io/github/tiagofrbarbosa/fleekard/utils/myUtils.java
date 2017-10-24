package io.github.tiagofrbarbosa.fleekard.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tfbarbosa on 23/10/17.
 */

public class myUtils {

    private Context context;

    public myUtils(Context context){
        this.context = context;
    }

    public boolean checkConnecton(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

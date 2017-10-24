package io.github.tiagofrbarbosa.fleekard.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/10/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG_TOKEN = "myTokenService";

    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token){
        Timber.tag(TAG_TOKEN).e("Service: " + token);
    }
}

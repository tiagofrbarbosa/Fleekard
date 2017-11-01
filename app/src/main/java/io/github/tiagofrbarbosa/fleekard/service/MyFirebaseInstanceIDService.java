package io.github.tiagofrbarbosa.fleekard.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.NotificationToken;
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

        FleekardApplication app = (FleekardApplication) getApplication();

        Timber.tag(TAG_TOKEN).e("Service: " + token);

        DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        NotificationToken notificationToken = new NotificationToken(token);

        mUserReference
                .child(app.getmAppUser().getUserKey())
                .child(Database.users.USER_NOTIFICATION_TOKEN)
                .setValue(notificationToken);
    }
}

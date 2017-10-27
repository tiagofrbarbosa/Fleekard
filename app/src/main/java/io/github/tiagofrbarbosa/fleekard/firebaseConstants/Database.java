package io.github.tiagofrbarbosa.fleekard.firebaseConstants;

/**
 * Created by tfbarbosa on 24/09/17.
 */

public final class Database {

    public static final class users {

        public static final String USER_AVATAR_IMG = "https://firebasestorage.googleapis.com/v0/b/fleekard.appspot.com/o/app_images%2Fuser_avatar.png?alt=media&token=0e719bac-b4da-4275-b37b-3271a623b185";
        public static final String CHILD_USERS = "users";
        public static final String USER_ID = "userId";
        public static final String USER_KEY = "userKey";
        public static final String USER_NAME = "userName";
        public static final String USER_STATUS = "userStatus";
        public static final String USER_IMAGE = "img";
        public static final String USER_EMAIL = "email";
        public static final String USER_GENDER = "gender";
        public static final String USER_AGE = "age";
        public static final String USER_LOCATION = "userLocation";
        public static final String USER_PRESENCE = "userPresence";
        public static final String USER_IMAGE_AVATAR = "NoImage";
        public static final String USER_NOTIFICATION_TOKEN = "notificationToken";
        public static final String USER_NOTIFICATION_VALUE = "token";
    }

    public static final class messages {

        public static final String CHILD_MESSAGES = "messages";
        public static final String MESSAGE_READ = "readMessage";
    }

    public static final class chats {

        public static final String CHILD_CHATS = "chats";
        public static final String CHAT_ID = "chatId";
    }

    public static final class favorite {

        public static final String CHILD_FAVORITES = "favorites";
    }

    public static final class notification {

        public static final String CHILD_NOTIFICATION = "notifications";
        public static final String USER_KEY_NOTIFICATE = "userKeyNotificate";
        public static final String USER_UID = "userUid";
        public static final String NOTIFICATION_UNREAD = "notificationRead";
    }

    public static final class notification_message {

        public static final String CHILD_NOTIFICATION_MESSAGE = "notification_message";
    }
}

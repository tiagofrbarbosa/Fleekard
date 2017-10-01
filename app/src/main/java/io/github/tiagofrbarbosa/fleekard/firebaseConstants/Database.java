package io.github.tiagofrbarbosa.fleekard.firebaseConstants;

/**
 * Created by tfbarbosa on 24/09/17.
 */

public final class Database {

    public static final class users {

        public static final String CHILD_USERS = "users";

        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_STATUS = "userStatus";
        public static final String USER_IMAGE = "img";
        public static final String USER_EMAIL = "email";
        public static final String USER_GENDER = "gender";
        public static final String USER_AGE = "age";
        public static final String USER_IAMGE_AVATAR = "NoImage";
    }

    public static final class messages {

        public static final String CHILD_MESSAGES = "messages";
    }
}

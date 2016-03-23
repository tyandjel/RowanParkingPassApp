package com.example.android.rowanparkingpass.utilities.database;

import android.provider.BaseColumns;

public final class UserContract extends BaseContract {

    public UserContract() {
    }

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_IS_ADMIN = "is_admin";
        public static final String COLUMN_SYNC = "sync";
        public static final String[] selectionArgs = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_IS_ADMIN, COLUMN_SYNC};
    }
}

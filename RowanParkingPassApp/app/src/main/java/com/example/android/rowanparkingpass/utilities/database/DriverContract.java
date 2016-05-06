package com.example.android.rowanparkingpass.utilities.database;

import android.provider.BaseColumns;

/**
 * Driver columns
 */
public final class DriverContract extends BaseContract {

    public DriverContract() {
    }

    public static abstract class DriverEntry implements BaseColumns {
        public static final String TABLE_NAME = "Driver";
        public static final String COLUMN_DRIVER_ID = "driver_id";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";
    }


}

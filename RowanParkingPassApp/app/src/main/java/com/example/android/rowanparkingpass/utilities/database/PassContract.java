package com.example.android.rowanparkingpass.utilities.database;

import android.provider.BaseColumns;

public final class PassContract extends BaseContract {

    public PassContract() {
    }

    public static abstract class PassEntry implements BaseColumns {
        public static final String TABLE_NAME = "Requests";
        public static final String COLUMN_REQUEST_ID = "request_id";
        public static final String COLUMN_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_DRIVER_ID = "driver_id";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
    }
}

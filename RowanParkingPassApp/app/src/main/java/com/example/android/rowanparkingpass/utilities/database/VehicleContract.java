package com.example.android.rowanparkingpass.utilities.database;

import android.provider.BaseColumns;

/**
 * Vehicle columns
 */
public final class VehicleContract extends BaseContract {

    public VehicleContract() {
    }

    public static abstract class VehicleEntry implements BaseColumns {
        public static final String TABLE_NAME = "Vehicles";
        public static final String COLUMN_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_MAKE = "make";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_LICENSE = "license";
    }
}

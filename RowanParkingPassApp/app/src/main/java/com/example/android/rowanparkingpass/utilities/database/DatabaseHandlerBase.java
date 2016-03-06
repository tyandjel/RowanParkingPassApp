package com.example.android.rowanparkingpass.utilities.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseHandlerBase extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "Parking.db";


    public DatabaseHandlerBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

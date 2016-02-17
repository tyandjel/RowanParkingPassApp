package com.example.android.rowanparkingpass.utilities.databasehandler;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseHandlerBase extends SQLiteOpenHelper {

    // Database Version
     static final int DATABASE_VERSION = 1;

    // Database Name
     static final String DATABASE_NAME = "rowan_car_pass";


    public DatabaseHandlerBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
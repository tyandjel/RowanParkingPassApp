package com.example.android.rowanparkingpass.utilities.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database handler base
 */
public abstract class DatabaseHandlerBase extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "Parking.db";

    // Pass create table statement
    protected static final String SQL_CREATE_PASS_ENTRIES =
            "CREATE TABLE " + PassContract.PassEntry.TABLE_NAME + " (" +
                    PassContract.PassEntry.COLUMN_REQUEST_ID + PassContract.INTEGER_TYPE + " PRIMARY KEY," +
                    PassContract.PassEntry.COLUMN_VEHICLE_ID + PassContract.INTEGER_TYPE + VehicleContract.COMMA_SEP +
                    PassContract.PassEntry.COLUMN_DRIVER_ID + PassContract.INTEGER_TYPE + VehicleContract.COMMA_SEP +
                    PassContract.PassEntry.COLUMN_START_DATE + PassContract.DATE_TYPE + VehicleContract.COMMA_SEP +
                    PassContract.PassEntry.COLUMN_END_DATE + PassContract.DATE_TYPE + " )";

    // Driver create table statement
    protected static final String SQL_CREATE_DRIVER_ENTRIES =
            "CREATE TABLE " + DriverContract.DriverEntry.TABLE_NAME + " (" +
                    DriverContract.DriverEntry.COLUMN_DRIVER_ID + DriverContract.INTEGER_TYPE + " PRIMARY KEY," +
                    DriverContract.DriverEntry.COLUMN_FULL_NAME + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_STREET + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_CITY + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_STATE + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_ZIP + DriverContract.TEXT_TYPE + " )";

    // Vehicle create table statement
    protected static final String SQL_CREATE_VEHICLE_ENTRIES =
            "CREATE TABLE " + VehicleContract.VehicleEntry.TABLE_NAME + " (" +
                    VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID + VehicleContract.INTEGER_TYPE + " PRIMARY KEY," +
                    VehicleContract.VehicleEntry.COLUMN_MAKE + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_MODEL + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_YEAR + VehicleContract.INTEGER_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_STATE + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_COLOR + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_LICENSE + VehicleContract.TEXT_TYPE + " )";

    public DatabaseHandlerBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

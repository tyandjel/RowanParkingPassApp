package com.example.android.rowanparkingpass.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.rowanparkingpass.personinfo.Driver;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerDrivers extends DatabaseHandlerBase {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DriverContract.DriverEntry.TABLE_NAME + " (" +
                    DriverContract.DriverEntry.COLUMN_DRIVER_ID + DriverContract.INTEGER_TYPE + " PRIMARY KEY," +
                    DriverContract.DriverEntry.COLUMN_FULL_NAME + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_STREET + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_CITY + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_STATE + DriverContract.TEXT_TYPE + DriverContract.COMMA_SEP +
                    DriverContract.DriverEntry.COLUMN_ZIP + DriverContract.TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DriverContract.DriverEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + DriverContract.DriverEntry.TABLE_NAME;

    public DatabaseHandlerDrivers(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TAG", "DRIVERS ON CREATE");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(SQL_DELETE_ENTRIES);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing Driver details in database
     */
    public void addDriver(int driverId, String firstName, String lastName, String street, String city, String state, String zip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DriverContract.DriverEntry.COLUMN_DRIVER_ID, driverId);
        values.put(DriverContract.DriverEntry.COLUMN_FULL_NAME, firstName + " " + lastName); // Full Name
        values.put(DriverContract.DriverEntry.COLUMN_STREET, street); // Street
        values.put(DriverContract.DriverEntry.COLUMN_CITY, city); // City
        values.put(DriverContract.DriverEntry.COLUMN_STATE, state); // State
        values.put(DriverContract.DriverEntry.COLUMN_ZIP, zip); // Zip
        // Inserting Row
        db.insert(DriverContract.DriverEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update visitor details in database
     */
    public void updateUser(String driverId, String firstName, String lastName, String street, String city, String state, String zip) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DriverContract.DriverEntry.COLUMN_FULL_NAME, firstName + " " + lastName); // Full Name
        values.put(DriverContract.DriverEntry.COLUMN_STREET, street); // Street
        values.put(DriverContract.DriverEntry.COLUMN_CITY, city); // City
        values.put(DriverContract.DriverEntry.COLUMN_STATE, state); // State
        values.put(DriverContract.DriverEntry.COLUMN_ZIP, zip); // Zip
        // Update Row
        db.update(DriverContract.DriverEntry.TABLE_NAME, values, DriverContract.DriverEntry.COLUMN_DRIVER_ID + "=" + driverId, null);
    }

    /**
     * Getting driver data from database
     */
    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> rows = new ArrayList<>();
        HashMap<String, String> driver = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            driver.put(DriverContract.DriverEntry.COLUMN_DRIVER_ID, cursor.getString(0));
            driver.put(DriverContract.DriverEntry.COLUMN_FULL_NAME, cursor.getString(1)); // Full Name
            driver.put(DriverContract.DriverEntry.COLUMN_STREET, cursor.getString(2)); // Street
            driver.put(DriverContract.DriverEntry.COLUMN_CITY, cursor.getString(3)); // City
            driver.put(DriverContract.DriverEntry.COLUMN_STATE, cursor.getString(4)); // State
            driver.put(DriverContract.DriverEntry.COLUMN_ZIP, cursor.getString(5)); // Zip
            // Split the full name
            String[] fullName = driver.get(DriverContract.DriverEntry.COLUMN_FULL_NAME).split(" ");
            String firstName = fullName[0];
            String lastName = "";
            for (int i = 1; i < fullName.length - 1; i++) {
                lastName += fullName[i];
            }
            rows.add(new Driver(Integer.parseInt(driver.get(DriverContract.DriverEntry.COLUMN_DRIVER_ID)),
                    firstName, lastName, driver.get(DriverContract.DriverEntry.COLUMN_STREET),
                    driver.get(DriverContract.DriverEntry.COLUMN_CITY),
                    driver.get(DriverContract.DriverEntry.COLUMN_STATE),
                    driver.get(DriverContract.DriverEntry.COLUMN_ZIP)));
            driver.clear();
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        return rows;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re create database
     * Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(DriverContract.DriverEntry.TABLE_NAME, null, null);
        db.close();
    }

}

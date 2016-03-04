package com.example.android.rowanparkingpass.utilities.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.rowanparkingpass.personinfo.Driver;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerDrivers extends DatabaseHandlerBase {

    // Login table name
    private static final String TABLE_DRIVER = "Driver";

    // Login Table Columns names
    private static final String KEY_DRIVER_ID = "driver_id";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_STREET = "street";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_ZIP = "zip";

    public DatabaseHandlerDrivers(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITOR_TABLE = "CREATE TABLE " + TABLE_DRIVER + "("
                + KEY_DRIVER_ID + " INTEGER PRIMARY KEY,"
                + KEY_FULL_NAME + " TEXT,"
                + KEY_STREET + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_STATE + " TEXT,"
                + KEY_ZIP + " TEXT" + ")";
        db.execSQL(CREATE_VISITOR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing visitor details in database
     */
    public void addVistor(String firstName, String lastName, String street, String city, String state, String zip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME, firstName + " " + lastName); // Full Name
        values.put(KEY_STREET, street); // Street
        values.put(KEY_CITY, city); // City
        values.put(KEY_STATE, state); // State
        values.put(KEY_ZIP, zip); // Zip
        // Inserting Row
        db.insert(TABLE_DRIVER, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update visitor details in database
     */
    public void updateUser(String driverId, String firstName, String lastName, String street, String city, String state, String zip) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME, firstName + " " + lastName); // Full Name
        values.put(KEY_STREET, street); // Street
        values.put(KEY_CITY, city); // City
        values.put(KEY_STATE, state); // State
        values.put(KEY_ZIP, zip); // Zip
        // Update Row
        db.update(TABLE_DRIVER, values, KEY_DRIVER_ID + "=" + driverId, null);
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Driver> getVisitors() {
        ArrayList<Driver> rows = new ArrayList<>();
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_DRIVER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user.put(KEY_DRIVER_ID, cursor.getString(0));
            user.put(KEY_FULL_NAME, cursor.getString(1));
            user.put(KEY_STREET, cursor.getString(2));
            user.put(KEY_CITY, cursor.getString(3));
            user.put(KEY_STATE, cursor.getString(4));
            user.put(KEY_ZIP, cursor.getString(5));
            // Split the full name
            String[] fullName = user.get(KEY_FULL_NAME).split(" ");
            String firstName = fullName[0];
            String lastName = "";
            for (int i = 1; i < fullName.length - 1; i++) {
                lastName += fullName[i];
            }
            rows.add(new Driver(Integer.parseInt(user.get(KEY_DRIVER_ID)), firstName, lastName, user.get(KEY_STREET), user.get(KEY_CITY), user.get(KEY_STATE), user.get(KEY_ZIP)));
            user.clear();
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
        String countQuery = "SELECT  * FROM " + TABLE_DRIVER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
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
        db.delete(TABLE_DRIVER, null, null);
        db.close();
    }

}

package com.example.android.rowanparkingpass.utilities.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.rowanparkingpass.personinfo.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseHandlerDrivers extends DatabaseHandlerBase {

    // Login table name
    private static final String TABLE_DRIVERS = "drivers";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DRIVER_ID = "driver_id";
    private static final String KEY_FULL_NAME_STR = "full_name_str";
    private static final String KEY_STREET_STR = "street_str";
    private static final String KEY_CITY_STR = "city_str";
    private static final String KEY_STATE_STR = "state_str";
    private static final String KEY_ZIP_INT = "zip_int";

    public DatabaseHandlerDrivers(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITOR_TABLE = "CREATE TABLE " + TABLE_DRIVERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DRIVER_ID + " TEXT,"
                + KEY_FULL_NAME_STR + " TEXT,"
                + KEY_STREET_STR + " TEXT,"
                + KEY_CITY_STR + " TEXT,"
                + KEY_STATE_STR + " TEXT,"
                + KEY_ZIP_INT + " TEXT" + ")";
        db.execSQL(CREATE_VISITOR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing visitor details in database
     */
    public void addVistor(String fname, String lname, String street, String city, String state, String zip) {
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DRIVER_ID, uid); // UID
        values.put(KEY_FULL_NAME_STR, fname); // Name
        values.put(KEY_STREET_STR, street); // Street
        values.put(KEY_CITY_STR, city); // City
        values.put(KEY_STATE_STR, state); // State
        values.put(KEY_ZIP_INT, zip); // Zip
        // Inserting Row
        db.insert(TABLE_DRIVERS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update visitor details in database
     */
    public void updateUser(String uid, String fname, String lname, String street, String city, String state, String zip) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME_STR, fname); // Name
        values.put(KEY_STREET_STR, street); // Street
        values.put(KEY_CITY_STR, city); // City
        values.put(KEY_STATE_STR, state); // State
        values.put(KEY_ZIP_INT, zip); // Zip
        // Update Row
        db.update(TABLE_DRIVERS, values, KEY_DRIVER_ID + "=" + uid, null);
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Driver> getVisitors() {
        ArrayList<Driver> rows = new ArrayList<Driver>();
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_DRIVERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user.put(KEY_DRIVER_ID, cursor.getString(1));
            user.put(KEY_FULL_NAME_STR, cursor.getString(2));
            user.put(KEY_STREET_STR, cursor.getString(3));
            user.put(KEY_CITY_STR, cursor.getString(4));
            user.put(KEY_STATE_STR, cursor.getString(5));
            user.put(KEY_ZIP_INT, cursor.getString(6));
            rows.add(new Driver(user.get(KEY_DRIVER_ID), user.get(KEY_FULL_NAME_STR), user.get(KEY_STREET_STR), user.get(KEY_CITY_STR), user.get(KEY_STATE_STR), user.get(KEY_ZIP_INT)));
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
        String countQuery = "SELECT  * FROM " + TABLE_DRIVERS;
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
        db.delete(TABLE_DRIVERS, null, null);
        db.close();
    }

}

package com.example.android.rowanparkingpass.utilities.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerVehicles extends DatabaseHandlerBase {

    // Login table name
    private static final String TABLE_VISITORS = "vehicles";

    // Login Table Columns names
    private static final String KEY_VEHICLE_ID = "vehicle_id";
    private static final String KEY_YEAR_INT = "year_int";
    private static final String KEY_MAKE_STR = "make_str";
    private static final String KEY_MODEL_STR = "model_str";
    private static final String KEY_STATE_ENUM = "state_enum";
    private static final String KEY_COLOR_STR = "color_str";
    private static final String KEY_LICENSE_STR = "license_str";

    public DatabaseHandlerVehicles(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITOR_TABLE = "CREATE TABLE " + TABLE_VISITORS + "("
                + KEY_VEHICLE_ID + " INTEGER PRIMARY KEY,"
                + KEY_YEAR_INT + " TEXT,"
                + KEY_MAKE_STR + " TEXT,"
                + KEY_MODEL_STR + " TEXT,"
                + KEY_STATE_ENUM + " TEXT,"
                + KEY_COLOR_STR + " TEXT,"
                + KEY_LICENSE_STR + " TEXT UNIQUE"  + ")";
        db.execSQL(CREATE_VISITOR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITORS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing visitor details in database
     */
    public void addVehicle(String year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YEAR_INT, year); // Car Year
        values.put(KEY_MAKE_STR, make); // Car Make
        values.put(KEY_MODEL_STR, model); // Car Model
        values.put(KEY_STATE_ENUM, state); // Car State
        values.put(KEY_COLOR_STR, color); // Car Color
        values.put(KEY_LICENSE_STR, license); // Car License Plate
        // Inserting Row
        db.insert(TABLE_VISITORS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update visitor details in database
     */
    public void updateUser(String year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YEAR_INT, year); // Car Year
        values.put(KEY_MAKE_STR, make); // Car Make
        values.put(KEY_MODEL_STR, model); // Car Model
        values.put(KEY_STATE_ENUM, state); // Car State
        values.put(KEY_COLOR_STR, color); // Car Color
        // Update Row
        db.update(TABLE_VISITORS, values, KEY_LICENSE_STR + "=" + license, null);
    }

    /**
     * Getting user data from database
     */
    public ArrayList<HashMap<String, String>> getVehicles() {
        ArrayList<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> vehicle = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_VISITORS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            vehicle.put(KEY_YEAR_INT, cursor.getString(1));
            vehicle.put(KEY_MAKE_STR, cursor.getString(2));
            vehicle.put(KEY_MODEL_STR, cursor.getString(3));
            vehicle.put(KEY_STATE_ENUM, cursor.getString(4));
            vehicle.put(KEY_COLOR_STR, cursor.getString(5));
            vehicle.put(KEY_LICENSE_STR, cursor.getString(6));
            rows.add(vehicle);
            vehicle.clear();
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
        String countQuery = "SELECT  * FROM " + TABLE_VISITORS;
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
        db.delete(TABLE_VISITORS, null, null);
        db.close();
    }

}

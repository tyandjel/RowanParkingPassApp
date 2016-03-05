package com.example.android.rowanparkingpass.utilities.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerVehicles extends DatabaseHandlerBase {

    // Login table name
    private static final String TABLE_VEHICLES = "vehicles";

    // Login Table Columns names
    public static final String KEY_VEHICLE_ID = "vehicle_id";
    public static final String KEY_MAKE = "make";
    public static final String KEY_MODEL = "model";
    public static final String KEY_YEAR = "year";
    public static final String KEY_STATE = "state";
    public static final String KEY_COLOR = "color";
    public static final String KEY_LICENSE = "license";

    public DatabaseHandlerVehicles(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITOR_TABLE = "CREATE TABLE " + TABLE_VEHICLES + "("
                + KEY_VEHICLE_ID + " INTEGER PRIMARY KEY,"
                + KEY_MAKE + " TEXT,"
                + KEY_MODEL + " TEXT,"
                + KEY_YEAR + " INTEGER,"
                + KEY_STATE + " TEXT,"
                + KEY_COLOR + " INTEGER,"
                + KEY_LICENSE + " TEXT UNIQUE" + ")";
        db.execSQL(CREATE_VISITOR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing vehicle details in database
     */
    public void addVehicle(String year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAKE, make); // Car Make
        values.put(KEY_MODEL, model); // Car Model
        values.put(KEY_YEAR, year); // Car Year
        values.put(KEY_STATE, state); // Car State
        values.put(KEY_COLOR, color); // Car Color
        values.put(KEY_LICENSE, license); // Car License Plate
        // Inserting Row
        db.insert(TABLE_VEHICLES, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update vehicle details in database
     */
    public void updateVehicle(String year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAKE, make); // Car Make
        values.put(KEY_MODEL, model); // Car Model
        values.put(KEY_YEAR, year); // Car Year
        values.put(KEY_STATE, state); // Car State
        values.put(KEY_COLOR, color); // Car Color
        // Update Row
        db.update(TABLE_VEHICLES, values, KEY_LICENSE + "=" + license, null);
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> rows = new ArrayList<Vehicle>();
        HashMap<String, String> vehicle = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_VEHICLES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            vehicle.put(KEY_VEHICLE_ID, cursor.getString(0));
            vehicle.put(KEY_MAKE, cursor.getString(1));
            vehicle.put(KEY_MODEL, cursor.getString(2));
            vehicle.put(KEY_YEAR, cursor.getString(3));
            vehicle.put(KEY_STATE, cursor.getString(4));
            vehicle.put(KEY_COLOR, cursor.getString(5));
            vehicle.put(KEY_LICENSE, cursor.getString(6));
            rows.add(new Vehicle(Integer.parseInt(vehicle.get(KEY_VEHICLE_ID)), vehicle.get(KEY_MAKE),
                    vehicle.get(KEY_MODEL), Integer.parseInt(vehicle.get(KEY_YEAR)), vehicle.get(KEY_STATE),
                    vehicle.get(KEY_COLOR), vehicle.get(KEY_LICENSE)));
            vehicle.clear();
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return vehicle
        return rows;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VEHICLES;
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
        db.delete(TABLE_VEHICLES, null, null);
        db.close();
    }

}

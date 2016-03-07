package com.example.android.rowanparkingpass.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerVehicles extends DatabaseHandlerBase {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VehicleContract.VehicleEntry.TABLE_NAME + " (" +
                    VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID + VehicleContract.INTEGER_TYPE + " PRIMARY KEY," +
                    VehicleContract.VehicleEntry.COLUMN_MAKE + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_MODEL + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_YEAR + VehicleContract.INTEGER_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_STATE + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_COLOR + VehicleContract.TEXT_TYPE + VehicleContract.COMMA_SEP +
                    VehicleContract.VehicleEntry.COLUMN_LICENSE + VehicleContract.TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VehicleContract.VehicleEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + VehicleContract.VehicleEntry.TABLE_NAME;

    public DatabaseHandlerVehicles(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
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
     * Storing vehicle details in database
     */
    public void addVehicle(int vehicleId, int year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID, vehicleId); // Vehicle Id
        values.put(VehicleContract.VehicleEntry.COLUMN_MAKE, make); // Car Make
        values.put(VehicleContract.VehicleEntry.COLUMN_MODEL, model); // Car Model
        values.put(VehicleContract.VehicleEntry.COLUMN_YEAR, year); // Car Year
        values.put(VehicleContract.VehicleEntry.COLUMN_STATE, state); // Car State
        values.put(VehicleContract.VehicleEntry.COLUMN_COLOR, color); // Car Color
        values.put(VehicleContract.VehicleEntry.COLUMN_LICENSE, license); // Car License Plate
        // Inserting Row
        db.insert(VehicleContract.VehicleEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Update vehicle details in database
     */
    public void updateVehicle(int vehicleId, int year, String make, String model, String state, String color, String license) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(VehicleContract.VehicleEntry.COLUMN_MAKE, make); // Car Make
        values.put(VehicleContract.VehicleEntry.COLUMN_MODEL, model); // Car Model
        values.put(VehicleContract.VehicleEntry.COLUMN_YEAR, year); // Car Year
        values.put(VehicleContract.VehicleEntry.COLUMN_STATE, state); // Car State
        values.put(VehicleContract.VehicleEntry.COLUMN_COLOR, color); // Car Color
        values.put(VehicleContract.VehicleEntry.COLUMN_LICENSE, license); // Car License Plate
        // Update Row
        db.update(VehicleContract.VehicleEntry.TABLE_NAME, values, VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID + "=" + vehicleId, null);
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> rows = new ArrayList<Vehicle>();
        HashMap<String, String> vehicle = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID, cursor.getString(0)); // Vehicle Id
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_MAKE, cursor.getString(1)); // Car Make
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_MODEL, cursor.getString(2)); // Car Model
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_YEAR, cursor.getString(3)); // Car Year
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_STATE, cursor.getString(4)); // Car State
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_COLOR, cursor.getString(5)); // Car Color
            vehicle.put(VehicleContract.VehicleEntry.COLUMN_LICENSE, cursor.getString(6)); // Car License Plate
            rows.add(new Vehicle(Integer.parseInt(vehicle.get(VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID)),
                    vehicle.get(VehicleContract.VehicleEntry.COLUMN_MAKE),
                    vehicle.get(VehicleContract.VehicleEntry.COLUMN_MODEL),
                    Integer.parseInt(vehicle.get(VehicleContract.VehicleEntry.COLUMN_YEAR)),
                    vehicle.get(VehicleContract.VehicleEntry.COLUMN_STATE),
                    vehicle.get(VehicleContract.VehicleEntry.COLUMN_COLOR),
                    vehicle.get(VehicleContract.VehicleEntry.COLUMN_LICENSE)));
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
        db.delete(VehicleContract.VehicleEntry.TABLE_NAME, null, null);
        db.close();
    }

}

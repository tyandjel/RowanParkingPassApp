package com.example.android.rowanparkingpass.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandlerPasses extends DatabaseHandlerBase {

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PassContract.PassEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + PassContract.PassEntry.TABLE_NAME;
    private static final String SQL_SELECT_DRIVER =
            "SELECT * FROM " + DriverContract.DriverEntry.TABLE_NAME +
                    " WHERE " + DriverContract.DriverEntry.COLUMN_DRIVER_ID +
                    " = " + PassContract.PassEntry.COLUMN_DRIVER_ID;
    private static final String SQL_SELECT_VEHICLE =
            "SELECT * FROM " + VehicleContract.VehicleEntry.TABLE_NAME +
                    " WHERE " + VehicleContract.VehicleEntry.COLUMN_VEHICLE_ID +
                    " = " + PassContract.PassEntry.COLUMN_VEHICLE_ID;

    public DatabaseHandlerPasses(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PASS_ENTRIES);
        db.execSQL(SQL_CREATE_DRIVER_ENTRIES);
        db.execSQL(SQL_CREATE_VEHICLE_ENTRIES);
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
     * Storing pass details in database
     */
    public void addRequest(int requestId, int vehicleId, int driverId, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PassContract.PassEntry.COLUMN_REQUEST_ID, requestId); // Request Id
        values.put(PassContract.PassEntry.COLUMN_VEHICLE_ID, vehicleId); // Vehicle Id
        values.put(PassContract.PassEntry.COLUMN_DRIVER_ID, driverId); // Driver Id
        values.put(PassContract.PassEntry.COLUMN_START_DATE, startDate); // Start Date
        values.put(PassContract.PassEntry.COLUMN_END_DATE, endDate); // End Date
        // Inserting Row
        db.insert(PassContract.PassEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting pass data from database
     */
    public ArrayList<Pass> getRequestDetails() {
        ArrayList<Pass> rows = new ArrayList<>();
        HashMap<String, String> pass = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            pass.put(PassContract.PassEntry.COLUMN_REQUEST_ID, cursor.getString(0));
            pass.put(PassContract.PassEntry.COLUMN_VEHICLE_ID, cursor.getString(1));
            pass.put(PassContract.PassEntry.COLUMN_DRIVER_ID, cursor.getString(2));
            pass.put(PassContract.PassEntry.COLUMN_START_DATE, cursor.getString(4));
            pass.put(PassContract.PassEntry.COLUMN_END_DATE, cursor.getString(5));

            HashMap<String, String> obj = new HashMap<>();
            // Get Driver Info

            SQLiteDatabase driverDB = this.getReadableDatabase();
            Cursor cursorDriver = driverDB.rawQuery(SQL_SELECT_DRIVER, null);
            cursorDriver.moveToFirst();
            String firstName = "";
            String lastName = "";
            if (!cursor.isAfterLast()) {
                obj.put(DriverContract.DriverEntry.COLUMN_FULL_NAME, cursor.getString(1));
                obj.put(DriverContract.DriverEntry.COLUMN_STREET, cursor.getString(2));
                obj.put(DriverContract.DriverEntry.COLUMN_CITY, cursor.getString(3));
                obj.put(DriverContract.DriverEntry.COLUMN_STATE, cursor.getString(4));
                obj.put(DriverContract.DriverEntry.COLUMN_ZIP, cursor.getString(5));
                // Split the full name
                String[] fullName = obj.get(DriverContract.DriverEntry.COLUMN_FULL_NAME).split(" ");
                firstName = fullName[0];
                for (int i = 1; i < fullName.length - 1; i++) {
                    lastName += fullName[i];
                }
            }
            Driver d = new Driver(Integer.parseInt(PassContract.PassEntry.COLUMN_DRIVER_ID), firstName, lastName,
                    obj.get(DriverContract.DriverEntry.COLUMN_STREET),
                    obj.get(DriverContract.DriverEntry.COLUMN_CITY),
                    obj.get(DriverContract.DriverEntry.COLUMN_STATE),
                    obj.get(DriverContract.DriverEntry.COLUMN_ZIP));
            obj.clear();
            cursorDriver.close();
            driverDB.close();

            // Get Vehicle Info

            SQLiteDatabase vehicleDB = this.getReadableDatabase();
            Cursor cursorVehicle = vehicleDB.rawQuery(SQL_SELECT_VEHICLE, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                obj.put(VehicleContract.VehicleEntry.COLUMN_MAKE, cursor.getString(1));
                obj.put(VehicleContract.VehicleEntry.COLUMN_MODEL, cursor.getString(2));
                obj.put(VehicleContract.VehicleEntry.COLUMN_YEAR, cursor.getString(3));
                obj.put(VehicleContract.VehicleEntry.COLUMN_STATE, cursor.getString(4));
                obj.put(VehicleContract.VehicleEntry.COLUMN_COLOR, cursor.getString(5));
                obj.put(VehicleContract.VehicleEntry.COLUMN_LICENSE, cursor.getString(6));
            }
            Vehicle v = new Vehicle(Integer.parseInt(PassContract.PassEntry.COLUMN_VEHICLE_ID),
                    obj.get(VehicleContract.VehicleEntry.COLUMN_MAKE),
                    obj.get(VehicleContract.VehicleEntry.COLUMN_MODEL),
                    Integer.parseInt(obj.get(VehicleContract.VehicleEntry.COLUMN_YEAR)),
                    obj.get(VehicleContract.VehicleEntry.COLUMN_STATE),
                    obj.get(VehicleContract.VehicleEntry.COLUMN_COLOR),
                    obj.get(VehicleContract.VehicleEntry.COLUMN_LICENSE));
            obj.clear();
            cursorVehicle.close();
            vehicleDB.close();

            rows.add(new Pass(d, v, pass.get(PassContract.PassEntry.COLUMN_START_DATE), pass.get(PassContract.PassEntry.COLUMN_END_DATE)));
            pass.clear();
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return passes
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
        db.delete(PassContract.PassEntry.TABLE_NAME, null, null);
        db.close();
    }
}

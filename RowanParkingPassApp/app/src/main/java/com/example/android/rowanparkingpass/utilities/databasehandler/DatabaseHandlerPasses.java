package com.example.android.rowanparkingpass.utilities.databasehandler;

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

    // Login table name
    private static final String TABLE_REQUESTS = "Requests";

    // Login Table Columns names
    private static final String KEY_REQUEST_ID = "request_id";
    private static final String KEY_VEHICLE_ID = "vehicle_id";
    private static final String KEY_DRIVER_ID = "driver_id";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";

    public DatabaseHandlerPasses(Context context) {
        super(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_REQUESTS + "("
                + KEY_REQUEST_ID + " INTEGER PRIMARY KEY,"
                + KEY_VEHICLE_ID + " INTEGER,"
                + KEY_DRIVER_ID + " INTEGER,"
                + KEY_START_DATE + " DATE,"
                + KEY_END_DATE + " DATE"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing pass details in database
     */
    public void addRequest(int requestId, int vehicleId, int driverId, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REQUEST_ID, requestId); // Request Id
        values.put(KEY_VEHICLE_ID, vehicleId); // Vehicle Id
        values.put(KEY_DRIVER_ID, driverId); // Driver Id
        values.put(KEY_START_DATE, startDate); // Start Date
        values.put(KEY_END_DATE, endDate); // End Date
        // Inserting Row
        db.insert(TABLE_REQUESTS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting pass data from database
     */
    public ArrayList<Pass> getRequestDetails() {
        ArrayList<Pass> rows = new ArrayList<>();
        HashMap<String, String> pass = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_REQUESTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            pass.put(KEY_REQUEST_ID, cursor.getString(0));
            pass.put(KEY_VEHICLE_ID, cursor.getString(1));
            pass.put(KEY_DRIVER_ID, cursor.getString(2));
            pass.put(KEY_START_DATE, cursor.getString(4));
            pass.put(KEY_END_DATE, cursor.getString(5));

            HashMap<String, String> obj = new HashMap<>();
            // Get Driver Info
            String selectDriver = "SELECT * FROM " + DatabaseHandlerDrivers.DATABASE_NAME +
                    " WHERE " + DatabaseHandlerDrivers.KEY_DRIVER_ID + "=" + KEY_DRIVER_ID;
            SQLiteDatabase driverDB = this.getReadableDatabase();
            Cursor cursorDriver = driverDB.rawQuery(selectDriver, null);
            cursorDriver.moveToFirst();
            String firstName = "";
            String lastName = "";
            if (!cursor.isAfterLast()) {
                obj.put(DatabaseHandlerDrivers.KEY_FULL_NAME, cursor.getString(1));
                obj.put(DatabaseHandlerDrivers.KEY_STREET, cursor.getString(2));
                obj.put(DatabaseHandlerDrivers.KEY_CITY, cursor.getString(3));
                obj.put(DatabaseHandlerDrivers.KEY_STATE, cursor.getString(4));
                obj.put(DatabaseHandlerDrivers.KEY_ZIP, cursor.getString(5));
                // Split the full name
                String[] fullName = obj.get(DatabaseHandlerDrivers.KEY_FULL_NAME).split(" ");
                firstName = fullName[0];
                for (int i = 1; i < fullName.length - 1; i++) {
                    lastName += fullName[i];
                }
            }
            Driver d = new Driver(Integer.parseInt(KEY_DRIVER_ID), firstName, lastName,
                    obj.get(DatabaseHandlerDrivers.KEY_STREET),
                    obj.get(DatabaseHandlerDrivers.KEY_CITY),
                    obj.get(DatabaseHandlerDrivers.KEY_STATE),
                    obj.get(DatabaseHandlerDrivers.KEY_ZIP));
            obj.clear();
            cursorDriver.close();
            driverDB.close();

            // Get Vehicle Info
            String selectVehicle = "SELECT * FROM " + DatabaseHandlerVehicles.DATABASE_NAME +
                    " WHERE " + DatabaseHandlerVehicles.KEY_VEHICLE_ID + "=" + KEY_VEHICLE_ID;
            SQLiteDatabase vehicleDB = this.getReadableDatabase();
            Cursor cursorVehicle = vehicleDB.rawQuery(selectDriver, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                obj.put(DatabaseHandlerVehicles.KEY_MAKE, cursor.getString(1));
                obj.put(DatabaseHandlerVehicles.KEY_MODEL, cursor.getString(2));
                obj.put(DatabaseHandlerVehicles.KEY_YEAR, cursor.getString(3));
                obj.put(DatabaseHandlerVehicles.KEY_STATE, cursor.getString(4));
                obj.put(DatabaseHandlerVehicles.KEY_COLOR, cursor.getString(5));
                obj.put(DatabaseHandlerVehicles.KEY_LICENSE, cursor.getString(6));
            }
            Vehicle v = new Vehicle(Integer.parseInt(KEY_VEHICLE_ID),
                    obj.get(DatabaseHandlerVehicles.KEY_MAKE),
                    obj.get(DatabaseHandlerVehicles.KEY_MODEL),
                    Integer.parseInt(obj.get(DatabaseHandlerVehicles.KEY_YEAR)),
                    obj.get(DatabaseHandlerVehicles.KEY_STATE),
                    obj.get(DatabaseHandlerVehicles.KEY_COLOR),
                    obj.get(DatabaseHandlerVehicles.KEY_LICENSE));
            obj.clear();
            cursorVehicle.close();
            vehicleDB.close();

            rows.add(new Pass(d, v, pass.get(KEY_START_DATE), pass.get(KEY_END_DATE)));
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
        String countQuery = "SELECT  * FROM " + TABLE_REQUESTS;
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
        db.delete(TABLE_REQUESTS, null, null);
        db.close();
    }
}

package com.example.android.rowanparkingpass.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Database handler passes
 */
public class DatabaseHandlerPasses extends DatabaseHandlerBase {

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PassContract.PassEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + PassContract.PassEntry.TABLE_NAME;

    Context context;

    public DatabaseHandlerPasses(Context context) {
        super(context);
        this.context = context;
    }

    // Creating Tables (pass, driver, vehicle)
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

    public ArrayList<Pass> getPasses() {
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
            pass.put(PassContract.PassEntry.COLUMN_START_DATE, cursor.getString(3));
            pass.put(PassContract.PassEntry.COLUMN_END_DATE, cursor.getString(4));

            Log.d("TAG", "BEFORE D");
            // Get driver
            Driver d = new DatabaseHandlerDrivers(context).getDriver(pass.get(PassContract.PassEntry.COLUMN_DRIVER_ID));
            Log.d("TAG", "BEFORE V");
            // Get vehicle
            Vehicle v = new DatabaseHandlerVehicles(context).getVehicle(pass.get(PassContract.PassEntry.COLUMN_VEHICLE_ID));
            Log.d("TAG", "BEFORE R");
            rows.add(new Pass(Integer.parseInt(pass.get(PassContract.PassEntry.COLUMN_REQUEST_ID)), d, v, pass.get(PassContract.PassEntry.COLUMN_START_DATE), pass.get(PassContract.PassEntry.COLUMN_END_DATE)));
            pass.clear();
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return passes
        return rows;
    }

    /**
     * Storing pass details in database
     */
    public void addRequest(int vehicleId, int driverId, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PassContract.PassEntry.COLUMN_VEHICLE_ID, vehicleId); // Vehicle Id
        values.put(PassContract.PassEntry.COLUMN_DRIVER_ID, driverId); // Driver Id
        values.put(PassContract.PassEntry.COLUMN_START_DATE, startDate); // Start Date
        values.put(PassContract.PassEntry.COLUMN_END_DATE, endDate); // End Date
        // Inserting Row
        db.insert(PassContract.PassEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Delete pass from database based on requestID
     *
     * @param requestID the request id
     */
    public void deleteRequestRequestID(String requestID) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Delete Row
        db.delete(PassContract.PassEntry.TABLE_NAME, PassContract.PassEntry.COLUMN_REQUEST_ID + "=" + requestID, null);
    }

    /**
     * Delete pass from database based on driverID
     *
     * @param driverID the driver id
     */
    public void deleteRequestDriverID(String driverID) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Delete Row
        db.delete(PassContract.PassEntry.TABLE_NAME, PassContract.PassEntry.COLUMN_DRIVER_ID + "=" + driverID, null);

    }

    /**
     * Delete pass from database based on vehicle id
     *
     * @param vehicleID the vehicle id
     */
    public void deleteRequestVehicleID(String vehicleID) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Delete Row
        db.delete(PassContract.PassEntry.TABLE_NAME, PassContract.PassEntry.COLUMN_VEHICLE_ID + "=" + vehicleID, null);

    }

    /**
     * Delete pass from database based on driver id and vehicle id
     *
     * @param driverID  the driver id
     * @param vehicleID the vehicle id
     */
    public void deleteRequestDriverIDVehicleID(String driverID, String vehicleID) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Delete Row
        db.delete(PassContract.PassEntry.TABLE_NAME,
                PassContract.PassEntry.COLUMN_DRIVER_ID + "=" + driverID + " AND "
                        + PassContract.PassEntry.COLUMN_VEHICLE_ID + "=" + vehicleID, null);
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
            pass.put(PassContract.PassEntry.COLUMN_START_DATE, cursor.getString(3));
            pass.put(PassContract.PassEntry.COLUMN_END_DATE, cursor.getString(4));

            Log.d("TAG", "BEFORE D");
            // Get driver
            Driver d = new DatabaseHandlerDrivers(context).getDriver(pass.get(PassContract.PassEntry.COLUMN_DRIVER_ID));
            Log.d("TAG", "BEFORE V");
            // Get vehicle
            Vehicle v = new DatabaseHandlerVehicles(context).getVehicle(pass.get(PassContract.PassEntry.COLUMN_VEHICLE_ID));
            Log.d("TAG", "BEFORE R");
            rows.add(new Pass(Integer.parseInt(pass.get(PassContract.PassEntry.COLUMN_REQUEST_ID)), d, v, pass.get(PassContract.PassEntry.COLUMN_START_DATE), pass.get(PassContract.PassEntry.COLUMN_END_DATE)));
            pass.clear();
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return passes
        return rows;
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

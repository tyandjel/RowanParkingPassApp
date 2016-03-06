package com.example.android.rowanparkingpass.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class DatabaseHandlerUser extends DatabaseHandlerBase {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry.COLUMN_USER_ID + UserContract.INTEGER_TYPE + " PRIMARY KEY," +
                    UserContract.UserEntry.COLUMN_USER_NAME + UserContract.TEXT_TYPE + UserContract.COMMA_SEP +
                    UserContract.UserEntry.COLUMN_IS_ADMIN + UserContract.INTEGER_TYPE + UserContract.COMMA_SEP +
                    UserContract.UserEntry.COLUMN_SYNC + UserContract.INTEGER_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + UserContract.UserEntry.TABLE_NAME;

    public DatabaseHandlerUser(Context context) {
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
     * Storing logged in user details in database
     */
    public void addUser(String userId, String userName, int isAdmin, int sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_ID, userId); // User ID
        values.put(UserContract.UserEntry.COLUMN_USER_NAME, userName); // User Name
        values.put(UserContract.UserEntry.COLUMN_IS_ADMIN, isAdmin); // is an admin?
        values.put(UserContract.UserEntry.COLUMN_SYNC, sync); // sync enabled?
        // Inserting Row
        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(UserContract.UserEntry.COLUMN_USER_ID, cursor.getString(0)); // User ID
            user.put(UserContract.UserEntry.COLUMN_USER_NAME, cursor.getString(1)); // User Name
            user.put(UserContract.UserEntry.COLUMN_IS_ADMIN, cursor.getString(2)); // is an admin?
            user.put(UserContract.UserEntry.COLUMN_SYNC, cursor.getString(3)); // sync enabled?
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    public boolean isUserAdmin() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);
        // Move to first row
        cursor.moveToFirst();
        // If there is anything in the database the isAdmin is equal to 1 (true)
        if (cursor.getCount() > 0 && cursor.getString(2).equals("1")) {
            return true;
        }
        return false;
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
        db.delete(UserContract.UserEntry.TABLE_NAME, null, null);
        db.close();
    }

}

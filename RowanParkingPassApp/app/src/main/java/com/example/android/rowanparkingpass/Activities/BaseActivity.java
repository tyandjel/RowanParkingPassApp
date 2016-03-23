package com.example.android.rowanparkingpass.Activities;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public enum mode {
        HOME_PAGE,
        DRIVERS,
        VEHICLES,
        DRIVERS_LIST,
        VEHICLES_LIST,
        FORGOT_PASSWORD,
        CHANGE_PASSWORD,
        UPDATE_VEHICLE,
        UPDATE_DRIVER,
        CREATE_PASS,
        CREATE_VEHICLE,
        CREATE_DRIVER
    }

    public static final String TAG = "tag";
    public static final String MODE = "mode";

    public static String currentMode;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
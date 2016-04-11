package com.example.android.rowanparkingpass.Activities;

import android.app.ProgressDialog;
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
        UPDATE_PASS_VEHICLE,
        UPDATE_DRIVER,
        UPDATE_PASS_DRIVER,
        UPDATE_PASS_DRIVERS,
        CREATE_PASS,
        CREATE_VEHICLE,
        CREATE_DRIVER,
        SETTINGS,
        LOGIN,
        PASS_SEARCH
    }

    public static final String TAG = "tag";
    public static final String MODE = "mode";
    public static String USER = "";
    public static String COOKIE = "";
    public ProgressDialog nDialog;

    public static String currentMode;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showNetworkDialog() {

        nDialog = new ProgressDialog(this);
        nDialog.setTitle("Checking Network");
        nDialog.setMessage("Loading..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

    }

    public void dismissNetworkDialog() {
        nDialog.dismiss();
    }
}
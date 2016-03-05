package com.example.android.rowanparkingpass;

import android.app.Activity;

public abstract class BaseActivity extends Activity {

    public enum mode {HOME_PAGE, DRIVERS, VEHICLES, DRIVERS_LIST, VEHICLES_LIST, FORGOT_PASSWORD, CHANGE_PASSWORD, UPDATE_VEHICLE, UPDATE_DRIVER}

    public static final String TAG = "tag";
    public static final String MODE = "mode";

    public static String currentMode;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

}

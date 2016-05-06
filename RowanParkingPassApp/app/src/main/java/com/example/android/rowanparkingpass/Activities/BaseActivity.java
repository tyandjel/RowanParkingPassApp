package com.example.android.rowanparkingpass.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.utilities.SavedData.ReadWrite;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveUser;

public abstract class BaseActivity extends AppCompatActivity {

    // Enum of modes
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
    public static final String SYNC = "sync";
    public static String USER = null;
    public static String COOKIE = "";
    public ProgressDialog nDialog;
    public SaveUser saveUser;
    protected String user = "";


    public static Context context;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        readInSavedData();

        context = getApplicationContext();
    }

    public void onPause() {
        super.onPause();
        writeOutData();

    }

    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Saves data to phone
     */
    public void writeOutData() {
        try {
            saveUser = new SaveUser(SaveData.getUSR(), SaveData.getSync(), SaveData.getQueue());
            ReadWrite.WRITE_OUT(saveUser, this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read saved data from phone
     */
    public void readInSavedData() {
        try {

            saveUser = ReadWrite.READ_IN(this.getApplicationContext());
            SaveData.setQueue(saveUser.getSendInfos());
            SaveData.setSync(saveUser.isSync());
            SaveData.setUSR(saveUser.getUSR());
            if (saveUser != null) {
                user = saveUser.getUSR();
            }
        } catch (Exception e) {
            e.printStackTrace();// not able to read in note
        }
    }

    /*
    overrides the default transition to have the the new activity move in from the right to the left while the old one fades out
     */
    public void leftToRightTransition() {
        overridePendingTransition(R.anim.slide_left, R.anim.fadeout);
    }

    public void dismissNetworkDialog() {
        nDialog.dismiss();
    }
}
package com.example.android.rowanparkingpass.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.example.android.rowanparkingpass.SavedData.ReadWrite;
import com.example.android.rowanparkingpass.SavedData.SaveData;
import com.example.android.rowanparkingpass.SavedData.SaveUser;

import java.io.IOException;

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
    public static final String SYNC = "sync";
    public static String USER = "";
    public static String COOKIE = "";
    public ProgressDialog nDialog;
    public SaveUser saveUser;
    protected String user="";


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
    public void onResume(){
        super.onResume();
        readInSavedData();

        context = getApplicationContext();
    }

public void onPause(){
    super.onPause();
    writeOutData();

}
    public void onDestroy() {
        super.onDestroy();



    }
    public void writeOutData(){
        try {
            saveUser = new SaveUser(SaveData.getUSR(),SaveData.getSync(),SaveData.getQueue());
            ReadWrite.WRITE_OUT(saveUser, this.getApplicationContext());
            Toast.makeText(getApplicationContext(),
                    SaveData.size()+""
                    , Toast.LENGTH_SHORT).show();        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readInSavedData() {
        try {

            saveUser = ReadWrite.READ_IN(this.getApplicationContext());
            SaveData.setQueue(saveUser.getSendInfos());
            SaveData.setSync(saveUser.isSync());
            SaveData.setUSR(saveUser.getUSR());
            if(saveUser!=null) {
                user = saveUser.getUSR();
                Toast.makeText(getApplicationContext(),
                        SaveData.size()+""
                        , Toast.LENGTH_SHORT).show();
            }
            // this will load the contents of the first note in the notepad.
        }
        catch (Exception e) {
            e.printStackTrace();// not able to read in note
        }
    }

    public void dismissNetworkDialog() {
        nDialog.dismiss();
    }
}
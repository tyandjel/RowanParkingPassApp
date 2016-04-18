package com.example.android.rowanparkingpass.Tests;

import android.util.Log;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.SavedData.ReadWrite;
import com.example.android.rowanparkingpass.SavedData.SaveData;

import java.io.IOException;

/**
 * Created by johnathan on 4/13/16.
 */
public class ReadWriteTest extends BaseActivity {
    static SaveData saveData;

    public boolean testWrite() {
        SaveData.setSync(false);
        saveData = new SaveData();
        try {
            ReadWrite.writeOut(saveData, ReadWrite.saveDateFile, getApplicationContext());
            return true;
        } catch (IOException e) {
            e.getMessage();
            return false;
        }
    }

    public boolean testRead() {
        boolean result = false;
        try {
            saveData = ReadWrite.readIn(getApplicationContext(), ReadWrite.saveDateFile);
            result = SaveData.getSync() == false;
        } catch (ClassNotFoundException e) {
            e.getMessage();

        } catch (IOException e) {
            e.printStackTrace();

        }
        Log.d("SaveData Snyc", String.valueOf(SaveData.getSync()));
        return result;
    }
}

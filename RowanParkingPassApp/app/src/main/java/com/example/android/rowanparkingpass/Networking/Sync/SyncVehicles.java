package com.example.android.rowanparkingpass.Networking.Sync;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by johnathan on 4/12/16.
 */
public class SyncVehicles {

    public synchronized void sync(Context c) {
        if (SaveData.getSync()) {
            SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
            JSONObject json = sendInfoVehicle.syncVehicles(c);
            JSONArray jsonArray;
            States[] arrayStates = States.values();
            try {
                String s = json.getString("JSONS");
                s = URLDecoder.decode(s);
                Log.d("Sssssss", s);
                jsonArray = new JSONArray(s);
                Log.d("JSON ARRAY", jsonArray.toString());
                DatabaseHandlerVehicles db = new DatabaseHandlerVehicles(c);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //[{"model":"zaz","color":"1","state":"23","user_id":"10","year":"1945","license":"bingling","vehicle_id":"3","make":"me a sammich"}
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    //Log.d("JSONOBJ", jsonObj.toString());
                    String model = jsonObj.getString("model");
                    String color = jsonObj.getString("color");
                    String state = jsonObj.getString("state");
                    String year = jsonObj.getString("year");
                    String license = jsonObj.getString("license");
                    String vehicle_id = jsonObj.getString("vehicle_id");
                    String make = jsonObj.getString("make");
                    try {
                        db.addVehicle(Integer.parseInt(vehicle_id), Integer.parseInt(year), make, model, arrayStates[Integer.parseInt(state)].valueOf(arrayStates[Integer.parseInt(state)].name()).toString(), color, license);
                    } catch (SQLiteConstraintException sqlC) {
                        Log.d("SQLite Exception", sqlC.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

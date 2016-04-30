package com.example.android.rowanparkingpass.Networking.Sync;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by johnathan on 4/12/16.
 */
public class SyncDrivers {

    public synchronized void sync(Context c) {
        if (SaveData.getSync()) {
            SendInfoDriver sendInfoDriver = new SendInfoDriver();
            JSONObject json = sendInfoDriver.syncDrivers(c);
            JSONArray jsonArray;
            States[] arrayStates = States.values();
            try {
                String s = json.getString("JSONS");
                s = URLDecoder.decode(s);
                Log.d("S", s);
                jsonArray = new JSONArray(s);
                Log.d("JSON ARRAY", jsonArray.toString());
                DatabaseHandlerDrivers db = new DatabaseHandlerDrivers(c);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //[{"model":"zaz","color":"1","state":"23","user_id":"10","year":"1945","license":"bingling","vehicle_id":"3","make":"me a sammich"}

                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    //Log.d("JSONOBJ", jsonObj.toString());
                    String driverID = jsonObj.getString("driver_id");
                    String fullName = jsonObj.getString("full_name");
                    String street = jsonObj.getString("street");
                    String city = jsonObj.getString("city");
                    String state = jsonObj.getString("state");
                    String zip = Utilities.appendZipZero(jsonObj.getString("zip"));
                    try {
                        db.addDriver(Integer.parseInt(driverID),Utilities.fakeByte, fullName, street, city, arrayStates[Integer.parseInt(state)].valueOf(arrayStates[Integer.parseInt(state)].name()).toString(), zip);
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

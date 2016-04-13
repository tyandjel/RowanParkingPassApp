package com.example.android.rowanparkingpass.Sync;

import android.content.Context;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by johnathan on 4/12/16.
 */
public class SyncDrivers {

    public void sync(Context c) {
        if (SaveData.getSync()) {
            SendInfoDriver sendInfoDriver = new SendInfoDriver();
            JSONObject json = sendInfoDriver.syncDrivers(c);
            JSONArray jsonArray;
            try {
                String s = (String) json.get("JSONS");
                s = URLDecoder.decode(s);
                Log.d("S", s);
                jsonArray = new JSONArray(s);
                Log.d("JSON ARRAY", jsonArray.toString());
                DatabaseHandlerDrivers db = new DatabaseHandlerDrivers(c);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //[{"model":"zaz","color":"1","state":"23","user_id":"10","year":"1945","license":"bingling","vehicle_id":"3","make":"me a sammich"}
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    //Log.d("JSONOBJ", jsonObj.toString());
                    String driverID = jsonObj.getString("driver_id");
                    String fullName = jsonObj.getString("full_name");
                    String street = jsonObj.getString("street");
                    String city = jsonObj.getString("city");
                    String state = jsonObj.getString("state");
                    String zip = jsonObj.getString("zip");
                    db.addDriver(Integer.parseInt(driverID), fullName, street, city, state, zip);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

package com.example.android.rowanparkingpass.Sync;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.Networking.SendToServer;
import com.example.android.rowanparkingpass.SavedDate.SaveData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by johnathan on 4/12/16.
 */
public class SyncVehicles {

    public void sync() {


                @Override
                protected JSONObject doInBackground(Void... params){
            SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
            JSONObject json = sendInfoVehicle.syncVehicles(getApplicationContext());
            JSONArray jsonArray;
            try {
                SaveData.makeSendInfo(new JSONObject(), "dfsd");
                SendToServer sew = new SendToServer();
                /// wait(2000);
                sew.send();
                String s = (String) json.get("JSONS");
                s = URLDecoder.decode(s);
                Log.d("S", s);
                jsonArray = new JSONArray(s);
                Log.d("JSON ARRAY", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    //[{"model":"zaz","color":"1","state":"23","user_id":"10","year":"1945","license":"bingling","vehicle_id":"3","make":"me a sammich"}
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    //Log.d("JSONOBJ", jsonObj.toString());
                    //TODO: FOR SYNC actually call gets and put new vehicles/drivers in database
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    SendInfoDriver sendInfoDriver = new SendInfoDriver();
//                    JSONObject json = sendInfoDriver.syncVehicles(getApplicationContext());
//                    SendInfoPass sendInfoPass = new SendInfoPass();
//                    JSONObject json = sendInfoPass.addPass("1", "1", "01/22/2015", "01/23/2015");
            //Log.d("SESSION: ", json.toString());
           // return json;
        }
    }
}

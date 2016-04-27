package com.example.android.rowanparkingpass.Networking.SendInfo;

import android.content.Context;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendToServer;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SendInfoDriver extends SendInfoBase {

    private static final String MODIFY_DRIVER_URL = IP_ADDRESS_URL + "/modify_driver.php";

    private static final String DRIVER_ID_KEY = ":driver_id";
    private static final String FULL_NAME_KEY = ":full_name";
    private static final String STREET_KEY = ":street";
    private static final String CITY_KEY = ":city";
    private static final String STATE_KEY = ":state";
    private static final String ZIP_KEY = ":zip";

    // constructor
    public SendInfoDriver() {
        super();
    }

    /**
     * Adds a driver to the server side database
     *
     * @param street driver street they live on
     * @param city   driver city they live in
     * @param state  driver state they are from
     * @param zip    driver zip code
     * @return JSONObject of whether driver was added successfully along with driver id
     */
    public void addDriver(int id, String name, String street, String city, String state, String zip) {
        // Return FLAG - true/false
        // Return id
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(FULL_NAME_KEY, name);
        params.put(STREET_KEY, street);
        params.put(CITY_KEY, city);
        params.put(STATE_KEY, state);
        params.put(ZIP_KEY, zip);
        SendInfoModel sendInfoModel = new SendInfoModel(params, MODIFY_DRIVER_URL, id);
        sendInfoModel.setIsDriver();
        SaveData.addSendInfo(sendInfoModel);
        // Return JsonObject
        new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_DRIVER_URL, JSONParser.POST, params);
    }

    /**
     * Update a driver
     *
     * @param driverId vehicle id
     * @param name     driver full name
     * @param street   driver street they live on
     * @param city     driver city they live in
     * @param state    driver state they are from
     * @param zip      driver zip code
     * @return JSONObject of whether driver was updated successfully
     */
    public JSONObject updateDriver(String driverId, String name, String street, String city, String state, String zip) {
        //Return FLAG
        //
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(KILL_KEY, "0");
        params.put(DRIVER_ID_KEY, driverId);
        params.put(FULL_NAME_KEY, name);
        params.put(STREET_KEY, street);
        params.put(CITY_KEY, city);
        params.put(STATE_KEY, state);
        params.put(ZIP_KEY, zip);

        SaveData.makeSendInfo(params, MODIFY_DRIVER_URL);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_DRIVER_URL, JSONParser.POST, params);
    }

    /**
     * Deletes driver associated with driver id
     *
     * @param driverId vehicle id
     * @return JSONObject of whether the driver was deleted successfully
     */
    public JSONObject deleteDriver(String driverId) {
        // Return FLAG - true
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(KILL_KEY, "1");
        params.put(DRIVER_ID_KEY, driverId);


        SaveData.makeSendInfo(params, MODIFY_DRIVER_URL);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_DRIVER_URL, JSONParser.POST, params);
    }

//    /**
//     * Syncs the local database and server side database
//     *
//     * @param userId       user's user id
//     * @param localDrivers list of drivers on phone
//     * @return JSONObject whether drivers were successfully synced and all vehicles associated with the user id
//     */
//    public JSONObject syncVehicles(String userId, ArrayList<Driver> localDrivers) {
//        HashMap<String, String> params = new HashMap<>();
//        JSONObject driverObj = new JSONObject();
//        JSONArray driverArray = new JSONArray();
//
//        params.put(TAG_KEY, SYNC_DRIVERS_TAG);
//        params.put(USER_ID_KEY, userId);
//
//        try {
//            for (int i = 0; i < localDrivers.size(); i++) {
//                JSONObject driver = new JSONObject();
//                driver.put(DRIVER_ID_KEY, localDrivers.get(i).getDriverId());
//                driver.put(FULL_NAME_KEY, localDrivers.get(i).getName());
//                driver.put(STREET_KEY, localDrivers.get(i).getStreet());
//                driver.put(CITY_KEY, localDrivers.get(i).getTown());
//                driver.put(STATE_KEY, localDrivers.get(i).getState());
//                driver.put(ZIP_KEY, localDrivers.get(i).getZipCode());
//                driverArray.put(i, driver);
//            }
//            driverObj.put("app_drivers", driverArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        params.put(DRIVER_LIST_KEY, driverObj.toString());
//        return jsonParser.makeHttpRequest(DRIVER_URL, JSONParser.POST, params);
//    }

    /**
     * Syncs the local database and server side database
     *
     * @return JSONObject whether drivers were successfully synced and all vehicles associated with the user id
     */
    public synchronized JSONObject syncDrivers(Context context) {
        // Send everything to server
        // Get need stuff back
        // Return json array of objects
        final String url = IP_ADDRESS_URL + "/sync_drivers.php";
        DatabaseHandlerDrivers db = new DatabaseHandlerDrivers(context);
        ArrayList<Driver> listOfDriver = db.getDrivers();
        StringBuilder sb = new StringBuilder("[");
        for (Driver d : listOfDriver) {
            sb.append(d.toString());
            sb.append(",");
        }
        String s = "[";
        if (sb.length() > 1) {
            s = sb.substring(0, sb.length() - 1);
        }
        s += "]";
        HashMap<String, String> params = new HashMap<>();
        params.put("json_obj", s);
        Log.d("json_obj", s);

        JSONObject json = new JSONObject(params);
        SaveData.makeSendInfo(params, url);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(url, JSONParser.POST, params);
    }

}

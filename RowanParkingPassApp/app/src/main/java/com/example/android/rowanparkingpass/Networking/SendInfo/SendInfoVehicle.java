package com.example.android.rowanparkingpass.Networking.SendInfo;

import android.content.Context;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendToServer;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SendInfoVehicle extends SendInfoBase {

    private static final String MODIFY_VEHICLE_URL = IP_ADDRESS_URL + "/modify_vehicle.php";

    private static final String VEHICLE_ID_KEY = ":vehicle_id";
    private static final String MAKE_KEY = ":make";
    private static final String MODEL_KEY = ":model";
    private static final String YEAR_KEY = ":year";
    private static final String STATE_KEY = ":state";
    private static final String COLOR_KEY = ":color";
    private static final String LICENSE_KEY = ":license";

    // constructor
    public SendInfoVehicle() {
        super();
    }

    /**
     * Adds a vehicle to the server side database
     *
     * @param make    car make
     * @param model   car model
     * @param year    car year
     * @param state   car state
     * @param color   car color
     * @param license car license plate
     * @return JSONObject of whether vehicle was added successfully along with vehicle id
     */
    public void addVehicle(int id, String make, String model, String year, String state, String color, String license) {
        // Return FLAG - successful?
        // Return id - id of vehicle
        // send state as num - start at 0
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(MAKE_KEY, make);
        params.put(MODEL_KEY, model);
        params.put(YEAR_KEY, year);
        params.put(STATE_KEY, state);
        params.put(COLOR_KEY, color);
        params.put(LICENSE_KEY, license);

        SendInfoModel sendInfoModel = new SendInfoModel(params, MODIFY_VEHICLE_URL, id);
        sendInfoModel.setIsVehicle();
        SaveData.addSendInfo(sendInfoModel);

        new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_VEHICLE_URL, JSONParser.POST, params);
    }

    /**
     * Update a vehicle
     *
     * @param vehicleId vehicle id
     * @param make      vehicle make
     * @param model     vehicle model
     * @param year      vehicle year
     * @param state     vehicle state
     * @param color     vehicle color
     * @param license   vehicle license plate
     * @return JSONObject of whether vehicle was updated successfully
     */
    public JSONObject updateVehicle(String vehicleId, String make, String model, String year, String state, String color, String license) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(KILL_KEY, "0");
        params.put(VEHICLE_ID_KEY, vehicleId);
        params.put(MAKE_KEY, make);
        params.put(MODEL_KEY, model);
        params.put(YEAR_KEY, year);
        params.put(STATE_KEY, state);
        params.put(COLOR_KEY, color);
        params.put(LICENSE_KEY, license);

        SaveData.makeSendInfo(params, MODIFY_VEHICLE_URL);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_VEHICLE_URL, JSONParser.POST, params);
    }

    /**
     * Deletes vehicle associated with vehicle id
     *
     * @param vehicleId vehicle id
     * @return JSONObject of whether the vehicle was deleted successfully
     */
    public JSONObject deleteVehicle(String vehicleId) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(KILL_KEY, "1");
        params.put(VEHICLE_ID_KEY, vehicleId);

        SaveData.makeSendInfo(params, MODIFY_VEHICLE_URL);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(MODIFY_VEHICLE_URL, JSONParser.POST, params);
    }

//    /**
//     * Syncs the local database of vehicles with server side
//     *
//     * @param userId        user's user id
//     * @param localVehicles list of vehicles on phone
//     * @return JSONObject whether vehicles were successfully synced and all vehicles associated with the user id
//     */
//    public JSONObject syncVehicles(String userId, ArrayList<Vehicle> localVehicles) {
//        HashMap<String, String> params = new HashMap<>();
//        JSONObject vehicleObj = new JSONObject();
//        JSONArray vehicleArray = new JSONArray();
//
//        params.put(TAG_KEY, SYNC_VEHICLES_TAG);
//        params.put(USER_ID_KEY, userId);
//
//        try {
//            for (int i = 0; i < localVehicles.size(); i++) {
//                JSONObject vehicle = new JSONObject();
//                vehicle.put(VEHICLE_ID_KEY, localVehicles.get(i).getVehicleId());
//                vehicle.put(MAKE_KEY, localVehicles.get(i).getMake());
//                vehicle.put(MODEL_KEY, localVehicles.get(i).getModel());
//                vehicle.put(YEAR_KEY, localVehicles.get(i).getYear());
//                vehicle.put(STATE_KEY, localVehicles.get(i).getVehicleState());
//                vehicle.put(COLOR_KEY, localVehicles.get(i).getColor());
//                vehicle.put(LICENSE_KEY, localVehicles.get(i).getLicensePlate());
//                vehicleArray.put(i, vehicle);
//            }
//            vehicleObj.put("app_vehicles", vehicleArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        params.put(VEHICLE_LIST_KEY, vehicleObj.toString());
//
//        return jsonParser.makeHttpRequest(VEHICLE_URL, JSONParser.POST, params);
//    }

    /**
     * Syncs the local database of vehicles with server side
     *
     * @return JSONObject whether vehicles were successfully synced and all vehicles associated with the user id
     */
    public synchronized JSONObject syncVehicles(Context context) {
        // Send everything to server
        // Get need stuff back
        // Return json array of objects
        final String url = IP_ADDRESS_URL + "/sync_vehicles.php";
        DatabaseHandlerVehicles db = new DatabaseHandlerVehicles(context);
        ArrayList<Vehicle> listOfVehicles = db.getVehicles();
        StringBuilder sb = new StringBuilder("[");
        for (Vehicle v : listOfVehicles) {
            sb.append(v.toString());
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
        SaveData.makeSendInfo(params, url);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(url, JSONParser.POST, params);
    }

}

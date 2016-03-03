package com.example.android.rowanparkingpass.utilities.userfunctions;

import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFunctionsVehicle extends UserFunctionsBase {

    //URL of the PHP API
    private static final String VEHICLE_URL = IP_ADDRESS_URL + DATABASE_NAME;

    private static final String ADD_VEHICLE_TAG = "add_vehicle";
    private static final String UPDATE_VEHICLE_TAG = "update_vehicle";
    private static final String DELETE_VEHICLE_TAG = "delete_vehicle";
    private static final String SYNC_VEHICLES_TAG = "sync_vehicles";

    private static final String USER_ID_KEY = "user_id";
    private static final String VEHICLE_LIST_KEY = "vehicle_list";
    private static final String VEHICLE_ID_KEY = "vehicle_id";
    private static final String MAKE_KEY = "make";
    private static final String MODEL_KEY = "model";
    private static final String YEAR_KEY = "year";
    private static final String STATE_KEY = "state";
    private static final String COLOR_KEY = "color";
    private static final String LICENSE_KEY = "license";

    // constructor
    public UserFunctionsVehicle() {
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
    public JSONObject addVehicle(String make, String model, String year, String state, String color, String license) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(TAG_KEY, ADD_VEHICLE_TAG);
        params.put(MAKE_KEY, make);
        params.put(MODEL_KEY, model);
        params.put(YEAR_KEY, year);
        params.put(STATE_KEY, state);
        params.put(COLOR_KEY, color);
        params.put(LICENSE_KEY, license);
        // Return JsonObject
        return jsonParser.makeHttpRequest(VEHICLE_URL, JSONParser.POST, params);
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
        params.put(TAG_KEY, UPDATE_VEHICLE_TAG);
        params.put(VEHICLE_ID_KEY, vehicleId);
        params.put(MAKE_KEY, make);
        params.put(MODEL_KEY, model);
        params.put(YEAR_KEY, year);
        params.put(STATE_KEY, state);
        params.put(COLOR_KEY, color);
        params.put(LICENSE_KEY, license);
        // Return JsonObject
        return jsonParser.makeHttpRequest(VEHICLE_URL, JSONParser.POST, params);
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
        params.put(TAG_KEY, DELETE_VEHICLE_TAG);
        params.put(VEHICLE_ID_KEY, vehicleId);
        // Return JsonObject
        return jsonParser.makeHttpRequest(VEHICLE_URL, JSONParser.POST, params);
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
     * @param userId        user's user id
     * @return JSONObject whether vehicles were successfully synced and all vehicles associated with the user id
     */
    public JSONObject syncVehicles(String userId) {
        HashMap<String, String> params = new HashMap<>();

        params.put(TAG_KEY, SYNC_VEHICLES_TAG);
        params.put(USER_ID_KEY, userId);

        return jsonParser.makeHttpRequest(VEHICLE_URL, JSONParser.POST, params);
    }

}

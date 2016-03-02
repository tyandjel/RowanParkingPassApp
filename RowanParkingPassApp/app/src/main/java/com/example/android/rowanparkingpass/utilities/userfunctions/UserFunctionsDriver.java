package com.example.android.rowanparkingpass.utilities.userfunctions;

import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFunctionsDriver extends UserFunctionsBase {

    //URL of the PHP API
    private static final String DRIVER_URL = IP_ADDRESS_URL + DATABASE_NAME;

    private static final String ADD_DRIVER_TAG = "add_driver";
    private static final String UPDATE_DRIVER_TAG = "update_driver";
    private static final String DELETE_DRIVER_TAG = "delete_driver";
    private static final String SYNC_DRIVERS_TAG = "sync_drivers";

    private static final String DRIVER_LIST_KEY = "driver_list";
    private static final String USER_ID_KEY = "user_id";
    private static final String DRIVER_ID_KEY = "driver_id";
    private static final String FULL_NAME_KEY = "full_name";
    private static final String STREET_KEY = "street";
    private static final String CITY_KEY = "city";
    private static final String STATE_KEY = "state";
    private static final String ZIP_KEY = "zip";

    // constructor
    public UserFunctionsDriver() {
        super();
    }

    /**
     * Adds a driver to the server side database
     *
     * @param firstName first name
     * @param lastName  last name
     * @param street    driver street they live on
     * @param city      driver city they live in
     * @param state     driver state they are from
     * @param zip       driver zip code
     * @return JSONObject of whether driver was added successfully along with driver id
     */
    public JSONObject addDriver(String firstName, String lastName, String street, String city, String state, String zip) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(TAG_KEY, ADD_DRIVER_TAG);
        params.put(FULL_NAME_KEY, firstName + " " + lastName);
        params.put(STREET_KEY, street);
        params.put(CITY_KEY, city);
        params.put(STATE_KEY, state);
        params.put(ZIP_KEY, zip);
        // Return JsonObject
        return jsonParser.makeHttpRequest(DRIVER_URL, JSONParser.POST, params);
    }

    /**
     * Update a driver
     *
     * @param driverId  vehicle id
     * @param firstName driver full name
     * @param lastName  last name
     * @param street    driver street they live on
     * @param city      driver city they live in
     * @param state     driver state they are from
     * @param zip       driver zip code
     * @return JSONObject of whether driver was updated successfully
     */
    public JSONObject updateDriver(String driverId, String firstName, String lastName, String street, String city, String state, String zip) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(TAG_KEY, UPDATE_DRIVER_TAG);
        params.put(DRIVER_ID_KEY, driverId);
        params.put(FULL_NAME_KEY, firstName + " " + lastName);
        params.put(STREET_KEY, street);
        params.put(CITY_KEY, city);
        params.put(STATE_KEY, state);
        params.put(ZIP_KEY, zip);
        // Return JsonObject
        return jsonParser.makeHttpRequest(DRIVER_URL, JSONParser.POST, params);
    }

    /**
     * Deletes driver associated with driver id
     *
     * @param driverId vehicle id
     * @return JSONObject of whether the driver was deleted successfully
     */
    public JSONObject deleteDriver(String driverId) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(TAG_KEY, DELETE_DRIVER_TAG);
        params.put(DRIVER_ID_KEY, driverId);
        // Return JsonObject
        return jsonParser.makeHttpRequest(DRIVER_URL, JSONParser.POST, params);
    }

    /**
     * Syncs the local database and server side database
     *
     * @param userId       user's user id
     * @param localDrivers list of drivers on phone
     * @return JSONObject whether drivers were successfully synced and all vehicles associated with the user id
     */
    public JSONObject syncVehicles(String userId, ArrayList<Driver> localDrivers) {
        HashMap<String, String> params = new HashMap<>();
        JSONObject driverObj = new JSONObject();
        JSONArray driverArray = new JSONArray();

        params.put(TAG_KEY, SYNC_DRIVERS_TAG);
        params.put(USER_ID_KEY, userId);

        try {
            for (int i = 0; i < localDrivers.size(); i++) {
                JSONObject driver = new JSONObject();
                driver.put(DRIVER_ID_KEY, localDrivers.get(i).getDriverId());
                driver.put(FULL_NAME_KEY, localDrivers.get(i).getName());
                driver.put(STREET_KEY, localDrivers.get(i).getStreet());
                driver.put(CITY_KEY, localDrivers.get(i).getTown());
                driver.put(STATE_KEY, localDrivers.get(i).getState());
                driver.put(ZIP_KEY, localDrivers.get(i).getZipCode());
                driverArray.put(i, driver);
            }
            driverObj.put("app_drivers", driverArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(DRIVER_LIST_KEY, driverObj.toString());
        return jsonParser.makeHttpRequest(DRIVER_URL, JSONParser.POST, params);
    }

}

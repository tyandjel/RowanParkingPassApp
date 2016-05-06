package com.example.android.rowanparkingpass.Networking.SendInfo;

import android.util.Log;

import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.JSONParser;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Send Pass info to server
 */
public class SendInfoPass extends SendInfoBase {

    private static final String VEHICLE_KEY = ":vehicle";
    private static final String DRIVER_KEY = ":driver";
    private static final String START_DATE_KEY = ":start";
    private static final String END_DATE_KEY = ":end";

    // constructor
    public SendInfoPass() {
        super();
    }

    /**
     * Adds a vehicle to the server side database
     *
     * @param vehicle   vehicle
     * @param driver    driver
     * @param startDate start date
     * @param endDate   end date
     * @return JSONObject of whether pass was added successfully along with pass id
     */
    public JSONObject addPass(Vehicle vehicle, Driver driver, String startDate, String endDate) {
        // Return FLAG - true/false (Dates interfere, one of the id's are incorrect. Didn't get added)
        // Return ERR (error code) - See google doc
        // Return id
        final String url = IP_ADDRESS_URL + "/register_request.php";
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(VEHICLE_KEY, vehicle.toString());
        params.put(DRIVER_KEY, driver.toString());
        params.put(START_DATE_KEY, startDate);
        params.put(END_DATE_KEY, endDate);
        Log.d("PARAMSPASS", Arrays.asList(params) + "");
        SaveData.makeSendInfo(params, url);

        // Return JsonObject
        return jsonParser.makeHttpRequest(url, JSONParser.POST, params);
    }

}

package com.example.android.rowanparkingpass.Networking.SendInfo;

import com.example.android.rowanparkingpass.Networking.SendToServer;
import com.example.android.rowanparkingpass.SavedDate.SaveData;

import org.json.JSONObject;

import java.util.HashMap;

public class SendInfoPass extends SendInfoBase {

    private static final String VEHICLE_ID_KEY = ":vehicle_id";
    private static final String DRIVER_ID_KEY = ":driver_id";
    private static final String START_DATE_KEY = ":start_date";
    private static final String END_DATE_KEY = ":end_date";

    // constructor
    public SendInfoPass() {
        super();
    }

    /**
     * Adds a vehicle to the server side database
     *
     * @param vehicleID vehicle id
     * @param driverID  driver id
     * @param startDate start date
     * @param endDate   end date
     * @return JSONObject of whether pass was added successfully along with pass id
     */
    public JSONObject addPass(String vehicleID, String driverID, String startDate, String endDate) {
        // Return FLAG - true/false (Dates interfere, one of the id's are incorrect. Diddn't get added)
        // Return ERR (error code) - See google doc
        // Return id
        final String url = IP_ADDRESS_URL + "/register_request.php";
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(VEHICLE_ID_KEY, vehicleID);
        params.put(DRIVER_ID_KEY, driverID);
        params.put(START_DATE_KEY, startDate);
        params.put(END_DATE_KEY, endDate);

        SaveData.makeSendInfo(params, url);
        // Return JsonObject
        return new SendToServer().send();
//        return jsonParser.makeHttpRequest(url, JSONParser.POST, params);
    }

}

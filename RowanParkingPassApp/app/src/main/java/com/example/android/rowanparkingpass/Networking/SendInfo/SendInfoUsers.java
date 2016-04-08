package com.example.android.rowanparkingpass.Networking.SendInfo;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.utilities.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class SendInfoUsers extends SendInfoBase {

    //URL of the PHP API
    private static final String LOGIN_URL = IP_ADDRESS_URL + "/check_cas_auth.php";

//    private static final String LOGIN_TAG = "login";

    private static final String EMAIL_KEY = "username";
    private static final String PASSWORD_KEY = "password";


    // constructor
    public SendInfoUsers() {
        super();
    }

    /**
     * Function to log user in
     *
     * @param email    the email to log into
     * @param password the password assoaciated with the user's email
     * @return JSONObject of whether it was a sucessful login
     */
    public JSONObject loginUser(String email, String password) {
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(EMAIL_KEY, email);
        params.put(PASSWORD_KEY, password);
        // Return JsonObject
        return jsonParser.makeHttpRequest(LOGIN_URL, JSONParser.POST, params);
    }

    /**
     * Function to check if admin
     */
    public boolean isAdmin() {
        if (BaseActivity.ADMIN.equals("TRUE")) {
            return true;
        }
        return false;
    }

}

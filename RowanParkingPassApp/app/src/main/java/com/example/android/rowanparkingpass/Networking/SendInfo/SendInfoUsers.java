package com.example.android.rowanparkingpass.Networking.SendInfo;

import com.example.android.rowanparkingpass.utilities.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class SendInfoUsers extends SendInfoBase {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    public SendInfoUsers() {
        super();
    }

    /**
     * Function to log user in
     *
     * @param username the email to log into
     * @param password the password assoaciated with the user's email
     * @return JSONObject of whether it was a sucessful login
     */
    public JSONObject loginUser(String username, String password) {
        //URL of the PHP login authentication file
        final String LOGIN_URL = IP_ADDRESS_URL + "/check_cas_auth.php";
        // Building Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put(USERNAME_KEY, username);
        params.put(PASSWORD_KEY, password);
        // Return JsonObject
        return jsonParser.makeHttpRequest(LOGIN_URL, JSONParser.POST, params);
    }

    /**
     * Checks if the user is an admin
     *
     * @return true if the logged in user is an admin
     */
    public boolean isAdmin() {
        //TODO
        return true;
    }

    /**
     * Checks if the user has sync on or off
     *
     * @return true if sync is on in the settings
     */
    public boolean isSyncOn() {
        //TODO
        return true;
    }

}

package com.example.android.rowanparkingpass.utilities.userfunctions;

import android.content.Context;

import com.example.android.rowanparkingpass.utilities.databasehandler.DatabaseHandlerLogin;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFunctionsUsers extends UserFunctionsBase {

    //URL of the PHP API
    private static final String LOGIN_URL = IP_ADDRESS_URL + DATABASE_NAME;
    private static final String REGISTER_URL = IP_ADDRESS_URL + DATABASE_NAME;
    private static final String FORGET_PASSWORD_URL = IP_ADDRESS_URL + DATABASE_NAME;
    private static final String CHANGE_PASSWORD_URL = IP_ADDRESS_URL + DATABASE_NAME;

    private static final String LOGIN_TAG = "login";
    private static final String POLICE_LOGIN_TAG = "plogin";
    private static final String REGISTER_TAG = "register";
    private static final String FORGET_PASSWORD_TAG = "forpass";
    private static final String CHANGE_PASSWORD_TAG = "chgpass";

    private static final String FIRST_NAME_KEY = "fname";
    private static final String LAST_NAME_KEY = "lname";
    private static final String USER_NAME_KEY = "uname";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String NEW_PASSWORD_KEY = "newpas";
    private static final String FORGOT_PASSWORD_KEY = "forgotpassword";

    // constructor
    public UserFunctionsUsers() {
        super();
    }

    /**
     * Function to Login
     */
    public JSONObject loginUser(String email, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_KEY, LOGIN_TAG));
        params.add(new BasicNameValuePair(EMAIL_KEY, email));
        params.add(new BasicNameValuePair(PASSWORD_KEY, password));
        // Return JsonObject
        return jsonParser.getJSONFromUrl(LOGIN_URL, params);
    }

    /**
     * Function to Login
     */
    public JSONObject policeLoginUser(String email, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_KEY, POLICE_LOGIN_TAG));
        params.add(new BasicNameValuePair(EMAIL_KEY, email));
        params.add(new BasicNameValuePair(PASSWORD_KEY, password));
        // Return JsonObject
        return jsonParser.getJSONFromUrl(LOGIN_URL, params);
    }

    /**
     * Function to change password
     */
    public JSONObject changePassword(String newPassword, String email) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_KEY, CHANGE_PASSWORD_TAG));

        params.add(new BasicNameValuePair(NEW_PASSWORD_KEY, newPassword));
        params.add(new BasicNameValuePair(EMAIL_KEY, email));
        // Return JsonObject
        return jsonParser.getJSONFromUrl(CHANGE_PASSWORD_URL, params);
    }

    /**
     * Function to reset the password
     */

    public JSONObject forgetPassword(String forgotPassword) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_KEY, FORGET_PASSWORD_TAG));
        params.add(new BasicNameValuePair(FORGOT_PASSWORD_KEY, forgotPassword));
        // Return JsonObject
        return jsonParser.getJSONFromUrl(FORGET_PASSWORD_URL, params);
    }

    /**
     * Function to  Register
     */
    public JSONObject registerUser(String firstName, String lastName, String email, String userName, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_KEY, REGISTER_TAG));
        params.add(new BasicNameValuePair(FIRST_NAME_KEY, firstName));
        params.add(new BasicNameValuePair(LAST_NAME_KEY, lastName));
        params.add(new BasicNameValuePair(EMAIL_KEY, email));
        params.add(new BasicNameValuePair(USER_NAME_KEY, userName));
        params.add(new BasicNameValuePair(PASSWORD_KEY, password));
        // Return JsonObject
        return jsonParser.getJSONFromUrl(REGISTER_URL, params);
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     */
    public boolean logoutUser(Context context) {
        DatabaseHandlerLogin db = new DatabaseHandlerLogin(context);
        db.resetTables();
        return true;
    }

}

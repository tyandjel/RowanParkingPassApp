package com.example.android.rowanparkingpass.utilities.userfunctions;

import com.example.android.rowanparkingpass.utilities.JSONParser;

public class UserFunctionsBase {

     JSONParser jsonParser;

    //URL of the PHP API
    static final String IP_ADDRESS_URL = "http://192.168.1.6/"; // computer ip address
    static final String DATABASE_NAME = "senior_project/";

    static final String TAG_KEY = "tag";

    // constructor
    public UserFunctionsBase() {
        jsonParser = new JSONParser();
    }
}

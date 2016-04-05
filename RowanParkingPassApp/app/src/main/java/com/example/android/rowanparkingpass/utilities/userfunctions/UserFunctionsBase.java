package com.example.android.rowanparkingpass.utilities.userfunctions;

import com.example.android.rowanparkingpass.utilities.JSONParser;

public class UserFunctionsBase {

    JSONParser jsonParser;

    //URL of the PHP API
    static final String IP_ADDRESS_URL = "https://saunderspc.ddns.net"; // computer ip address
    static final String DATABASE_NAME = "Parking/";

    static final String TAG_KEY = "tag";

    // constructor
    public UserFunctionsBase() {
        jsonParser = new JSONParser();
    }
}

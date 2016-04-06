package com.example.android.rowanparkingpass.utilities.userfunctions;

import com.example.android.rowanparkingpass.utilities.JSONParser;

public abstract class UserFunctionsBase {

    JSONParser jsonParser;

    //URL of the PHP API
    public static final String IP_ADDRESS_URL = /*"http://saunderspc.ddns.net"*/"http://150.250.173.97"; // computer ip address
    static final String DATABASE_NAME = "Parking/";

    static final String TAG_KEY = "tag";

    // constructor
    public UserFunctionsBase() {
        jsonParser = new JSONParser();
    }
}

package com.example.android.rowanparkingpass.Networking.SendInfo;

import org.json.JSONObject;

/**
 * Created by johnathan on 4/8/16.
 */
public class SendInfoModel {
    private String url = "";
    private JSONObject json =null;

    public SendInfoModel(JSONObject j, String u){
        url =u;
        json = j;
    }

    public String getUrl() {
        return url;
    }

    public JSONObject getJson() {
        return json;
    }
}

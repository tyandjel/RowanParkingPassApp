package com.example.android.rowanparkingpass.Networking.SendInfo;

import java.util.HashMap;

/**
 * Created by johnathan on 4/8/16.
 */
public class SendInfoModel {
    private String url = "";
    private HashMap<String, String> json = null;
    private int id = -1;
    private boolean isDriver;
    private boolean isVehicle;

    public SendInfoModel(HashMap<String, String> j, String u) {
        url = u;
        json = j;
    }

    public SendInfoModel(HashMap<String, String> j, String u, int i) {
        url = u;
        json = j;
        id = i;
    }

    public void setIsDriver(){
        isDriver = true;
    }

    public boolean isDriverFlag(){
        return isDriver;
    }

    public void setIsVehicle(){
        isVehicle = true;
    }

    public boolean isVehicleFlag(){
        return isVehicle;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getJson() {
        return json;
    }
}

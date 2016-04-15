package com.example.android.rowanparkingpass.SavedDate;

import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Information that is saved when the activity is closed
 * Saves the current location, server password, and a list of pending taps
 */
public  class SaveData implements Serializable {
    private static Queue<SendInfoModel> sendInfos = new LinkedList<>();
    private static boolean sync = true;



    public static void setSync(boolean sync) {
        SaveData.sync = sync;
    }
    public static boolean getSync(){
        return sync;
    }

    public static void makeSendInfo(HashMap<String, String> j, String u) {
        addSendInfo(new SendInfoModel(j, u));
    }
    public static void makeSendInfo(HashMap<String, String> j, String u, int id) {

        addSendInfo(new SendInfoModel(j, u,id));
    }

    public static boolean addSendInfo(SendInfoModel s) {
        Log.d("Added To Que", s.toString());
        return sendInfos.add(s);
    }

    public  static Queue<SendInfoModel> getQueue(){
        return sendInfos;
    }

    public static SendInfoModel peek() {
        return sendInfos.peek();
    }

    public static SendInfoModel remove() {

        Log.d("remove from Que", sendInfos.toString());
        return sendInfos.remove();
    }

    public static int size() {
        return sendInfos.size();
    }

}

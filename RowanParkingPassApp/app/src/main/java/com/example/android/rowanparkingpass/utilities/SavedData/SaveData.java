package com.example.android.rowanparkingpass.utilities.SavedData;

import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Information that is saved when the activity is closed
 * Saves the current location, server password, and a list of pending taps
 */
public class SaveData implements Serializable {
    private static Queue<SendInfoModel> sendInfos = new LinkedList<>();
    private static boolean sync = true;
    private static String USR;

    public SaveData(String user, boolean sync, Queue<SendInfoModel> q) {
        USR = user;
        this.sync = sync;
        sendInfos = q;

    }

    public static void setUSR(String u) {
        USR = u;
    }

    public static String getUSR() {
        return USR;
    }

    public String getUser() {
        return USR;
    }

    public void setUser(String user) {
        USR = user;
    }

    public static void setSync(boolean sync) {
        SaveData.sync = sync;
    }

    public static boolean getSync() {
        return sync;
    }

    public static void makeSendInfo(HashMap<String, String> j, String u) {
        addSendInfo(new SendInfoModel(j, u));
    }

    public static void makeSendInfo(HashMap<String, String> j, String u, int id) {

        addSendInfo(new SendInfoModel(j, u, id));
    }

    public static boolean addSendInfo(SendInfoModel s) {
        Log.d("Added To Que", s.toString());
        return sendInfos.add(s);
    }

    public static Queue<SendInfoModel> getQueue() {
        return sendInfos;
    }

    public static void setQueue(Queue<SendInfoModel> queue) {
        sendInfos = queue;
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

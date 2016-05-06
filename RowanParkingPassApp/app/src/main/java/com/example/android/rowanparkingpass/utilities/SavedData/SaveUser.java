package com.example.android.rowanparkingpass.utilities.SavedData;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Saves user info to android
 */
public class SaveUser implements Serializable {

    private Queue<SendInfoModel> sendInfos = new LinkedList<>();
    private boolean sync = true;
    private String USR;

    public SaveUser(String user, boolean sync, Queue<SendInfoModel> q) {
        USR = user;
        this.sync = sync;
        sendInfos = q;

    }


    public Queue<SendInfoModel> getSendInfos() {
        return sendInfos;
    }

    public void setSendInfos(Queue<SendInfoModel> sendInfos) {
        this.sendInfos = sendInfos;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getUSR() {
        return USR;
    }

    public void setUSR(String USR) {
        this.USR = USR;
    }
}

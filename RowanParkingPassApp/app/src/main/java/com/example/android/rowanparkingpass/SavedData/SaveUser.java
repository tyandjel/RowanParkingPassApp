package com.example.android.rowanparkingpass.SavedData;

import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by johnathan on 4/18/16.
 */
public class SaveUser implements Serializable{

        private  Queue<SendInfoModel> sendInfos = new LinkedList<>();
        private  boolean sync = true;
        private  String USR;
        public SaveUser(String user,boolean sync, Queue<SendInfoModel> q){
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

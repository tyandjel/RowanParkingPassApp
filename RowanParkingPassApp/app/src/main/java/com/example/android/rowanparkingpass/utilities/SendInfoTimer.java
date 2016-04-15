package com.example.android.rowanparkingpass.utilities;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendToServer;

/**
 * Created by Tyler on 4/15/2016.
 */
public class SendInfoTimer extends AsyncTask<Void, Void, Void> {

    public void sendInfo() {
        execute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Looper.prepare();
        for (int i = 0; i < 999999999; i++) {
            try {
                Log.d("LOOP", "HERE");
                SendToServer s = new SendToServer();
                s.send();
                Thread n = new Thread();

                n.sleep(200, 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //s.wait(200000);
        }
        return null;
    }
}

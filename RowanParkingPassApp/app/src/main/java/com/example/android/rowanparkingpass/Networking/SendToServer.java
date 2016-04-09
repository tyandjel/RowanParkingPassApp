package com.example.android.rowanparkingpass.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;
import com.example.android.rowanparkingpass.SavedDate.SaveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * {example of sending a driver
 * SaveData.makeSendInfo(makejson(driver),url);
 * SendToServer s = new SendToServer();
 * s.send();}
 * sends Json objects to the server by poping the queue in saveData for the json object and the url
 * Also Known as Carrier Pigeon <(*_*<)
 * Created by John on 3/7/2016.
 */
public class SendToServer {

    private static final String LOG_TAG = SendToServer.class.getSimpleName();
    private JSONObject jsonObject;

    public SendToServer() {

    }

    public JSONObject send() {
        SendJSON api = new SendJSON();
        api.execute();
        return jsonObject;
    }

    public class SendJSON extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject sendJSon(String output, String urlOut) throws Exception {
            JSONObject jObj = null;
            try {
                URL url = new URL(urlOut);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setReadTimeout(10000); // 10 seconds
                connection.setConnectTimeout(15000); // 15 seconds

                connection.setDoOutput(true); // You need to set it to true  if you want to send (output) a request body
                connection.connect();

                OutputStream outputStream = connection.getOutputStream();
                DataOutputStream dStream = new DataOutputStream(outputStream);
                dStream.writeBytes(output); // Writes out the string to the underlying output stream as a sequence of bytes
                dStream.flush(); // Flushes the data output stream.
                dStream.close(); // Closing the output stream.
                //====== response from server
                int responseCode = connection.getResponseCode();
                if (responseCode > 199 && responseCode < 208) {
                    Log.d(urlOut + " Response ", responseCode + "");
                } else {
                    Log.d(urlOut + " Response Code bad", responseCode + "");
                    throw new Exception();
                }
                StringBuilder result = new StringBuilder();
                try {
                    //Receive the response from the server
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.d("LINE: ", line);
                        result.append(line);
                    }
                    Log.d("JSON Parser2", "result: " + result.toString());
                } catch (IOException e) {
                    Log.d("ERROR: ", e.getMessage());
                    e.printStackTrace();
                }
                try {
                    Log.d("RESULT: ", result.toString());
                    jObj = new JSONObject(result.toString());
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                Log.d(LOG_TAG, urlOut + " Sent String " + output);
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(urlOut + " Error ", output + " exception: " + e);
                throw new Exception();
            }
            // return JSON Object
            return jObj;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            SendInfoModel tempSendInfo = SaveData.remove();
            try {
                if (SaveData.peek() != null) {
                    jsonObject = sendJSon(tempSendInfo.getJson().toString(), tempSendInfo.getUrl());
                    return jsonObject;
                }
            } catch (Exception e) {
                SaveData.addSendInfo(tempSendInfo);
                Log.d("doInBackground: ", e.toString());
            }
            return null;
        }
    }
}


package com.example.android.rowanparkingpass.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoModel;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {example of sending a driver
 * SaveData.makeSendInfo(makejson(driver),url);
 * SendToServer s = new SendToServer();
 * s.send();}
 * sends Json objects to the server by poping the queue in saveData for the json object and the url
 * Also Known as Carrier Pigeon <(*_*<)
 * Created by John on 3/7/2016.
 */
public class SendToServer extends BaseActivity {

    private static final String LOG_TAG = SendToServer.class.getSimpleName();
    private JSONObject jsonObject;
    String charset = "UTF-8";
    JSONObject jObj = null;
    StringBuilder sbParams;

    public SendToServer() {

    }


    public synchronized JSONObject send() {
        SendJSON api = new SendJSON();
        api.execute();
        return jsonObject;
    }


    public class SendJSON extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject sendJSon(HashMap<String, String> output, String urlOut) throws Exception {
            try {
                URL url = new URL(urlOut);

                sbParams = new StringBuilder();
                int i = 0;
                for (String key : output.keySet()) {
                    try {
                        if (i != 0) {
                            sbParams.append("&");
                        }
                        sbParams.append(key).append("=")
                                .append(URLEncoder.encode(output.get(key), charset));

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    i++;
                }


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setReadTimeout(10000); // 10 seconds
                connection.setConnectTimeout(15000); // 15 seconds
                //connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept-Charset", charset);
                //connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true); // You need to set it to true  if you want to send (output) a request body


                if (!BaseActivity.COOKIE.equals("")) {
                    connection.setRequestProperty("Cookie", "PHPSESSID=" + BaseActivity.COOKIE);
                    Log.d("COOKIE", BaseActivity.COOKIE);
                }
                connection.connect();

                OutputStream outputStream = connection.getOutputStream();
                DataOutputStream dStream = new DataOutputStream(outputStream);
                dStream.writeBytes(sbParams.toString()); // Writes out the string to the underlying output stream as a sequence of bytes
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
                    Map<String, List<String>> m = connection.getHeaderFields();
                    Set keys = m.keySet();

                    for (Iterator j = keys.iterator(); j.hasNext(); ) {
                        String key = (String) j.next();
                        List<String> value = m.get(key);
                        Log.d("HEADER", key + " = " + Arrays.asList(value));
                    }
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

            } catch (
                    Exception e
                    )

            {
                e.printStackTrace();
                Log.d(urlOut + " Error ", output + " exception: " + e);
                throw new Exception();
            }
            // return JSON Object
            return jObj;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            int a = SaveData.size() * 3;
            for (int i = a - 1; i >= 0 && !(SaveData.size() == 0); i--) {
                Log.d("SEND Number", a - i + "");
                SendInfoModel tempSendInfo = SaveData.remove();

                try {
                    if (tempSendInfo != null) {
                        jsonObject = sendJSon(tempSendInfo.getJson(), tempSendInfo.getUrl());
                        if (tempSendInfo.isDriverFlag()) {
                            int newID = Integer.parseInt(jsonObject.getString("id"));
                            new DatabaseHandlerDrivers(getApplicationContext()).updateDriverWithID(tempSendInfo.getId(), newID - 1);
                        } else if (tempSendInfo.isVehicleFlag()) {
                            int newID = Integer.parseInt(jsonObject.getString("id"));
                            new DatabaseHandlerVehicles(getApplicationContext()).updateVehicleWithID(tempSendInfo.getId(), newID - 1);
                        }
                        return jsonObject;
                    }
                } catch (Exception e) {
                    SaveData.addSendInfo(tempSendInfo);
                    Log.d("Send to server failed: ", e.toString());
                    Log.d("Queue Size:", String.valueOf(SaveData.size()));
                }
            }
            return jsonObject;
        }
    }
}


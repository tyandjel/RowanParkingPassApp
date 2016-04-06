package com.example.android.rowanparkingpass.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.utilities.userfunctions.UserFunctionsBase;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkCheck extends BaseActivity {

    public void NetAsync(View view, BaseActivity activity, String className) {
        new NetCheck(view, activity, className).execute();
    }

    /**
     * Async Task to check whether internet connection is working.
     */

    private class NetCheck extends AsyncTask<String, Boolean, Boolean> {

        private ProgressDialog nDialog;
        private BaseActivity activity;
        private String className;


        public NetCheck(View v, BaseActivity activity, String className) {
            this.activity = activity;
            this.className = className;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                activity.showNetworkDialog();
            }
        }

        @Override
        protected Boolean doInBackground(String... args) {

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(UserFunctionsBase.IP_ADDRESS_URL);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean th) {
            if (th) {
                activity.dismissNetworkDialog();
                try {
                    Class c = Class.forName(className);
                    Object t = c.newInstance();
                    Method[] allMethods = c.getMethods();
                    for (Method m : allMethods) {
                        String mname = m.getName();
                        if (!mname.startsWith("execute") || m.getGenericReturnType() != boolean.class) {
                            continue;
                        }
                        try {
                            m.setAccessible(true);
                            Object o = m.invoke(t);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                activity.dismissNetworkDialog();
                Toast.makeText(activity.getApplicationContext(), R.string.error_in_network_connection, Toast.LENGTH_LONG).show();
            }
        }
    }
}

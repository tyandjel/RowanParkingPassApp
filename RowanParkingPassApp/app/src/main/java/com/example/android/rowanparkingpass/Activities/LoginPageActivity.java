package com.example.android.rowanparkingpass.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.ListActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.PassesActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerUser;
import com.example.android.rowanparkingpass.utilities.userfunctions.UserFunctionsUsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoginPageActivity extends BaseActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        Button btnLogin = (Button) findViewById(R.id.createdmainmenu);
        Button btnForgotPassword = (Button) findViewById(R.id.forgotpass);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RowanWebPageActivity.class);
                myIntent.putExtra(MODE, mode.FORGOT_PASSWORD.name());
                startActivity(myIntent);
            }
        });

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if ((!inputEmail.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                    NetAsync(view);
                } else if ((!inputEmail.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!inputPassword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO Remove next line
                    Intent upanel = new Intent(getApplicationContext(), PassesActivity.class);
                    upanel.putExtra(MODE, mode.HOME_PAGE.name());
                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(upanel);
                    /**
                     * Close Login Screen
                     **/
//                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void NetAsync(View view) {
        new NetCheck().execute();
    }

    /**
     * Async Task to check whether internet connection is working.
     */

    private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginPageActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpsURLConnection urlc = (HttpsURLConnection) url.openConnection();
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
                nDialog.dismiss();
                new ProcessLogin().execute();
            } else {
                nDialog.dismiss();
                loginErrorMsg.setText(R.string.error_in_network_connection);
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     */
    private class ProcessLogin extends AsyncTask<String, JSONObject, JSONObject> {

        private String email, password;
        private ProgressDialog pDialog;

        private static final String USER_KEY = "user";
        private static final String KEY_SUCCESS = "success";
        private static final String KEY_USER_ID = "user_id";
        private static final String KEY_USER_NAME = "user_name";
        private static final String KEY_IS_ADMIN = "is_admin";
        private static final String KEY_SYNC = "sync";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputEmail = (EditText) findViewById(R.id.username);
            inputPassword = (EditText) findViewById(R.id.pword);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(LoginPageActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctionsUsers userFunction = new UserFunctionsUsers();
            // Return JsonObject
            return userFunction.loginUser(email, password);
        }

        protected void onPostExecute(JSONObject json) {

            try {
                /*
                Called when the activity is first created.
                */
                if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandlerUser db = new DatabaseHandlerUser(getApplicationContext());
                        JSONObject json_user = json.getJSONObject(USER_KEY);
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctionsUsers logout = new UserFunctionsUsers();
                        logout.logoutUser(getApplicationContext());

                        db.addUser(json_user.getString(KEY_USER_ID), json_user.getString(KEY_USER_NAME), json_user.getInt(KEY_IS_ADMIN), json_user.getInt(KEY_SYNC));
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent upanel = new Intent(getApplicationContext(), HomePageActivity.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();

                    } else {

                        pDialog.dismiss();
                        loginErrorMsg.setText(R.string.incorrect_username_password);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
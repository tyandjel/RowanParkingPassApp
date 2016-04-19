package com.example.android.rowanparkingpass.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.PassesActivity;
import com.example.android.rowanparkingpass.Networking.NetworkCheck;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoUsers;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.SavedData.ReadWrite;
import com.example.android.rowanparkingpass.SavedData.SaveData;
import com.example.android.rowanparkingpass.SavedData.SaveUser;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginPageActivity extends BaseActivity {

    private static EditText inputUserName;
    private static EditText inputPassword;
    private static TextView loginErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);
        readInSavedData();
        setupUI(findViewById(R.id.parent));
        inputUserName = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        Button btnLogin = (Button) findViewById(R.id.createdmainmenu);
        Button btnForgotPassword = (Button) findViewById(R.id.forgotpass);
        Button btnAdminLogin = (Button) findViewById(R.id.admin_login);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);


        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RowanWebPageActivity.class);
                myIntent.putExtra(MODE, mode.FORGOT_PASSWORD.name());
                startActivity(myIntent);
            }
        });

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RowanWebPageActivity.class);
                myIntent.putExtra(MODE, mode.PASS_SEARCH.name());
                startActivity(myIntent);
            }
        });

        /*
         * Login button click event
         * A Toast is set to alert when the Email and Password field is empty
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if ((!inputUserName.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                    new NetworkCheck().NetAsync(view, LoginPageActivity.this);
                } else if ((!inputUserName.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!inputPassword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {

                    //TODO Remove next line

                    //Intent upanel = new Intent(getApplicationContext(), PassesActivity.class);

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



    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilities.hideSoftKeyboard(LoginPageActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void executeProcessLogin() {
        new ProcessLogin().execute();
    }

    /**
     * Async Task to check login credentials and login the user through JSON response.
     */
    private class ProcessLogin extends AsyncTask<String, JSONObject, JSONObject> {

        private String userName, password;
        private ProgressDialog pDialog;

        private static final String KEY_SUCCESS = "FLAG";
        private static final String KEY_COOKIE = "GDHR";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputUserName = (EditText) findViewById(R.id.username);
            inputPassword = (EditText) findViewById(R.id.pword);
            userName = inputUserName.getText().toString();
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
            SendInfoUsers userFunction = new SendInfoUsers();
            // Return JsonObject
            return userFunction.loginUser(userName, password);
        }

        protected void onPostExecute(JSONObject json) {

            try {
                /*
                Called when the activity is first created.
                */
                String res = json.getString(KEY_SUCCESS);

                if (res.equals("true")) {
                    BaseActivity.COOKIE = json.getString(KEY_COOKIE);
                    pDialog.setMessage("Loading User Space");
                    pDialog.setTitle("Getting Data");
                    USER = userName;
                    Intent upanel = new Intent(getApplicationContext(), PassesActivity.class);
                    upanel.putExtra(MODE, mode.HOME_PAGE.name());
                    upanel.putExtra(SYNC, "true");
                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //TODO: If different user call clearDatabases below

                    if (user!=null && !USER.equals(user)) {
                        clearDatabases();
                    }
                    SaveData.setUSR(USER);
                    pDialog.dismiss();
                    startActivity(upanel);
                    // Start SendInfoTimer guy
                    //timer.sendInfo();
                } else {
                    pDialog.dismiss();
                    loginErrorMsg.setText(R.string.incorrect_username_password);
                }
            } catch (JSONException | NullPointerException e) {
                loginErrorMsg.setText(e.getMessage());
                e.printStackTrace();
            }

        }

        private void clearDatabases() {
            new DatabaseHandlerDrivers(getApplicationContext()).resetTables();
            new DatabaseHandlerVehicles(getApplicationContext()).resetTables();
            new DatabaseHandlerPasses(getApplicationContext()).resetTables();
            Log.d("CLEAR DATABASE", "clear");
            Toast.makeText(getApplicationContext(),
                    "Different User Logged In. Cleared local vehicles and drivers.", Toast.LENGTH_SHORT).show();
        }
    }


    public void onStop(){
        super.onStop();


    }




}
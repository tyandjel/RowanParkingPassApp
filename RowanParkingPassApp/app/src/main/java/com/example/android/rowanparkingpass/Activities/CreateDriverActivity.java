package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;

public class CreateDriverActivity extends BaseActivity {

    private static final String TEMP_DRIVER = "temp";

    EditText fullName;
    EditText street;
    EditText city;
    Spinner state;
    EditText zipCode;
    CheckBox saveInfo;
    DatabaseHandlerDrivers db;
    Intent pastIntent;
    String pastMode;
    Driver driver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver);
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        saveInfo = (CheckBox) findViewById(R.id.saveInfoOnPhoneCheckBox);
        state = (Spinner) findViewById(R.id.driverSpinner);
        fullName = (EditText) findViewById(R.id.fullNameEditText);
        street = (EditText) findViewById(R.id.streetEditText);
        city = (EditText) findViewById(R.id.cityEditText);
        zipCode = (EditText) findViewById(R.id.zipCodeEditText);
        db = new DatabaseHandlerDrivers(getApplicationContext());

        driver = (Driver) pastIntent.getSerializableExtra("Driver");

        Button cancel = (Button) findViewById(R.id.cancelDriverButton);

        // Change to new activity
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancel was clicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Cancelbtn");
                Intent myIntent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                if (currentMode.equals(mode.UPDATE_DRIVER.name())) {
                    myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());

                } else {
                        myIntent.putExtra(MODE, mode.DRIVERS.name());

                }
                startActivity(myIntent);
                finish();
            }
        });
        // ======= Create Driver/ Update Driver
        Button createDriver = (Button) findViewById(R.id.createDriverButton);
        createDriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Toast.makeText(this, "Create was clicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Createbtn");
                if (TextUtils.isEmpty(fullName.getText()) || TextUtils.isEmpty(street.getText()) ||
                        TextUtils.isEmpty(city.getText()) || TextUtils.isEmpty(zipCode.getText())) {
                    Log.d(TAG, "onClick: All Field Empty");
                    Toast.makeText(getApplicationContext(), "Fill out all driver fields", Toast.LENGTH_SHORT).show();
                } else if (zipCode.getText().length() != 5) {
                    Log.d(TAG, "onClick: !5 zip");
                    Toast.makeText(getApplicationContext(), "Enter a 5 digit zip code", Toast.LENGTH_SHORT).show();
                } else {
                    // opens the next activity
                    Intent myIntent;
                    if (currentMode.equals(mode.UPDATE_DRIVER.name())) {
                        myIntent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                        myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());

                        db.updateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());

                    } else {
                        if(pastIntent.getStringExtra("Old").equals( mode.DRIVERS.name())){
                            myIntent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                            myIntent.putExtra(MODE, mode.DRIVERS.name());
                        }else {
                            myIntent = new Intent(CreateDriverActivity.this, VehiclesActivity.class);
                            myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                        }
                        db.addDriver(fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                        //Todo: add ID to addDriver later
                    }
                    startActivity(myIntent);
                    finish();
                }
            }
        });
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values());
        state.setAdapter(spinnerAdapter);
        if (currentMode != null && currentMode.equals(mode.UPDATE_DRIVER.name())) {
            setTitle("Update Driver");
            createDriver.setText("Update Driver");
            buildUpdateDriver(spinnerAdapter);
        } else {
            setTitle("Create New Driver");
        }


        saveInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Button createDriver = (Button) findViewById(R.id.createDriverButton);
                if (isChecked) {
                    if (currentMode.equals(mode.UPDATE_DRIVER.name())) {
                        createDriver.setText(R.string.save_driver);
                    } else {
                        createDriver.setText(R.string.create_driver);
                    }
                } else {
                    createDriver.setText(R.string.create_temp_driver);
                }
            }
        });
    }

    public void buildUpdateDriver(ArrayAdapter spinnerAdapter) {
        fullName.setText(driver.getName());
        street.setText(driver.getStreet());
        city.setText(driver.getTown());
        state.setSelection(States.getPosition(driver.getState()));
        zipCode.setText(driver.getZipCode());
        CheckBox saveInfo; // possible future bug todo: make toast to inform user of deletion

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (currentMode != null && currentMode.equals(mode.UPDATE_DRIVER.name())) {
            inflater.inflate(R.menu.menu_delete, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            // action with ID action_delete was selected
            case R.id.action_delete:
                Toast.makeText(this, "Delete selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, DriversActivity.class);
                myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
                // delete driver from database
                db.deleteDriver(String.valueOf(driver.getDriverId()));
                startActivity(myIntent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
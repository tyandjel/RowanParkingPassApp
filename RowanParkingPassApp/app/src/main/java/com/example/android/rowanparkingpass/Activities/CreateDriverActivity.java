package com.example.android.rowanparkingpass.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.io.Serializable;
import java.util.ArrayList;

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
    Driver driver;
    Vehicle vehicle;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver);
        setupUI(findViewById(R.id.parent));
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        context = getApplicationContext();
        saveInfo = (CheckBox) findViewById(R.id.saveInfoOnPhoneCheckBox);
        state = (Spinner) findViewById(R.id.driverSpinner);
        fullName = (EditText) findViewById(R.id.fullNameEditText);
        street = (EditText) findViewById(R.id.streetEditText);
        city = (EditText) findViewById(R.id.cityEditText);
        zipCode = (EditText) findViewById(R.id.zipCodeEditText);
        db = new DatabaseHandlerDrivers(getApplicationContext());

        driver = (Driver) pastIntent.getSerializableExtra("Driver");
        vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");

        Button cancel = (Button) findViewById(R.id.cancelDriverButton);

        // Change to new activity
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "onClick: Cancelbtn");
                Intent intent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                if (currentMode.equals(mode.UPDATE_DRIVER.name())) {
                    intent.putExtra(MODE, mode.DRIVERS_LIST.name());
                } else if (currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
                    intent = new Intent(CreateDriverActivity.this, PassActivity.class);
                    intent.putExtra(MODE, mode.CREATE_PASS.name());
                    intent.putExtra("Driver", (Serializable) driver);
                    intent.putExtra("Vehicle", (Serializable) vehicle);
                } else {
                    intent.putExtra(MODE, mode.DRIVERS.name());

                }
                startActivity(intent);
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
                    Intent intent;
                    if (currentMode.equals(mode.UPDATE_DRIVER.name())) { // checks if ur updating a driver
                        intent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                        intent.putExtra(MODE, mode.DRIVERS_LIST.name()); // tells the intent that it has to use the update driver list logic
                        // updates driver in database
                        db.updateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                    } else if (currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
                        intent = new Intent(CreateDriverActivity.this, PassActivity.class);
                        intent.putExtra(MODE, mode.DRIVERS_LIST.name()); // tells the intent that it has to use the update driver list logic
                        // updates driver in database
                        db.updateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                    } else { // if not Updating then u are creating a driver
                        if (pastIntent.getStringExtra("Old").equals(mode.DRIVERS_LIST.name())) { // checks if the old intent was the DRivers or driver_list
                            intent = new Intent(CreateDriverActivity.this, DriversActivity.class); // it was the drivers list go back to the drivers list
                            intent.putExtra(MODE, mode.DRIVERS_LIST.name());
                        } else { // was no the drivers list create driver and move to vehicle list
                            intent = new Intent(CreateDriverActivity.this, VehiclesActivity.class);
                        }
                        // add new driver
                        db.addDriver(fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                        //Todo: add ID to addDriver later
                        intent.putExtra(MODE, mode.VEHICLES.name());
                        ArrayList<Driver> drivers = db.getDrivers();
                        intent.putExtra("Driver", drivers.get(drivers.size() - 1));
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values());
        state.setAdapter(spinnerAdapter);
        if (currentMode != null && (currentMode.equals(mode.UPDATE_DRIVER.name()) || currentMode.equals(mode.UPDATE_PASS_DRIVER.name()))) {
            setTitle("Update Driver");
            createDriver.setText("Update Driver");
            buildUpdateDriver();
        } else {
            setTitle("Create New Driver");
        }


        saveInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Button createDriver = (Button) findViewById(R.id.createDriverButton);
                if (isChecked) {
                    if (currentMode.equals(mode.UPDATE_DRIVER.name()) || currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
                        createDriver.setText(R.string.save_driver);
                    } else {
                        createDriver.setText(R.string.create_driver);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Driver will not be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void buildUpdateDriver() {
        fullName.setText(driver.getName());
        street.setText(driver.getStreet());
        city.setText(driver.getTown());
        state.setSelection(States.getPosition(driver.getState()));
        zipCode.setText(driver.getZipCode());
        saveInfo.setChecked(true);
        saveInfo.setEnabled(false);

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
        switch (item.getItemId()) {
            // action with ID action_delete was selected
            case R.id.action_delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateDriverActivity.this);
                alertDialog.setTitle("Delete Driver?");
                alertDialog.setMessage(driver.getName());
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                        myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
                        // delete driver from database
                        db.deleteDriver(String.valueOf(driver.getDriverId()));
                        new DatabaseHandlerPasses(context).deleteRequestDriverID(String.valueOf(driver.getDriverId()));
                        startActivity(myIntent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                alertDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilities.hideSoftKeyboard(CreateDriverActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();


    }
}
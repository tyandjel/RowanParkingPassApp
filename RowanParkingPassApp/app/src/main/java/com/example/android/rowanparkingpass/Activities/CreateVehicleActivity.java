package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerDialog;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerSwatch;
import com.example.android.rowanparkingpass.utilities.colorpicker.Utils;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import java.util.ArrayList;

public class CreateVehicleActivity extends BaseActivity  {

    private static final String TEMP_VEHICLE = "temp";

    private EditText make;
    private EditText model;
    private EditText year;
    private EditText colorBox;
    private EditText license;
    private CheckBox saveInfo;
    private ColorPickerDialog colorCalender;
    private int mSelectedColorCal0;
    private int[] mColor;
    Intent pastIntent;
    private Driver driver;
    private Vehicle vehicle;

    Spinner state;
    DatabaseHandlerVehicles db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);

        make = (EditText) findViewById(R.id.vehicleMakeEditText);
        model = (EditText) findViewById(R.id.modelEditText);
        year = (EditText) findViewById(R.id.yearEditText);
        colorBox = (EditText) findViewById(R.id.vehicleColorEditText);
        state = (Spinner) findViewById(R.id.vehicleSpinner);
        license = (EditText) findViewById(R.id.licenseEditText);
        saveInfo = (CheckBox) findViewById(R.id.saveVehicleInfoOnPhoneCheckBox);


        final Button createVehicle = (Button) findViewById(R.id.createVehicleButton);
        pastIntent=getIntent();
        driver = (Driver) pastIntent.getSerializableExtra("Driver");
        vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values());
        state.setAdapter(spinnerAdapter);

        Button cancel = (Button) findViewById(R.id.cancelVehicleButton);
        Button create = (Button) findViewById(R.id.createVehicleButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancel was clicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Cancelbtn");
                Intent myIntent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class);
                if (currentMode.equals(mode.UPDATE_VEHICLE.name())) {
                    myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());

                } else {
                    myIntent.putExtra(MODE, mode.VEHICLES.name());
                }
                myIntent.putExtra("Driver", pastIntent.getSerializableExtra("Driver"));
                startActivity(myIntent);
                finish();
            }
        });
        create.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent;

                if (TextUtils.isEmpty(year.getText()) || TextUtils.isEmpty(make.getText()) ||
                        TextUtils.isEmpty(model.getText()) || TextUtils.isEmpty(license.getText())) {
                    Log.d(TAG, "onClick: All Field Empty");
                    Toast.makeText(getApplicationContext(), "Fill out all driver fields", Toast.LENGTH_SHORT).show();
                } else if (year.getText().length() != 4) {
                    Log.d(TAG, "onClick: !4 year");
                    Toast.makeText(getApplicationContext(), "Enter a 4 digit year", Toast.LENGTH_SHORT).show();
                } else {
                    db = new DatabaseHandlerVehicles(getApplicationContext());
                    // opens the next activity
                    if (currentMode.equals(mode.UPDATE_VEHICLE.name())) { // checks if ur updating a driver
                        myIntent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class);
                        myIntent.putExtra(MODE, mode.VEHICLES_LIST.name()); // tells the intent that it has to use the update driver list logic
                        // updates driver in database
                        Vehicle vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");

                        db.updateVehicle(vehicle.getVehicleId(), Integer.valueOf(year.getText().toString()), make.getText().toString(), model.getText().toString(), state.getSelectedItem().toString(), String.valueOf(mSelectedColorCal0), license.getText().toString());
                    } else { // if not Updating then u are creating a driver
                        if (pastIntent.getStringExtra("Old").equals(mode.VEHICLES_LIST.name())) { // checks if the old intent was the DRivers or driver_list
                            myIntent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class); // it was the drivers list go back to the drivers list
                            myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                        } else { // was no the drivers list create driver and move to vehicle list
                            myIntent = new Intent(CreateVehicleActivity.this, PassActivity.class);
                            myIntent.putExtra(MODE, mode.VEHICLES.name());
                        }
                        // add new driver
                        db.addVehicle(Integer.valueOf(year.getText().toString()), make.getText().toString(), model.getText().toString(), state.getSelectedItem().toString(), String.valueOf(mSelectedColorCal0), license.getText().toString());
                        ArrayList<Vehicle> vehicles = db.getVehicles();
                        myIntent.putExtra("Vehicle",vehicles.get(vehicles.size()-1));
                        //Todo: add ID to addVehicle later
                    }
                    myIntent.putExtra("Driver", pastIntent.getSerializableExtra("Driver"));
                    startActivity(myIntent);
                    finish();
                }
            }
        }));

        setupUI(findViewById(R.id.parent));

        Intent pastIntent = getIntent();
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);


        if (currentMode.equals(mode.UPDATE_VEHICLE.name())) {
            setTitle("Update Vehicle");
            buildUpdateDriver();
            create.setText("Update");
        } else {
            setTitle("Create New Vehicle");
        }


        //cancel.setOnClickListener(this);
        //createVehicle.setOnClickListener(this);

        saveInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (currentMode.equals(mode.UPDATE_VEHICLE.name())) {
                        createVehicle.setText(R.string.save_vehicle);
                    } else {
                        createVehicle.setText(R.string.create_vehicle);
                    }
                } else {
                    createVehicle.setText(R.string.create_temp_vehicle);
                }
            }
        });

        //Set up the colorBox picker for car colorBox
        mColor = Utils.ColorUtils.colorChoice(getApplicationContext());
        colorCalender = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColor, mSelectedColorCal0, 5, Utils.isTablet(this) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL);
        colorBox.setInputType(InputType.TYPE_NULL);
        colorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorCalender.show(getFragmentManager(), "cal");
            }
        });
        colorCalender.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mSelectedColorCal0 = color;
                colorBox.setBackgroundColor(mSelectedColorCal0);
                colorBox.setTextColor(mSelectedColorCal0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (currentMode != null && currentMode.equals(mode.UPDATE_VEHICLE.name())) {
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
                myIntent = new Intent(this, VehiclesActivity.class);
                myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                startActivity(myIntent);
                finish();
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
                    Utilities.hideSoftKeyboard(CreateVehicleActivity.this);
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


    public void buildUpdateDriver() {
        make.setText(vehicle.getMake());
        model.setText(vehicle.getModel());
        year.setText(String.valueOf(vehicle.getYear()));
        state.setSelection(States.getPosition(vehicle.getVehicleState()));
        colorBox.setBackgroundColor(Integer.valueOf(vehicle.getColor()));
        license.setText(vehicle.getLicensePlate());
        saveInfo.setEnabled(false);

    }
}
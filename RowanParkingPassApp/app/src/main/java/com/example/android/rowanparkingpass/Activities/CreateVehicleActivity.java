package com.example.android.rowanparkingpass.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerDialog;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerSwatch;
import com.example.android.rowanparkingpass.utilities.colorpicker.Utils;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import org.json.JSONObject;

import java.util.ArrayList;

public class CreateVehicleActivity extends BaseActivity {

    private EditText make;
    private EditText model;
    private EditText year;
    private EditText colorBox;
    private EditText license;
    private CheckBox saveInfo;
    private ColorPickerDialog colorCalender;
    private int mSelectedColorCal0;
    private Intent pastIntent;
    private Driver driver;
    private Vehicle vehicle;
    private Button createVehicle;
    private Button cancel;
    private Spinner state;
    private DatabaseHandlerVehicles db;
    private Context context;
    private States[] arrayOfStates = States.values();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        //populates the global fields
        populateFeilds();
        //builds the cancelBtn and its listener
        buildCancelBtn();
        //builds the createVehicle button and its listener
        buildCreateBtn();
        setupUI(findViewById(R.id.parent));
        //sets the banner text to the current state of the activity
        buildBannerText();
        // makes the check box and it's listener
        buildCehckBox();
        //Set up the colorBox picker for car colorBox
        buildColorBox();
    }

    private void populateFeilds() {
        make = (EditText) findViewById(R.id.vehicleMakeEditText);
        model = (EditText) findViewById(R.id.modelEditText);
        year = (EditText) findViewById(R.id.yearEditText);
        colorBox = (EditText) findViewById(R.id.vehicleColorEditText);
        state = (Spinner) findViewById(R.id.vehicleSpinner);
        license = (EditText) findViewById(R.id.licenseEditText);
        saveInfo = (CheckBox) findViewById(R.id.saveVehicleInfoOnPhoneCheckBox);
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        driver = (Driver) pastIntent.getSerializableExtra("Driver");
        vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values());
        state.setAdapter(spinnerAdapter);
        cancel = (Button) findViewById(R.id.cancelVehicleButton);
        createVehicle = (Button) findViewById(R.id.createVehicleButton);
        context = getApplicationContext();

    }

    private void buildCancelBtn() {
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class);
                if (currentMode.equals(mode.UPDATE_VEHICLE.name())) {
                    intent.putExtra(MODE, mode.VEHICLES_LIST.name());
                } else if (currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
                    intent = new Intent(CreateVehicleActivity.this, PassActivity.class);
                    intent.putExtra(MODE, mode.CREATE_PASS.name());
                    intent.putExtra("Driver", driver);
                    intent.putExtra("Vehicle", vehicle);
                } else {
                    intent.putExtra(MODE, mode.VEHICLES.name());
                }
                startActivity(intent);
                finish();
            }
        });
    }

    private void buildCreateBtn() {
        createVehicle.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (TextUtils.isEmpty(year.getText()) || TextUtils.isEmpty(make.getText()) ||
                        TextUtils.isEmpty(model.getText()) || TextUtils.isEmpty(license.getText())) {
                    Log.d(TAG, "onClick: All Field Empty");
                    Toast.makeText(getApplicationContext(), "Fill out all vehicle fields", Toast.LENGTH_SHORT).show();
                } else if (year.getText().length() != 4) {
                    Log.d(TAG, "onClick: !4 year");
                    Toast.makeText(getApplicationContext(), "Enter a 4 digit year", Toast.LENGTH_SHORT).show();
                } else {
                    db = new DatabaseHandlerVehicles(getApplicationContext());
                    // opens the next activity
                    if (currentMode.equals(mode.UPDATE_VEHICLE.name())) { // checks if ur updating a driver
                        intent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class);
                        intent.putExtra(MODE, mode.VEHICLES_LIST.name()); // tells the intent that it has to use the update driver list logic
                        // updates driver in database
                        Vehicle vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");
                        if (SaveData.getSync()) {
                            syncUpdateVehicle(String.valueOf(vehicle.getVehicleId()), year.getText().toString(), make.getText().toString(), model.getText().toString(), String.valueOf(state.getSelectedItemPosition()), String.valueOf(mSelectedColorCal0), license.getText().toString());
                        }
                        db.updateVehicle(vehicle.getVehicleId(), Integer.valueOf(year.getText().toString()), make.getText().toString(), model.getText().toString(), state.getSelectedItem().toString(), String.valueOf(mSelectedColorCal0), license.getText().toString());
                    } else if (currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
                        intent = new Intent(CreateVehicleActivity.this, PassActivity.class);
                        intent.putExtra(MODE, mode.CREATE_PASS.name()); // tells the intent that it has to use the update driver list logic
                        Vehicle v2 = new Vehicle(vehicle.getVehicleId(), make.getText().toString(), model.getText().toString(), Integer.parseInt(year.getText().toString()), String.valueOf(mSelectedColorCal0), state.getSelectedItem().toString(), license.getText().toString());
                        intent.putExtra("Driver", driver);
                        intent.putExtra("Vehicle", v2);
                        if (SaveData.getSync()) {
                            syncUpdateVehicle(String.valueOf(vehicle.getVehicleId()), year.getText().toString(), make.getText().toString(), model.getText().toString(), String.valueOf(state.getSelectedItemPosition()), String.valueOf(mSelectedColorCal0), license.getText().toString());
                        }
                        // updates vehicle in database
                        db.updateVehicle(vehicle.getVehicleId(), Integer.valueOf(year.getText().toString()), make.getText().toString(), model.getText().toString(), state.getSelectedItem().toString(), String.valueOf(mSelectedColorCal0), license.getText().toString());
                    } else { // if not Updating then u are creating a driver
                        if (pastIntent.getStringExtra("Old").equals(mode.VEHICLES_LIST.name())) { // checks if the old intent was the vehicle or vehicle_list
                            intent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class); // it was the drivers list go back to the drivers list
                            intent.putExtra(MODE, mode.VEHICLES_LIST.name());
                        } else { // was not the vehicle list createVehicle driver and move to pass
                            intent = new Intent(CreateVehicleActivity.this, PassActivity.class);
                            intent.putExtra(MODE, mode.CREATE_PASS.name());
                        }
                        // add new vehicle
                        if (saveInfo.isChecked()) {
                            if (SaveData.getSync()) {
                                syncNewVehicle(year.getText().toString(), make.getText().toString(), model.getText().toString(), String.valueOf(state.getSelectedItemPosition()), String.valueOf(mSelectedColorCal0), license.getText().toString());
                            } else {
                                db.addVehicle(Integer.valueOf(year.getText().toString()), make.getText().toString(), model.getText().toString(), state.getSelectedItem().toString(), String.valueOf(mSelectedColorCal0), license.getText().toString());
                            }
                            ArrayList<Vehicle> vehicles = db.getVehicles();
                            intent.putExtra("Vehicle", vehicles.get(vehicles.size() - 1));
                        } else {
                            intent.putExtra(MODE, mode.CREATE_PASS.name());
                            Vehicle tempVehicle = new Vehicle(-1, make.getText().toString(), model.getText().toString(), Integer.parseInt(year.getText().toString()), String.valueOf(mSelectedColorCal0), state.getSelectedItem().toString(), license.getText().toString());
                            intent.putExtra("Vehicle", tempVehicle);
                        }
                    }
                    startActivity(intent);
                }
            }
        }));
    }

    public void startActivity(Intent in) {
        in.putExtra("Driver", pastIntent.getSerializableExtra("Driver"));
        super.startActivity(in);
        finish();
    }

    private void buildBannerText() {
        currentMode = pastIntent.getStringExtra(MODE);
        if (currentMode.equals(mode.UPDATE_VEHICLE.name()) || currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
            setTitle("Update Vehicle");
            buildUpdateDriver();
            createVehicle.setText("Update");
        } else {
            setTitle("Create New Vehicle");
        }
    }

    private void buildCehckBox() {
        saveInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (currentMode.equals(mode.UPDATE_VEHICLE.name()) || currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
                        createVehicle.setText(R.string.save_vehicle);
                    } else {
                        createVehicle.setText(R.string.create_vehicle);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Vehicle will not be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildColorBox() {
        int[] mColor = Utils.ColorUtils.colorChoice(getApplicationContext());
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateVehicleActivity.this);
                alertDialog.setTitle("Delete Driver?");
                alertDialog.setMessage(vehicle.getCarInfo());
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(CreateVehicleActivity.this, VehiclesActivity.class);
                        myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                        if (SaveData.getSync()) {
                            SendInfoVehicle s = new SendInfoVehicle();
                            s.deleteVehicle(String.valueOf(vehicle.getVehicleId()));
                        }
                        // delete driver from database
                        db.deleteVehicle(String.valueOf(vehicle.getVehicleId()));
                        new DatabaseHandlerPasses(context).deleteRequestVehicleID(String.valueOf(vehicle.getVehicleId()));
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

    private void buildUpdateDriver() {
        make.setText(vehicle.getMake());
        model.setText(vehicle.getModel());
        year.setText(String.valueOf(vehicle.getYear()));
        state.setSelection(States.getPosition(vehicle.getVehicleState()));
        int color = Integer.valueOf(vehicle.getColor());
        mSelectedColorCal0 = Integer.valueOf(vehicle.getColor());
        colorBox.setBackgroundColor(color);
        colorBox.setHintTextColor(color);
        license.setText(vehicle.getLicensePlate());
        saveInfo.setChecked(true);
        saveInfo.setEnabled(false);

    }

    public synchronized void syncNewVehicle(String year, String make, String model, String state, String color, String license) {
        String newID = "-400";
        SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
        JSONObject json;
        int oldID = db.addVehicle(Integer.parseInt(year), make, model, arrayOfStates[Integer.parseInt(state)].valueOf(arrayOfStates[Integer.parseInt(state)].name()).toString(), color, license);
            /*json = */
        sendInfoVehicle.addVehicle(oldID, make, model, year, state, color, license);
//        String flag = json.getString("FLAG");
        return;
    }

    public String syncUpdateVehicle(String id, String year, String make, String model, String state, String color, String license) {
        String flag = "-1";
        SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
        sendInfoVehicle.updateVehicle(id, make, model, year, state, color, license);
        return flag;
    }
}
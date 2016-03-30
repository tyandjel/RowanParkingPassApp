package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerDialog;
import com.example.android.rowanparkingpass.utilities.colorpicker.ColorPickerSwatch;
import com.example.android.rowanparkingpass.utilities.colorpicker.Utils;

public class CreateVehicleActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_vehicle);

        setupUI(findViewById(R.id.parent));

        Intent pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);

        if (currentMode.equals(mode.UPDATE_VEHICLE.name())) {
            setTitle("Update Vehicle");
        } else {
            setTitle("Create New Vehicle");
        }

        make = (EditText) findViewById(R.id.vehicleMakeEditText);
        model = (EditText) findViewById(R.id.modelEditText);
        year = (EditText) findViewById(R.id.yearEditText);
        colorBox = (EditText) findViewById(R.id.vehicleColorEditText);
        Spinner state = (Spinner) findViewById(R.id.vehicleSpinner);
        license = (EditText) findViewById(R.id.licenseEditText);
        saveInfo = (CheckBox) findViewById(R.id.saveVehicleInfoOnPhoneCheckBox);

        Button cancel = (Button) findViewById(R.id.cancelDriverButton);
        final Button createVehicle = (Button) findViewById(R.id.createVehicleButton);

        state.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values()));

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

    @Override
    public void onClick(View view) {
        // Check to see if user filled out all fields
        if (TextUtils.isEmpty(make.getText()) || TextUtils.isEmpty(model.getText()) ||
                TextUtils.isEmpty(year.getText()) || TextUtils.isEmpty(colorBox.getText()) ||
                TextUtils.isEmpty(license.getText())) {
            Toast.makeText(this, "Fill out all vehicle fields", Toast.LENGTH_SHORT);
        } else if (year.getText().length() != 4) {
            Toast.makeText(this, "Enter a 4 digit year", Toast.LENGTH_SHORT);
        } else {
            // Change to new activity
            Intent myIntent;
            switch (view.getId()) {
                case R.id.cancelVehicleButton:
                    Toast.makeText(this, "Cancel was clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.createVehicleButton:
                    Toast.makeText(this, "Create was clicked", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            // Go back to past activity
            myIntent = new Intent(this, VehiclesActivity.class);
            if (currentMode.equals(mode.VEHICLES.name())) {
                myIntent.putExtra(MODE, mode.VEHICLES.name());
            } else {
                myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
            }
            myIntent.putExtra(TEMP_VEHICLE, saveInfo.isChecked());
            startActivity(myIntent);
            finish();
        }
    }
}
package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

<<<<<<< HEAD
import com.example.android.rowanparkingpass.Activities.ListViewActivities.ListActivity;
=======
import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.States;

public class CreateDriverActivity extends BaseActivity implements View.OnClickListener {

    private static final String TEMP_DRIVER = "temp";

    EditText fullName;
    EditText street;
    EditText city;
    Spinner state;
    EditText zipCode;
    CheckBox saveInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_driver);

        Intent pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);

        if(currentMode.equals(mode.UPDATE_DRIVER.name())){
            setTitle("Update Driver");
        }else{
            setTitle("Create New Driver");
        }

        fullName = (EditText) findViewById(R.id.fullNameEditText);
        street = (EditText) findViewById(R.id.streetEditText);
        city = (EditText) findViewById(R.id.cityEditText);
        state = (Spinner) findViewById(R.id.driverSpinner);
        zipCode = (EditText) findViewById(R.id.zipCodeEditText);
        saveInfo = (CheckBox) findViewById(R.id.saveInfoOnPhoneCheckBox);

        Button cancel = (Button) findViewById(R.id.cancelDriverButton);
        final Button createDriver = (Button) findViewById(R.id.createDriverButton);

        state.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values()));

        cancel.setOnClickListener(this);
        createDriver.setOnClickListener(this);

        saveInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (currentMode!=null && currentMode.equals(mode.UPDATE_DRIVER.name())) {
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
                startActivity(myIntent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        // Check to see if user filled out all fields
        if (TextUtils.isEmpty(fullName.getText()) || TextUtils.isEmpty(street.getText()) ||
                TextUtils.isEmpty(city.getText()) || TextUtils.isEmpty(zipCode.getText())) {
            Toast.makeText(this, "Fill out all driver fields", Toast.LENGTH_SHORT);
        } else if (zipCode.getText().length() != 5) {
            Toast.makeText(this, "Enter a 4 digit zip code", Toast.LENGTH_SHORT);
        } else {
            // Change to new activity
            Intent myIntent;
            switch (v.getId()) {
                case R.id.cancelDriverButton:
                    Toast.makeText(this, "Cancel was clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.createDriverButton:
                    Toast.makeText(this, "Create was clicked", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            // Go back to past activity
            myIntent = new Intent(this, DriversActivity.class);
            if (currentMode.equals(mode.DRIVERS.name())) {
                myIntent.putExtra(MODE, mode.DRIVERS.name());
            } else {
                myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
            }
            myIntent.putExtra(TEMP_DRIVER, saveInfo.isChecked());
            startActivity(myIntent);
            finish();
        }
    }


}
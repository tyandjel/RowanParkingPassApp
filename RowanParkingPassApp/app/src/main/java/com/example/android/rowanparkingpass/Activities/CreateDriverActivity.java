package com.example.android.rowanparkingpass.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.utilities.PermissionUtils;
import com.example.android.rowanparkingpass.utilities.SavedData.SaveData;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.States;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateDriverActivity extends BaseActivity {

    private static final String TEMP_DRIVER = "temp";

    EditText fullName;
    EditText street;
    EditText city;
    Spinner state;
    EditText zipCode;
    ImageButton imageView;
    CheckBox saveInfo;
    DatabaseHandlerDrivers db;
    Intent pastIntent;
    Driver driver;
    Vehicle vehicle;
    Context context;
    States[] arrayOfStates = States.values();
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final String FILE_NAME = "temp.jpg";
    static final int REQUEST_IMAGE_CAPTURE = 1;



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
        imageView = (ImageButton) findViewById(R.id.driver_image_button);

        driver = (Driver) pastIntent.getSerializableExtra("Driver");
        vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");



        //picture call
        imageView.setOnClickListener(new View.OnClickListener() {
                                         public void onClick(View view) {
                                    pickedCameraStuff();
                                         }
                                     });

        //new Tests();

        Button cancel = (Button) findViewById(R.id.cancelDriverButton);

        // Change to new activity
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateDriverActivity.this, DriversActivity.class);
                if (currentMode.equals(mode.UPDATE_DRIVER.name())) {
                    intent.putExtra(MODE, mode.DRIVERS_LIST.name());
                } else if (currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
                    intent = new Intent(CreateDriverActivity.this, PassActivity.class);
                    intent.putExtra(MODE, mode.CREATE_PASS.name());
                    intent.putExtra("Driver", driver);
                    intent.putExtra("Vehicle", vehicle);
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
                        if (SaveData.getSync()) {
                            syncUpdateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), String.valueOf(state.getSelectedItemPosition()), zipCode.getText().toString());
                        }
                        // updates driver in database
                        db.updateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                        finish();
                    } else if (currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
                        intent = new Intent(CreateDriverActivity.this, PassActivity.class);
                        intent.putExtra(MODE, mode.CREATE_PASS.name()); // tells the intent that it has to use the update driver list logic
                        Driver d = new Driver(driver.getDriverId(), fullName.getText().toString(), "", street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                        intent.putExtra("Driver", d);
                        intent.putExtra("Vehicle", vehicle);
                        if (SaveData.getSync()) {
                            syncUpdateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), String.valueOf(state.getSelectedItemPosition()), zipCode.getText().toString());
                        }
                        // updates driver in database
                        db.updateDriver(String.valueOf(driver.getDriverId()), fullName.getText().toString(), street.getText().toString(), city.getText().toString(), String.valueOf(state.getSelectedItemPosition()), zipCode.getText().toString());
                    } else { // if not Updating then u are creating a driver
                        if (pastIntent.getStringExtra("Old").equals(mode.DRIVERS_LIST.name())) { // checks if the old intent was the DRivers or driver_list
                            intent = new Intent(CreateDriverActivity.this, DriversActivity.class); // it was the drivers list go back to the drivers list
                            intent.putExtra(MODE, mode.DRIVERS_LIST.name());
                        } else { // was no the drivers list create driver and move to vehicle list
                            intent = new Intent(CreateDriverActivity.this, VehiclesActivity.class);
                            intent.putExtra(MODE, mode.VEHICLES.name());
                        }
                        // add new driver
                        if (saveInfo.isChecked()) {
                            if (SaveData.getSync()) {
                                syncNewDriver(fullName.getText().toString(), street.getText().toString(), city.getText().toString(), String.valueOf(state.getSelectedItemPosition()), zipCode.getText().toString());
                            } else {
                                db.addDriver(fullName.getText().toString(), street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                            }

                            ArrayList<Driver> drivers = db.getDrivers();
                            intent.putExtra("Driver", drivers.get(drivers.size() - 1)); // gets newest driver just made in teh database to send
                        } else {
                            intent.putExtra(MODE, mode.VEHICLES.name());
                            Driver tempDriver = new Driver(-1, fullName.getText().toString(), "", street.getText().toString(), city.getText().toString(), state.getSelectedItem().toString(), zipCode.getText().toString());
                            intent.putExtra("Driver", tempDriver);
                        }
                    }

                    startActivity(intent);
                    finish();
                }
            }
        });
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, States.values());
        state.setAdapter(spinnerAdapter);
        if (currentMode.equals(mode.UPDATE_DRIVER.name()) || currentMode.equals(mode.UPDATE_PASS_DRIVER.name())) {
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
                        if (SaveData.getSync()) {
                            SendInfoDriver s = new SendInfoDriver();
                            s.deleteDriver(String.valueOf(driver.getDriverId()));
                        }
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

    public String syncUpdateDriver(String id, String name, String street, String city, String state, String zip) {
        String flag = "-1";
        SendInfoDriver sendInfoDriver = new SendInfoDriver();
        sendInfoDriver.updateDriver(id, name, street, city, state, zip);
        return flag;

    }

    /**
     * @param name
     * @param street
     * @param city
     * @param state
     * @param zip    returns a string of the id or -400 if there was an error
     */
    public synchronized void syncNewDriver(String name, String street, String city, String state, String zip) {
        SendInfoDriver sendInfoDriver = new SendInfoDriver();
        JSONObject json;
        int oldID = db.addDriver(name, street, city, arrayOfStates[Integer.parseInt(state)].valueOf(arrayOfStates[Integer.parseInt(state)].name()).toString(), zip);
            /*json = */
        sendInfoDriver.addDriver(oldID, name, street, city, state, zip);
//        String flag = json.getString("FLAG");
        return;
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
// ===================== camera Stuff
    private void pickedCameraStuff(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CreateDriverActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();
                    }
                });
        builder.create().show();
    }
    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                GALLERY_IMAGE_REQUEST);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

}
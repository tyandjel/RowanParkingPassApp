package com.example.android.rowanparkingpass.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.ListActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PassActivity extends BaseActivity implements View.OnClickListener {

    private DatabaseHandlerDrivers dbDriverss;
    private DatabaseHandlerVehicles dbVehicles;

    //private Spinner visitorList;
    //private Spinner vehicleList;

    private ArrayAdapter<Driver> driverArrayAdapter;
    private ArrayAdapter<Vehicle> vehicleArrayAdapter;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private EditText startDate;
    private EditText endDate;

    //    private Button newVisitor;
//    private Button newVehicle;
    private Button createPass;
    private Button mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        //Get this from database
        Driver[] drivers = new Driver[]{new Driver(0, "Tyler", "Andjel", "13 Yorktown Dr.", "Shamong", "NJ", "08088"),
                new Driver(1, "John", "Doe", "1 Main St.", "Main", "NJ", "08088")};
        //Get this from database
        Vehicle[] vehicles = new Vehicle[]{new Vehicle(1, "Hyandai", "Sonnata", 2007, "Gray", "NJ", "DBA-TBA"),
                new Vehicle(2, "Ford", "Mustang", 2015, "Red", "PA", "DTP-TXV")};

        //visitorList = (Spinner) findViewById(R.id.createdriverspinner);
        //vehicleList = (Spinner) findViewById(R.id.createvehiclespinner);

        driverArrayAdapter = new ArrayAdapter<Driver>(this, android.R.layout.simple_spinner_item, drivers);
        vehicleArrayAdapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_spinner_item, vehicles);

        //visitorList.setAdapter(driverArrayAdapter);
        //vehicleList.setAdapter(vehicleArrayAdapter);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        startDate = (EditText) findViewById(R.id.createstartdatefield);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(this);

        endDate = (EditText) findViewById(R.id.createenddatefield);
        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);


//        newVisitor = (Button) findViewById(R.id.createnewdriverbtn);
//        newVehicle = (Button) findViewById(R.id.createnewvehiclebtn);
        createPass = (Button) findViewById(R.id.createPassButton);
        mainMenu = (Button) findViewById(R.id.goMainMenuButton);

//        newVisitor.setOnClickListener(this);
//        newVehicle.setOnClickListener(this);
        createPass.setOnClickListener(this);
        mainMenu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
//        if (view == newVisitor) {
//            //TODO change so it knows to go to show drivers
//            intent = new Intent(getApplicationContext(), ListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        } else if (view == newVehicle) {
//            //TODO change so it knows to go to show vehicles
//            intent = new Intent(getApplicationContext(), ListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
       /* } else*/
        if (view == startDate) {
            // Select Pass Start Date using a date selector
            startDatePickerDialog.show();
        } else if (view == endDate) {
            // Select Pass End Date using a date selector
            endDatePickerDialog.show();
        } else if (view == createPass) {
            //TODO add pass to remote database, send email, and may be able to get rid of createdPass
//            Pass createdPass = new Pass(driverArrayAdapter.getItem(visitorList.getSelectedItemPosition()),
//                    vehicleArrayAdapter.getItem(vehicleList.getSelectedItemPosition()),
//                    startDate.getText().toString(), endDate.getText().toString());
            intent = new Intent(getApplicationContext(), ListActivity.class);
//            intent.putExtra("pass", createdPass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (view == mainMenu) {
            // Goes back to main menu
            intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
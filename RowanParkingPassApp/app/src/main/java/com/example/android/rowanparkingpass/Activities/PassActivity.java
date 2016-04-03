package com.example.android.rowanparkingpass.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.PassesActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PassActivity extends BaseActivity implements View.OnClickListener {

    private Intent pastIntent;
    private Pass pass;
    private Driver driver;
    private Vehicle vehicle;

    private View driverView;
    private View vehicleView;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private EditText startDate;
    private EditText endDate;

    private Button createPass;
    private Button mainMenu;

    private DatabaseHandlerPasses db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);

        db = new DatabaseHandlerPasses(getApplicationContext());

        pass = (Pass) pastIntent.getSerializableExtra("Pass");
        if (pass == null) {
            driver = (Driver) pastIntent.getSerializableExtra("Driver");
            vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");
        } else {
            driver = pass.getDriver();
            vehicle = pass.getVehicle();
        }

        setDriverView();
        setVehicleView();

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
        calendar.set(year, month, day);

        startDate.setText(dateFormatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        endDate.setText(dateFormatter.format(calendar.getTime()));


        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
                try {
                    Date d = newDate.getTime();
                    Date d2 = dateFormatter.parse(endDate.getText().toString());
                    if (d.after(d2)) {
                        newDate.add(Calendar.DAY_OF_MONTH, 1);
                        endDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

            }
        }, year, month, day);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
                try {
                    Date d = newDate.getTime();
                    Date d2 = dateFormatter.parse(startDate.getText().toString());
                    if (d.before(d2)) {
                        newDate.add(Calendar.DAY_OF_MONTH, -1);
                        startDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }, year, month, day);

        createPass = (Button) findViewById(R.id.createPassButton);
        mainMenu = (Button) findViewById(R.id.goMainMenuButton);

        createPass.setOnClickListener(this);
        mainMenu.setOnClickListener(this);
    }

    public void setDriverView() {
        driverView = findViewById(R.id.driver_view);
        driverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassActivity.this, DriversActivity.class);
                intent.putExtra(MODE, mode.DRIVERS.name());
                startActivity(intent);
                finish();
            }
        });
        driverView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(PassActivity.this, CreateDriverActivity.class);
                intent.putExtra(MODE, mode.UPDATE_PASS_DRIVER.name());
                intent.putExtra("Driver", (Serializable) driver);
                intent.putExtra("Vehicle", (Serializable) vehicle);
                startActivity(intent);
                finish();
                return true;
            }
        });
        // Set up text views for driver info
        TextView newDriver = (TextView) findViewById(R.id.new_visitor_text_view);
        TextView name = (TextView) findViewById(R.id.driver_text_view);
        TextView driverAddress = (TextView) findViewById(R.id.address_text_view);
        TextView driverTownCity = (TextView) findViewById(R.id.town_city_text_view);
        newDriver.setText("");
        name.setText(driver.getName());
        driverAddress.setText(driver.getStreet());
        driverTownCity.setText(driver.getTown() + ", " + driver.getState() + " " + driver.getZipCode());
    }

    public void setVehicleView() {
        vehicleView = findViewById(R.id.vehicle_view);
        vehicleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassActivity.this, VehiclesActivity.class);
                intent.putExtra(MODE, mode.UPDATE_PASS_VEHICLE.name());
                intent.putExtra("Driver", (Serializable) driver);
                startActivity(intent);
                finish();
            }
        });
        vehicleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(PassActivity.this, CreateVehicleActivity.class);
                intent.putExtra(MODE, mode.UPDATE_PASS_VEHICLE.name());
                intent.putExtra("Driver", (Serializable) driver);
                intent.putExtra("Vehicle", (Serializable) vehicle);
                startActivity(intent);
                finish();
                return true;
            }
        });
        // Set up text views for vehicle info
        TextView newVehicle = (TextView) findViewById(R.id.new_vehicle_text_view);
        TextView carText = (TextView) findViewById(R.id.car_text_view);
        TextView plateText = (TextView) findViewById(R.id.plate_text_view);
        TextView carColor = (TextView) findViewById(R.id.car_color);
        newVehicle.setText("");
        carText.setText(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
        plateText.setText(vehicle.getVehicleState() + " " + vehicle.getLicensePlate());
        carColor.setText("");
        carColor.setTextColor(Integer.parseInt(vehicle.getColor()));
        carColor.setBackgroundColor(Integer.parseInt(vehicle.getColor()));

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view == startDate) {
            // Select Pass Start Date using a date selector
            startDatePickerDialog.show();
        } else if (view == endDate) {
            // Select Pass End Date using a date selector
            endDatePickerDialog.show();
        } else {
            if (view == createPass) {
                //TODO add pass to remote database, send email
                Pass createdPass = new Pass(1, driver, vehicle, startDate.getText().toString(), endDate.getText().toString());
                db.deleteRequestDriverIDVehicleID(String.valueOf(createdPass.getDriver().getDriverId()), String.valueOf(createdPass.getVehicle().getVehicleId()));
                db.addRequest(createdPass.getRequestID(), createdPass.getVehicle().getVehicleId(), createdPass.getDriver().getDriverId(), createdPass.getFromDate(), createdPass.getToDate());
                intent = new Intent(getApplicationContext(), PassesActivity.class);
                intent.putExtra(MODE, mode.HOME_PAGE.name());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (view == mainMenu) {
                // Goes back to main menu
                intent = new Intent(getApplicationContext(), PassesActivity.class);
                intent.putExtra(MODE, mode.HOME_PAGE.name());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

}
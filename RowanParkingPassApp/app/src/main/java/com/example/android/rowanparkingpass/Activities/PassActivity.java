package com.example.android.rowanparkingpass.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.PassesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PassActivity extends BaseActivity implements View.OnClickListener {

    Intent pastIntent;
    Driver driver;
    Vehicle vehicle;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private EditText startDate;
    private EditText endDate;

    private Button createPass;
    private Button mainMenu;

    private DatabaseHandlerPasses dbPasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);

        dbPasses = new DatabaseHandlerPasses(getApplicationContext());

//        driver = (Driver) pastIntent.getSerializableExtra("Driver");
//        vehicle = (Vehicle) pastIntent.getSerializableExtra("Vehicle");
        driver = new Driver(1, "Tyler", "Andjel", "13 Yorktown Dr.", "Shamong", "New Jersey", "08088");
        vehicle = new Vehicle(1, "Hyndai", "Sonota", 2007, "0", "New Jersey", "125ABC");

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

        createPass = (Button) findViewById(R.id.createPassButton);
        mainMenu = (Button) findViewById(R.id.goMainMenuButton);

        createPass.setOnClickListener(this);
        mainMenu.setOnClickListener(this);

    }

    public void setDriverView() {
        TextView newDriver = (TextView) findViewById(R.id.new_visitor_text_view);
        TextView name = (TextView) findViewById(R.id.driver_text_view);
        TextView driverAddress = (TextView) findViewById(R.id.address_text_view);
        TextView driverTownCity = (TextView) findViewById(R.id.town_city_text_view);
        newDriver.setText("");
        name.setText(driver.getName());
        driverAddress.setText(driver.getStreet());
        driverTownCity.setText(driver.getTown() + "," + driver.getState() + " " + driver.getZipCode());
    }

    public void setVehicleView() {
        TextView newVehicle = (TextView) findViewById(R.id.new_vehicle_text_view);
        TextView carText = (TextView) findViewById(R.id.car_text_view);
        TextView plateText = (TextView) findViewById(R.id.plate_text_view);
        newVehicle.setText("");
        carText.setText(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel() + " " + vehicle.getColor());
        plateText.setText(vehicle.getLicensePlate());

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
        } else if (view == createPass) {
            //TODO add pass to remote database, send email
            if (TextUtils.isEmpty(startDate.getText()) || TextUtils.isEmpty(endDate.getText())) {
                Toast.makeText(getApplicationContext(), "You must select a start and end date.", Toast.LENGTH_SHORT).show();
            } else {
                Pass createdPass = new Pass(driver, vehicle, startDate.getText().toString(), endDate.getText().toString());
                dbPasses.addRequest(1, createdPass.getDriver().getDriverId(), createdPass.getVehicle().getVehicleId(), createdPass.getFromDate(), createdPass.getToDate());
                intent = new Intent(getApplicationContext(), PassesActivity.class);
                intent.putExtra(MODE, mode.HOME_PAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else if (view == mainMenu) {
            // Goes back to main menu
            intent = new Intent(getApplicationContext(), PassesActivity.class);
            intent.putExtra(MODE, mode.HOME_PAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
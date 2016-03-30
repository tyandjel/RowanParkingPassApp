package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.Activities.LoginPageActivity;
import com.example.android.rowanparkingpass.Activities.SettingActivity;
import com.example.android.rowanparkingpass.R;

public class ListActivity extends BaseActivity {
    protected Intent pastIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);


        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        if (currentMode != null) {
            if (currentMode.equals(mode.DRIVERS.name())) {
                setTitle("Select a Driver");
            } else if (currentMode.equals(mode.DRIVERS_LIST.name())) {
                setTitle("Your Drivers");
            } else if (currentMode.equals(mode.VEHICLES.name())) {
                setTitle("Select a Vehicle");
            } else if (currentMode.equals(mode.VEHICLES_LIST.name())) {
                setTitle("Your Vehicles");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (currentMode != null) {
            if (currentMode.equals(mode.HOME_PAGE.name())) {
                inflater.inflate(R.menu.menu_home_page, menu);
            } else if (currentMode.equals(mode.DRIVERS.name()) || currentMode.equals(mode.VEHICLES.name())) {
                inflater.inflate(R.menu.menu_vehicles_drivers_page, menu);
            } else if (currentMode.equals(mode.DRIVERS_LIST.name()) || currentMode.equals(mode.VEHICLES_LIST.name())) {
                inflater.inflate(R.menu.menu_search, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent myIntent;

        switch (item.getItemId()) {
            // action with ID action_drivers was selected
            case R.id.action_home_page:
                myIntent = new Intent(this, PassesActivity.class);
                myIntent.putExtra(MODE, mode.HOME_PAGE.name());
                startActivity(myIntent);
                finish();
                break;
            case R.id.action_drivers:
                Toast.makeText(this, "Drivers selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, DriversActivity.class);
                myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_vehicles was selected
            case R.id.action_vehicles:
                Toast.makeText(this, "Vehicles selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, VehiclesActivity.class);
                myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, SettingActivity.class);
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_logout was selected
            case R.id.action_logout:
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, LoginPageActivity.class);
                startActivity(myIntent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }
}

package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.Activities.LoginPageActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.Activities.SettingActivity;

public class ListActivity extends BaseActivity {

    protected ListView listView;
    Intent currentIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        currentIntent = getIntent();
        currentMode = currentIntent.getStringExtra(MODE);
        listView = (ListView) findViewById(R.id.listView);
        /*
        if (currentMode.equals(mode.HOME_PAGE.name())) {
            DatabaseHandlerPasses db = new DatabaseHandlerPasses(this);
            ArrayList<Pass> listOfPasses = db.getRequestDetails();
            listView.setAdapter(new ListViewArrayAdapter(listOfPasses, this, R.layout.view_recent_pass, currentMode));
        } else if (currentMode.equals(mode.DRIVERS.name()) || currentMode.equals(mode.DRIVERS_LIST.name())) {
//TODO Fix why no table created
//            DatabaseHandlerDrivers db = new DatabaseHandlerDrivers(this);
//            ArrayList<Driver> listOfDrivers = db.getDrivers();
//            listView.setAdapter(new ListViewArrayAdapter(listOfDrivers, this, R.layout.view_driver, currentMode));
        } else if (currentMode.equals(mode.VEHICLES.name()) || currentMode.equals(mode.VEHICLES_LIST.name())) {
//            DatabaseHandlerVehicles db = new DatabaseHandlerVehicles(getApplicationContext());
//            ArrayList<Vehicle> listOfVehicles = db.getVehicles();
//            listView.setAdapter(new ListViewArrayAdapter(listOfVehicles, this, R.layout.view_vehicle, currentMode));
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(currentMode!=null) {
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
            case R.id.action_drivers:
                Toast.makeText(this, "Drivers selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, ListActivity.class);
                myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
                startActivity(myIntent);
                break;
            // action with ID action_vehicles was selected
            case R.id.action_vehicles:
                Toast.makeText(this, "Vehicles selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, ListActivity.class);
                myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                startActivity(myIntent);
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, SettingActivity.class);
                startActivity(myIntent);
                break;
            // action with ID action_logout was selected
            case R.id.action_logout:
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, LoginPageActivity.class);
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_search was selected
            case R.id.action_search:
                Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT).show();
                break;
            // action with ID action_home was selected
            case R.id.action_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, ListActivity.class);
                myIntent.putExtra(MODE, mode.HOME_PAGE.name());
                startActivity(myIntent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }


}

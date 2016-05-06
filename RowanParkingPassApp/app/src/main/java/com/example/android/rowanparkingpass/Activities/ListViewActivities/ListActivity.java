package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.ArrayAdapter.ListViewArrayAdapter;
import com.example.android.rowanparkingpass.Activities.LoginPageActivity;
import com.example.android.rowanparkingpass.Activities.SettingActivity;
import com.example.android.rowanparkingpass.R;

public abstract class ListActivity extends BaseActivity {
    protected Intent pastIntent;
    ListView listView;
    ListViewArrayAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // Sets the title depending on  the current mode
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        if (currentMode != null) {
            if (currentMode.equals(mode.HOME_PAGE.name())) {
                setTitle("Your Passes");
            } else if (currentMode.equals(mode.PASS_SEARCH.name())) {
                setTitle("Find Passes");
            } else if (currentMode.equals(mode.DRIVERS.name()) || currentMode.equals(mode.UPDATE_PASS_DRIVERS.name())) {
                setTitle("Select a Driver");
            } else if (currentMode.equals(mode.DRIVERS_LIST.name())) {
                setTitle("Your Drivers");
            } else if (currentMode.equals(mode.VEHICLES.name()) || currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
                setTitle("Select a Vehicle");
            } else if (currentMode.equals(mode.VEHICLES_LIST.name())) {
                setTitle("Your Vehicles");
            }
        }
        // wait for Child to be built
    }


    public void loaded() {
        // Checks if the listView has finished loading in and then tells the adapter so it can stop animating things
        ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adapter.setHasLoaded(true);
                Log.d("has Loaded", "List");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (currentMode != null) {
            if (currentMode.equals(mode.HOME_PAGE.name())) {
                inflater.inflate(R.menu.menu_home_page, menu);
            } else if (currentMode.equals(mode.PASS_SEARCH.name())) {
                inflater.inflate(R.menu.menu_search_home, menu);
            } else if (currentMode.equals(mode.DRIVERS.name()) || currentMode.equals(mode.VEHICLES.name())) {
                inflater.inflate(R.menu.menu_vehicles_drivers_page, menu);
            } else if (currentMode.equals(mode.DRIVERS_LIST.name()) || currentMode.equals(mode.VEHICLES_LIST.name())) {
                inflater.inflate(R.menu.menu_vehicles_drivers_page, menu);
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
                myIntent = new Intent(this, DriversActivity.class);
                myIntent.putExtra(MODE, mode.DRIVERS_LIST.name());
                startActivity(myIntent);
                leftToRightTransition();
                finish();
                break;
            // action with ID action_vehicles was selected
            case R.id.action_vehicles:
                myIntent = new Intent(this, VehiclesActivity.class);
                myIntent.putExtra(MODE, mode.VEHICLES_LIST.name());
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                myIntent = new Intent(this, SettingActivity.class);
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_logout was selected
            case R.id.action_logout:
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

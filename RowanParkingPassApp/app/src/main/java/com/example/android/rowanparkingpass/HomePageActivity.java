package com.example.android.rowanparkingpass;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HomePageActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_page, menu);
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
                myIntent.putExtra(MODE, mode.DRIVERS_LIST);
                startActivity(myIntent);
                finish();
                break;
            // action with ID action_vehicles was selected
            case R.id.action_vehicles:
                Toast.makeText(this, "Vehicles selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, ListActivity.class);
                myIntent.putExtra(MODE, mode.VEHICLES_LIST);
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

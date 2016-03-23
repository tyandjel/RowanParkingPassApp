package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.DriversActivity;
import com.example.android.rowanparkingpass.Activities.ListViewActivities.VehiclesActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.PassArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends BaseActivity {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);

        setTitle("Select a Pass");

        listView = (ListView) findViewById(R.id.listView);

        DatabaseHandlerPasses db = new DatabaseHandlerPasses(this.getApplicationContext());
        ArrayList<Pass> listOfAllPasses = db.getRequestDetails();
        ArrayList<Pass> listOfPasses = new ArrayList<>();
        for (Pass pass : listOfAllPasses) {
            if (listOfPasses.contains(pass)) {
                listOfPasses.add(pass);
            }
        }
        buildEventList(listOfPasses);
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

    public void buildEventList(List<Pass> passes) {
        final PassArrayAdapter adapter = new PassArrayAdapter(passes, this);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                Intent intent;
                if (position == 0) {
                    intent = new Intent(HomePageActivity.this, DriversActivity.class);
                    intent.putExtra(MODE, mode.DRIVERS.name());
                    startActivity(intent);
                } else {
                    intent = new Intent(HomePageActivity.this, PassActivity.class);
                    intent.putExtra(MODE, mode.CREATE_PASS.name());
                    intent.putExtra("Pass", (Serializable) adapter.getItem(position));
                    startActivity(intent);
                }
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

}
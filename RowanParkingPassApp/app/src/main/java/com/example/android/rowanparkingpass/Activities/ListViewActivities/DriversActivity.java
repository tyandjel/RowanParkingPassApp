package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.rowanparkingpass.ArrayAdapter.DriverArrayAdapter;
import com.example.android.rowanparkingpass.Activities.CreateDriverActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class DriversActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Driver> testDrivers = new ArrayList<>();
        testDrivers.add(new Driver(-1,"Test Driver 1","-1","-1","-1","-1","-1"));
        testDrivers.add(new Driver(-1,"Test Driver 2","-1","-1","-1","-1","-1"));
        buildEventList(testDrivers);
    }

    public void buildEventList(List<Driver> drivers) {
        ListView listView = (ListView) findViewById(R.id.listView);
        final DriverArrayAdapter adapter = new DriverArrayAdapter(drivers, this);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                Intent intent;
                if(position==0){
                    intent = new Intent(DriversActivity.this, CreateDriverActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(DriversActivity.this, VehiclesActivity.class);
                    intent.putExtra("Drvier", (Serializable)adapter.getItem(position));
                    startActivity(intent);
                }
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }
}

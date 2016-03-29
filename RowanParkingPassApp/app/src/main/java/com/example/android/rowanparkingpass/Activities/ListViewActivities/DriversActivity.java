package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.rowanparkingpass.Activities.CreateDriverActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.DriverArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class DriversActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //build();
    }

    public void build(){

        DatabaseHandlerDrivers db = new DatabaseHandlerDrivers(this.getApplicationContext());
        //TODO Fix why it can't find table
        ArrayList<Driver> listOfDrivers = db.getDrivers();
        listOfDrivers.add(new Driver(-1,"Test Driver 1","-1","-1","-1","-1","-1"));
        listOfDrivers.add(new Driver(-1,"Test Driver 2","-1","-1","-1","-1","-1"));
        listOfDrivers.add(new Driver(-1,"Test Driver 1","-1","-1","-1","-1","-1"));
        listOfDrivers.add(new Driver(-1,"Test Driver 2","-1","-1","-1","-1","-1"));
        listOfDrivers.add(new Driver(-1,"Test Driver 1","-1","-1","-1","-1","-1"));
        listOfDrivers.add(new Driver(-1,"Test Driver 2","-1","-1","-1","-1","-1"));

        Log.d(TAG, Arrays.asList(listOfDrivers).toString());
        buildEventList(listOfDrivers);
    }


    private void buildEventList(List<Driver> drivers) {
        ListView listView = (ListView) findViewById(R.id.listView);
        final DriverArrayAdapter adapter = new DriverArrayAdapter(drivers, this);
        listView.setAdapter(adapter);
        // Checks if the listView has finished loading in and then tells the adapter so it can stop animating things
        ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adapter.setHHasLoaded(true);
            }
        });
        // checks what item in the listview was clicked. 
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent;
                if(position==0)
                {
                    intent = new Intent(DriversActivity.this, CreateDriverActivity.class);
                    intent.putExtra(MODE, mode.CREATE_DRIVER.name());
                }
                else {
                    if(currentMode.equals(mode.DRIVERS_LIST.name())) {
                        intent = new Intent(DriversActivity.this, CreateDriverActivity.class);
                        intent.putExtra(MODE, mode.UPDATE_DRIVER.name());
                        intent.putExtra("Driver", (Serializable) adapter.getItem(position));
                    }else{
                        intent = new Intent(DriversActivity.this, VehiclesActivity.class);
                        intent.putExtra(MODE, mode.VEHICLES.name());
                    }
                    intent.putExtra("Driver", (Serializable) adapter.getItem(position));
                }
                startActivity(intent);
                        }
            };
            listView.setOnItemClickListener(mMessageClickedHandler);
        }

    }


package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.rowanparkingpass.Activities.CreateVehicleActivity;
import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.VehicleArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class VehiclesActivity extends ListActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       DatabaseHandlerVehicles db = new DatabaseHandlerVehicles(getApplicationContext());
        ArrayList<Vehicle> listOfVehicles = db.getVehicles();
        buildEventList(listOfVehicles);
        loaded();
    }

    public void buildEventList(final List<Vehicle> vehicles) {
         listView = (ListView) findViewById(R.id.listView);
        adapter = new VehicleArrayAdapter(vehicles, this);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                Intent intent;

                if (position == 0) {
                    // create vehicle was clicked
                    intent = new Intent(VehiclesActivity.this, CreateVehicleActivity.class);
                    intent.putExtra(MODE, mode.CREATE_VEHICLE.name());
                    intent.putExtra("Old", currentMode); // tells wheather the vehicle was created during create pass or not
                } else {// a premade vehicle was picked
                    intent = new Intent(VehiclesActivity.this, CreateVehicleActivity.class);
                    if (currentMode.equals(mode.VEHICLES_LIST.name())) { // User is updating driver
                        intent.putExtra(MODE, mode.UPDATE_VEHICLE.name());
                    }
                    else {// user is selecting this driver for Creating a pass
                        intent = new Intent(VehiclesActivity.this, PassActivity.class);
                        intent.putExtra(MODE,mode.CREATE_PASS.name());

                    }

                }
                intent.putExtra("Vehicle",(Serializable) adapter.getItem(position));
                intent.putExtra("Driver", pastIntent.getSerializableExtra("Driver"));
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

}






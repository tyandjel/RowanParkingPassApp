package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.rowanparkingpass.Activities.CreateDriverActivity;
import com.example.android.rowanparkingpass.Activities.CreateVehicleActivity;
import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.PassArrayAdapter;
import com.example.android.rowanparkingpass.ArrayAdapter.VehicleArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class VehiclesActivity extends ListActivity {

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            List<Vehicle> testVehicles = new ArrayList<>();
            testVehicles.add(new Vehicle(-1,"-1","-1",-1,"-1","-1","-1"));
            buildEventList(testVehicles);
        }

        public void buildEventList(List<Vehicle> vehicles) {
            //ListView listView = (ListView) findViewById(R.id.listView);
            final VehicleArrayAdapter adapter = new VehicleArrayAdapter(vehicles, this);
            listView.setAdapter(adapter);
            // Create a message handling object as an anonymous class.
            AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // Do something in response to the click
                    Intent intent;
                    if(position==0){
                        intent = new Intent(VehiclesActivity.this, CreateVehicleActivity.class);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(VehiclesActivity.this, PassActivity.class);
                        intent.putExtra("Vehicle", (Serializable)adapter.getItem(position));
                        startActivity(intent);
                    }
                }
            };
            listView.setOnItemClickListener(mMessageClickedHandler);
        }
    }



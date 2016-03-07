package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.PassArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class PassesActivity extends ListActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Pass> testPasses= new ArrayList<>();
        testPasses.add(new Pass());
        buildPassesList(testPasses);
    }

    public void buildPassesList(List<Pass> passes) {
        //ListView listView = (ListView) findViewById(R.id.listView);
        final PassArrayAdapter adapter = new PassArrayAdapter(passes, this);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                Intent intent;
                if(position==0){
                    intent = new Intent(PassesActivity.this, DriversActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(PassesActivity.this, PassActivity.class);
                    intent.putExtra("Pass", (Serializable)adapter.getItem(position));
                    startActivity(intent);
                }
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }
}

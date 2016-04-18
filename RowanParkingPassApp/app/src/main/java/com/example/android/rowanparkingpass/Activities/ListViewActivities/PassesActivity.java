package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.PassArrayAdapter;
import com.example.android.rowanparkingpass.Networking.NetworkCheck;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.Sync.SyncDrivers;
import com.example.android.rowanparkingpass.Sync.SyncVehicles;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassesActivity extends ListActivity implements SearchView.OnQueryTextListener {

    DatabaseHandlerPasses db;
    SearchView searchView;
    Intent pastIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandlerPasses(getApplicationContext());
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        buildEventList(buildList());
        //TODO had ping network back in
        try {
            if (NetworkCheck.haveNetworkConnection()/* && NetworkCheck.pingNetwork()*/ && pastIntent.getStringExtra(SYNC).equals("true")) {
                sync();
            }
        } catch (NullPointerException npe) {

        }
        loaded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_home_page, menu);
        return true;
    }

    private List<Pass> buildList() {
        ArrayList<Pass> listOfAllPasses;
        listOfAllPasses = db.getPasses();
        //new Tests();


        if (listOfAllPasses == null) {
            listOfAllPasses = new ArrayList<>();
        }
        return listOfAllPasses;
    }

    private void buildEventList(List<Pass> passes) {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new PassArrayAdapter(passes, this, false);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                if (currentMode.equals(mode.HOME_PAGE.name())) {
                    Intent intent;
                    if (position == 0) {
                        intent = new Intent(PassesActivity.this, DriversActivity.class);
                        intent.putExtra(MODE, mode.DRIVERS.name());
                        startActivity(intent);
                    } else {
                        intent = new Intent(PassesActivity.this, PassActivity.class);
                        intent.putExtra(MODE, mode.CREATE_PASS.name());
                        intent.putExtra("Pass", (Serializable) adapter.getItem(position));
                        startActivity(intent);
                    }
                }
            }
        };
        // checks what item in the listview was long clicked
        AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {

                if (position != 0) {
                    final Pass pass = (Pass) listView.getItemAtPosition(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PassesActivity.this);
                    alertDialog.setTitle("Delete Pass?");
                    alertDialog.setMessage(pass.getDriver().getName() + "\n" + pass.getVehicle().getCarInfo());
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteRequestRequestID(String.valueOf(pass.getRequestID()));
                            makeAdapter(db.getRequestDetails());
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    });
                    alertDialog.show();
                }
                return true;
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setOnItemLongClickListener(mMessageLongClickedHandler);
    }

    private void makeAdapter(List<Pass> p) {
        adapter = new PassArrayAdapter(p, this, false);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchView.requestFocus();
                break;
            case R.id.action_home:
                Intent intent = new Intent(this, PassesActivity.class);
                intent.putExtra(MODE, mode.HOME_PAGE.name());
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    private void sync() {
        SyncDrivers syncDrivers = new SyncDrivers();
        SyncVehicles syncVehicles = new SyncVehicles();
        syncDrivers.sync(getApplicationContext());
        syncVehicles.sync(getApplicationContext());
    }
}

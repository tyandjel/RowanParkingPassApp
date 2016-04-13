package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
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

import com.example.android.rowanparkingpass.Activities.CreateVehicleActivity;
import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.VehicleArrayAdapter;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerVehicles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VehiclesActivity extends ListActivity implements SearchView.OnQueryTextListener {

    DatabaseHandlerVehicles db;
    Context context;
    SearchView searchView;
    MenuItem searchMenuItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandlerVehicles(getApplicationContext());
        context = getApplicationContext();
        ArrayList<Vehicle> listOfVehicles = db.getVehicles();
        buildEventList(listOfVehicles);
        loaded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        if (currentMode.equals(mode.VEHICLES_LIST.name())) {
            inflator.inflate(R.menu.menu_search_home, menu);
        } else {
            inflator.inflate(R.menu.menu_vehicles_drivers_page, menu);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Utilities.hideSoftKeyboard(VehiclesActivity.this);
                searchView.setQuery("", false);
            }
        });
        return true;
    }

    public void buildEventList(final List<Vehicle> vehicles) {
        listView = (ListView) findViewById(R.id.listView);
        final ListView tempListView = listView;
        makeAdapter(vehicles);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Close search view if its visible
                if (searchView.isShown()) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
                // Do something in response to the click
                Intent intent;
                if (position == 0 && listView.getItemAtPosition(0) == null) {
                    // create vehicle was clicked
                    intent = new Intent(VehiclesActivity.this, CreateVehicleActivity.class);
                    intent.putExtra(MODE, mode.CREATE_VEHICLE.name());
                    intent.putExtra("Old", currentMode); // tells wheather the vehicle was created during create pass or not
                } else {// a premade vehicle was picked
                    intent = new Intent(VehiclesActivity.this, CreateVehicleActivity.class);
                    if (currentMode.equals(mode.VEHICLES_LIST.name())) { // User is updating driver
                        intent.putExtra(MODE, mode.UPDATE_VEHICLE.name());
                    } else if (currentMode.equals(mode.UPDATE_PASS_VEHICLE.name())) {
                        intent = new Intent(VehiclesActivity.this, PassActivity.class);
                        intent.putExtra("Vehicle", pastIntent.getSerializableExtra("Vehicle"));
                        intent.putExtra(MODE, mode.CREATE_PASS.name());
                    } else {// user is selecting this driver for Creating a pass
                        intent = new Intent(VehiclesActivity.this, PassActivity.class);
                        intent.putExtra(MODE, mode.CREATE_PASS.name());

                    }

                }
                intent.putExtra("Vehicle", (Serializable) adapter.getItem(position));
                intent.putExtra("Driver", pastIntent.getSerializableExtra("Driver"));
                startActivity(intent);
            }
        };
        // checks what item in the listview was long clicked
        AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {
                //Close search view if its visible
                if (searchView.isShown()) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
                if (position != 0) {
                    final Vehicle vehicle = (Vehicle) tempListView.getItemAtPosition(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VehiclesActivity.this);
                    alertDialog.setTitle("Delete Driver?");
                    alertDialog.setMessage(vehicle.getCarInfo());
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (SaveData.getSync()) {
                                SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
                                sendInfoVehicle.deleteVehicle(String.valueOf(vehicle.getVehicleId()));
                            }
                            db.deleteVehicle(String.valueOf(vehicle.getVehicleId()));
                            new DatabaseHandlerPasses(context).deleteRequestVehicleID(String.valueOf(vehicle.getVehicleId()));
                            makeAdapter(db.getVehicles());
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

    private void makeAdapter(List<Vehicle> v) {
        adapter = new VehicleArrayAdapter(v, this);
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

}






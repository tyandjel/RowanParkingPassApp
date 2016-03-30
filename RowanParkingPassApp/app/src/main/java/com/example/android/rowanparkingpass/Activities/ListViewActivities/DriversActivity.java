package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.rowanparkingpass.Activities.CreateDriverActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.DriverArrayAdapter;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerDrivers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriversActivity extends ListActivity implements SearchView.OnQueryTextListener {

    ListView listView;
    DatabaseHandlerDrivers db;
    DriverArrayAdapter adapter;
    SearchView searchView;
    MenuItem searchMenuItem;

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

    public void build() {

        db = new DatabaseHandlerDrivers(this.getApplicationContext());
        ArrayList<Driver> listOfDrivers = db.getDrivers();
        listOfDrivers.add(new Driver(-1, "Test Driver 1", "-1", "-1", "-1", "-1", "-1"));
        listOfDrivers.add(new Driver(-1, "Test Driver 2", "-1", "-1", "-1", "-1", "-1"));
        listOfDrivers.add(new Driver(-1, "Test Driver 1", "-1", "-1", "-1", "-1", "-1"));
        listOfDrivers.add(new Driver(-1, "Test Driver 2", "-1", "-1", "-1", "-1", "-1"));
        listOfDrivers.add(new Driver(-1, "Test Driver 1", "-1", "-1", "-1", "-1", "-1"));
        listOfDrivers.add(new Driver(-1, "Test Driver 2", "-1", "-1", "-1", "-1", "-1"));

        Log.d(TAG, Arrays.asList(listOfDrivers).toString());
        buildEventList(listOfDrivers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflator = getMenuInflater();
        if(currentMode.equals(mode.DRIVERS_LIST)){
            inflator.inflate(R.menu.menu_search, menu);
        }else{
            inflator.inflate(R.menu.menu_vehicles_drivers_page,menu);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void buildEventList(List<Driver> drivers) {
        final ListView listView = (ListView) findViewById(R.id.listView);
        final ListView tempListView = listView;
        adapter = new DriverArrayAdapter(drivers, this);
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
                //Close search view if its visible
                if (searchView.isShown()) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
                Intent intent;
                if (position == 0 && listView.getItemAtPosition(0) == null) {
                    intent = new Intent(DriversActivity.this, CreateDriverActivity.class);
                    intent.putExtra(MODE, mode.CREATE_DRIVER.name());
                } else {
                    if (currentMode.equals(mode.DRIVERS_LIST.name())) {
                        intent = new Intent(DriversActivity.this, CreateDriverActivity.class);
                        intent.putExtra(MODE, mode.UPDATE_DRIVER.name());
                        intent.putExtra("Driver", (Serializable) adapter.getItem(position));
                    } else {
                        intent = new Intent(DriversActivity.this, VehiclesActivity.class);
                        intent.putExtra(MODE, mode.VEHICLES.name());
                    }
                    intent.putExtra("Driver", (Serializable) adapter.getItem(position));
                }
                startActivity(intent);
            }
        };
        // checks what item in the listview was long clicked
        AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
                if (position != 0) {
                    final Driver driver = (Driver) tempListView.getItemAtPosition(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriversActivity.this);
                    alertDialog.setTitle("Delete Driver");
                    alertDialog.setMessage(driver.getName());
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteDriver(String.valueOf(driver.getDriverId()));
                            Intent intent = new Intent(DriversActivity.this, DriversActivity.class);
                            intent.putExtra(MODE, currentMode);
                            startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                searchView.requestFocus();
                break;
            case R.id.action_home:
                Intent intent = new Intent(this, PassesActivity.class);
                intent.putExtra(MODE,mode.HOME_PAGE.name());
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

}


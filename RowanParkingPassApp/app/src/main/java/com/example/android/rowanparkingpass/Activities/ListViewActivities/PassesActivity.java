package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.rowanparkingpass.Activities.PassActivity;
import com.example.android.rowanparkingpass.ArrayAdapter.PassArrayAdapter;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoPass;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;
import com.example.android.rowanparkingpass.utilities.Utilities;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class PassesActivity extends ListActivity implements SearchView.OnQueryTextListener {

    DatabaseHandlerPasses db;
    SearchView searchView;
    MenuItem searchMenuItem;
    Intent pastIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandlerPasses(getApplicationContext());
        pastIntent = getIntent();
        currentMode = pastIntent.getStringExtra(MODE);
        buildEventList(buildList());
        loaded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        if (currentMode.equals(mode.HOME_PAGE.name())) {
            inflator.inflate(R.menu.menu_home_page, menu);
        } else {
            inflator.inflate(R.menu.menu_search_home, menu);
            //Will be used for searching through passes
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
                    Utilities.hideSoftKeyboard(PassesActivity.this);
                    searchView.setQuery("", false);
                }
            });
        }
        return true;
    }

    private List<Pass> buildList() {
        ArrayList<Pass> listOfAllPasses;
        if (currentMode.equals(mode.HOME_PAGE.name())) {
            new AsyncTask<Void, JSONObject, JSONObject>(){

                @Override
                protected JSONObject doInBackground(Void... params) {
                    SendInfoVehicle sendInfoVehicle = new SendInfoVehicle();
                    JSONObject json = sendInfoVehicle.syncVehicles(getApplicationContext());
                    JSONArray jsonArray;
                    try {
                        String s = (String) json.get("JSONS");
                        s = URLDecoder.decode(s);
                        Log.d("S", s);
                        jsonArray = new JSONArray(s);
                        Log.d("JSON ARRAY", jsonArray.toString());
                        for(int i = 0; i < jsonArray.length(); i++){
                            //[{"model":"zaz","color":"1","state":"23","user_id":"10","year":"1945","license":"bingling","vehicle_id":"3","make":"me a sammich"}
                           JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            Log.d("JSONOBJ", jsonObj.toString());
                            //TODO: FOR SYNC actually call gets and put new vehicles/drivers in database
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    SendInfoDriver sendInfoDriver = new SendInfoDriver();
//                    JSONObject json = sendInfoDriver.syncVehicles(getApplicationContext());
//                    SendInfoPass sendInfoPass = new SendInfoPass();
//                    JSONObject json = sendInfoPass.addPass("1", "1", "01/22/2015", "01/23/2015");
                    //Log.d("SESSION: ", json.toString());
                    return json;
                }
            }.execute();
            listOfAllPasses = db.getPasses();
        } else {
            //TODO: Get list of Passes with current date only with just Name and Vehicle Info from server not local db
            listOfAllPasses = new ArrayList<>();
            Driver d = new Driver(-1, "Fake", "Name1", "-1", "-1", "-1", "-1");
            Driver d2 = new Driver(-2, "Fake", "Name2", "-1", "-1", "-1", "-1");
            Vehicle v = new Vehicle(-1, "Fake Make", "Fake Model", 2008, "-1", "Fake State", "ABC123");
            Vehicle v2 = new Vehicle(-1, "Fake Make", "Fake Model", 2099, "-1", "Fake State", "DEF456");
            Pass p = new Pass(-1, d, v, "Date1", "Date2");
            Pass p2 = new Pass(-2, d2, v2, "Date1", "Date2");
            listOfAllPasses.add(p);
            listOfAllPasses.add(p2);
        }
        if (listOfAllPasses == null) {
            listOfAllPasses = new ArrayList<>();
        }
        return listOfAllPasses;
    }

    private void buildEventList(List<Pass> passes) {
        listView = (ListView) findViewById(R.id.listView);
        if (currentMode.equals(mode.HOME_PAGE.name())) {
            adapter = new PassArrayAdapter(passes, this, false);
        } else {
            adapter = new PassArrayAdapter(passes, this, true);
        }
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
                } else {
                    //Close search view if its visible
                    if (searchView.isShown()) {
                        searchMenuItem.collapseActionView();
                        searchView.setQuery("", false);
                    }
                }
            }
        };
        // checks what item in the listview was long clicked
        AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {
                //Close search view if its visible
                if (currentMode.equals(mode.HOME_PAGE.name())) {
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
                } else {
                    if (searchView.isShown()) {
                        searchMenuItem.collapseActionView();
                        searchView.setQuery("", false);
                    }
                }
                return true;

            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setOnItemLongClickListener(mMessageLongClickedHandler);
    }

    private void makeAdapter(List<Pass> p) {
        if (currentMode.equals(mode.HOME_PAGE.name())) {
            adapter = new PassArrayAdapter(p, this, false);
        } else {
            adapter = new PassArrayAdapter(p, this, true);
        }
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

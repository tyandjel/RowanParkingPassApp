package com.example.android.rowanparkingpass.Activities.ListViewActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.utilities.database.DatabaseHandlerPasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassesActivity extends ListActivity implements SearchView.OnQueryTextListener {

    DatabaseHandlerPasses db;
    SearchView searchView;
    MenuItem searchMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandlerPasses(getApplicationContext());
        buildEventList(buildList());
        loaded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_home_page, menu);
        //Will be used for searching through passes
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchMenuItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) searchMenuItem.getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(false);
//        searchView.setOnQueryTextListener(this);
//        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//                Utilities.hideSoftKeyboard(PassesActivity.this);
//                searchView.setQuery("", false);
//            }
//        });
        return true;
    }

    private List<Pass> buildList() {
        ArrayList<Pass> listOfAllPasses = db.getRequestDetails();
        Log.d(TAG, "BUILD LIST");
        if (listOfAllPasses == null) {
            listOfAllPasses = new ArrayList<>();
        }
        return listOfAllPasses;
    }

    private void buildEventList(List<Pass> passes) {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new PassArrayAdapter(passes, this);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                //Close search view if its visible
//                if (searchView.isShown()) {
//                    searchMenuItem.collapseActionView();
//                    searchView.setQuery("", false);
//                }
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
        };
        // checks what item in the listview was long clicked
        AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {
                //Close search view if its visible
//                if (searchView.isShown()) {
//                    searchMenuItem.collapseActionView();
//                    searchView.setQuery("", false);
//                }
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
        adapter = new PassArrayAdapter(p, this);
        listView.setAdapter(adapter);
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

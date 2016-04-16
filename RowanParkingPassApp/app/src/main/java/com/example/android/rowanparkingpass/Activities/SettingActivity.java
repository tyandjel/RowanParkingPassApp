package com.example.android.rowanparkingpass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.rowanparkingpass.Activities.ListViewActivities.PassesActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.Sync.SyncDrivers;
import com.example.android.rowanparkingpass.Sync.SyncVehicles;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    Button changePassword;
    Button syncNow;
    Switch syncSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        changePassword = (Button) findViewById(R.id.changePassButton);
        syncNow = (Button) findViewById(R.id.syncNowButton);
        syncSwitch = (Switch) findViewById(R.id.mySwitch);

        changePassword.setOnClickListener(this);
        syncNow.setOnClickListener(this);

        syncSwitch.setChecked(SaveData.getSync());
        syncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SaveData.setSync(true);
                    Toast.makeText(SettingActivity.this, "Information will be synced with the server.",
                            Toast.LENGTH_LONG).show();
                } else {
                    SaveData.setSync(false);
                    Log.d("QUE SIZE", "" + SaveData.getQueue().size());
                    SaveData.getQueue().clear();
                    Log.d("QUE SIZE", "" + SaveData.getQueue().size());
                    Toast.makeText(SettingActivity.this, "Information not yet sent to the server will not be synced",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent myIntent;

        switch (item.getItemId()) {
            // action with ID action_drivers was selected
            case R.id.action_home:
                Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
                myIntent = new Intent(this, PassesActivity.class);
                myIntent.putExtra(MODE, mode.HOME_PAGE.name());
                startActivity(myIntent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        // Change to new activity
        switch (v.getId()) {
            case R.id.changePassButton:
                Toast.makeText(this, "Change password was clicked", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(this, RowanWebPageActivity.class);
                myIntent.putExtra(MODE, RowanWebPageActivity.mode.CHANGE_PASSWORD.name());
                startActivity(myIntent);
                break;
            case R.id.syncNowButton:
                Toast.makeText(this, "Sync now was clicked", Toast.LENGTH_SHORT).show();
                SyncDrivers syncDrivers = new SyncDrivers();
                SyncVehicles syncVehicles = new SyncVehicles();
                syncDrivers.sync(getApplicationContext());
                syncVehicles.sync(getApplicationContext());
                break;
            default:
                break;
        }
    }
}
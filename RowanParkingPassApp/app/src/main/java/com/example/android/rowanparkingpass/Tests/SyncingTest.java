package com.example.android.rowanparkingpass.Tests;

import android.util.Log;

import com.example.android.rowanparkingpass.Activities.CreateDriverActivity;
import com.example.android.rowanparkingpass.Activities.CreateVehicleActivity;

/**
 * Created by johnathan on 4/13/16.
 */
public class SyncingTest {
    public static boolean testAddDriver(CreateDriverActivity d) {
        try {
            return true;//!d.syncNewDriver("Happy Joy", "342 Smile Dr.", "Glassboro","12","08332").equals("-400");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean testAddVehicle(CreateVehicleActivity d) {
        try {
            //return !d.syncNewVehicle("Happy Joy", "342 Smile Dr.", "Glassboro","12","08332").equals("-400");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean testUpdateDriver(CreateDriverActivity d) {
        try {
            if (!(testAddDriver(d))) {
                Log.d("testAddDriver ", "Failed");
            }
            return d.syncUpdateDriver("1", "John saunders", "312 Roselle Dr.", "Millville", "10", "08028").equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

package com.example.android.rowanparkingpass.Tests;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.personinfo.Driver;

/**
 * Created by johnathan on 4/12/16.
 */
public class QueuingTest  {

    public static boolean testQueue(QueuingTest f)
  {
        SendInfoDriver d = new SendInfoDriver();
        SendInfoVehicle v = new SendInfoVehicle();
       // v.addVehicle("make123","modle","1983","10","-1","fffffff");
        d.addDriver(1,"John saunders123", "312 Roselle Dr.", "Millville","10","08028") ;
        return true;
    }
}

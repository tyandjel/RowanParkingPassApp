package com.example.android.rowanparkingpass.Tests;

import android.util.Log;

import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoDriver;
import com.example.android.rowanparkingpass.Networking.SendInfo.SendInfoVehicle;
import com.example.android.rowanparkingpass.SavedDate.SaveData;
import com.example.android.rowanparkingpass.personinfo.Driver;

/**
 * Created by johnathan on 4/12/16.
 */
public class QueuingTest {

    public static boolean testQueue(){
        try{

            SendInfoDriver d = new SendInfoDriver();
            SendInfoVehicle v = new SendInfoVehicle();
            v.addVehicle("make","modle","1983","10","blue?","fffffff");
            d.addDriver("John saunders", "312 Roselle Dr.", "Millville","10","08028") ;
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

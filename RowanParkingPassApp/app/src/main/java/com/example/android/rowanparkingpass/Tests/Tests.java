package com.example.android.rowanparkingpass.Tests;

import android.util.Log;

import com.example.android.rowanparkingpass.Activities.BaseActivity;

/**
 * Created by johnathan on 4/12/16.
 */
public class Tests extends BaseActivity {
    public Tests() {
        Log.d(String.valueOf(QueuingTest.testQueue(new QueuingTest())), "Queue test result: ");
        //testSyncDriver();
        // ReadWriteTest t = new ReadWriteTest();
        // t.testWrite();
        // t.testRead();
    }

//    public void testSyncDriver(){
//        new Intent(getApplicationContext(), CreateDriverActivity.class);
//        //CreateDriverActivity d = CreateDriverActivity.getTest();
//        Log.d("SyncAddDriver", String.valueOf(SyncingTest.testAddDriver(d)));
//        Log.d("SyncUpdateDriver", String.valueOf(SyncingTest.testUpdateDriver(d)));
//
//    }

}

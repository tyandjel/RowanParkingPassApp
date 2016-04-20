package com.example.android.rowanparkingpass.utilities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utilities {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String appendZipZero(String zip) {
        int zipLength = zip.length();
        int zerosNeeded = zipLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= zerosNeeded; i++) {
            sb.append("0");
        }
        sb.append(zip);
        return sb.toString();
    }

}

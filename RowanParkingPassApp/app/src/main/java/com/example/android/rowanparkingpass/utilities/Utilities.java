package com.example.android.rowanparkingpass.utilities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Utilities for Rowan Car Pass
 */
public class Utilities {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Appends any needed zeros for zip code. Zip code must be length of 5
     *
     * @param zip the zip code without appended zeros
     * @return zip code with any needed zeros
     */
    public static String appendZipZero(String zip) {
        int zipLength = zip.length();
        int zerosNeeded = 5 - zipLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            sb.append("0");
        }
        sb.append(zip);
        return sb.toString();
    }

}

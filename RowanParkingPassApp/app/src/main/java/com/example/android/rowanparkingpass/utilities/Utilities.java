package com.example.android.rowanparkingpass.utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        int zerosNeeded = 5 - zipLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            sb.append("0");
        }
        sb.append(zip);
        return sb.toString();
    }


    public static Bitmap byteArrayToBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options); //Convert byte array to bitmap
        return bitmap;
    }

    public static byte[] bitmapToByteArray(String file) {
        byte[] byteArray = null;
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(file)), null, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteArray;
    }


}

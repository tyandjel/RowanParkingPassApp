package com.example.android.rowanparkingpass.SavedDate;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Read and write information to/from Android
 */
public class ReadWrite implements Serializable {

    public static final String SYNC_ON_FILE = "syncOn.txt";
    public static final String ADMIN_FILE = "admin.txt";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void writeOut(SaveData saveData, String fileName, Context context) throws IOException {

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(saveData);
            os.close();
            fos.close();
        } catch (Exception e) {

        }
    }

    public static SaveData readIn(Context context, String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        SaveData saveData = (SaveData) is.readObject();
        is.close();
        fis.close();
        return saveData;
    }
}

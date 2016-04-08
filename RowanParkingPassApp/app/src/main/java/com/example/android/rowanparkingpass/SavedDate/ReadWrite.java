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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void writeOut(SaveData saveData, Context context) throws IOException {

        try (FileOutputStream fos = context.openFileOutput("Scans.ser", Context.MODE_PRIVATE)) {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(saveData);
            os.close();
            fos.close();
        }
        catch (Exception e) {

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

package com.example.android.rowanparkingpass.SavedData;

import android.content.Context;
import android.util.Log;

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
    public static final String saveDateFile = "SaveDate.ser";

    public static void WRITE_OUT(SaveUser notePad, Context context) throws IOException {

        try {
            FileOutputStream fos = context.openFileOutput("SaveData.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(notePad);
            os.close();
            fos.close();
        } catch (Exception e) {
            Log.d("ReadWriteException", e.getMessage());
        }
    }

    public static SaveUser READ_IN(Context context) throws IOException, ClassNotFoundException {

        FileInputStream fis = context.openFileInput("SaveData.ser");
        ObjectInputStream is = new ObjectInputStream(fis);
        SaveUser notePad = (SaveUser) is.readObject();
        is.close();
        fis.close();
        return notePad;
    }


}


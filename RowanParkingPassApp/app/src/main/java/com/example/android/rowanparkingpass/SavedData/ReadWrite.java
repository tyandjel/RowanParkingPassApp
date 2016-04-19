package com.example.android.rowanparkingpass.SavedData;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Savepoint;

/**
 * Read and write information to/from Android
 */



    public class ReadWrite implements Serializable {
    public static final String saveDateFile = "SaveDate.ser";
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static void WRITE_OUT(SaveUser notePad, Context context) throws IOException {

            try (FileOutputStream fos = context.openFileOutput("SaveData.ser", Context.MODE_PRIVATE)) {
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(notePad);
                os.close();
                fos.close();
            }
            catch (Exception e) {

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


package com.realgear.sudoku_thebestone.data;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer {

    public static String serialize(Object obj) {
        if(obj == null)
            return "";

        try {
            ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
            objStream.writeObject(obj);
            objStream.close();
            return Base64.encodeToString(serialObj.toByteArray(), Base64.DEFAULT);
            //return new String(serialObj.toByteArray(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            Log.e("Object Serializer", e.toString());
            return "";
        }
    }

    public static Object deserialize(String str) {
        if(str == null || str.length() == 0) {
            Log.e("Serialize", "Length is null");
            return null;
        }

        try {
            ByteArrayInputStream serialObj = new ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT));
            ObjectInputStream objStream = new ObjectInputStream(serialObj);
            return objStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

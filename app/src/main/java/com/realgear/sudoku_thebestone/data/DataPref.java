package com.realgear.sudoku_thebestone.data;

import android.content.Context;
import android.content.SharedPreferences;

public class DataPref {
    private final String KEY_PACKAGE = "com.realgear.sudoku_thematerialone";

    private SharedPreferences mSharedPrefs;
    private final Context           mContext;
    private final String            mPrefName;

    public DataPref(Context context, String prefName) {
        this.mContext = context;
        this.mPrefName = getPrefName(prefName);

        this.mSharedPrefs = mContext.getSharedPreferences(mPrefName, Context.MODE_PRIVATE);
        reloadPref();
    }

    private String getPrefName(String key) {
        return (KEY_PACKAGE + "_" + key);
    }

    public boolean getBoolean(String key) {
        return mSharedPrefs.getBoolean(key, false);
    }

    public String getString(String key) {
        return mSharedPrefs.getString(key, "");
    }

    public Integer getInt(String key) {
        return mSharedPrefs.getInt(key, -1);
    }

    public Object getObject(String key) {
        String data = getString(key);
        return deserializeObject(data);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(key);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(key);
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, Integer value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(key);
        editor.putInt(key, value);
        editor.apply();
    }

    public void putObject(String key, Object value) {
        String data = serializeObject(value);
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(key);
        editor.putString(key, data);
        editor.commit();
        editor.apply();
    }

    private String serializeObject(Object data) {
        return ObjectSerializer.serialize(data);
    }

    private Object deserializeObject(String data) {
        //Log.e("Preferences", "String Empty ? " + data.isEmpty());
        if(data == null) {
            return null;
        }
        return ObjectSerializer.deserialize(data);
    }

    public void reloadPref() {
       this.mSharedPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                mSharedPrefs = sharedPreferences;
            }
        });
    }
}

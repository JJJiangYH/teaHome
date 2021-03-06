package com.hc.bluetoothlibrary.tootl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.os.ParcelFileDescriptor.MODE_APPEND;


public class DataMemory {
    private final String collect = "collect";
    private final String parameters = "parameters";
    private final SharedPreferences sp;

    @SuppressLint("WrongConstant")
    public DataMemory(Context context) {
        sp = context.getSharedPreferences("data",
                MODE_APPEND | Context.MODE_PRIVATE);
    }

    public void saveData(String mac, String name) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(mac, name);
        editor.apply();
    }

    public String getData(String mac) {
        return sp.getString(mac, null);
    }

    public void saveCollectData(String mac, String name) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(collect + mac, name);
        editor.apply();
    }

    public String getCollectData(String mac) {
        return sp.getString(collect + mac, null);
    }

    public void saveParameters(String data) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(parameters, data);
        editor.apply();
    }

    public int getModuleLevel() {
        String level = "ModuleLevel";
        return sp.getInt(level, 0);
    }

    public String getParameters() {
        return sp.getString(parameters, null);
    }
}
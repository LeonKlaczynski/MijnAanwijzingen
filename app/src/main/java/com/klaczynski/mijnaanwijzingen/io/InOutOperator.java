package com.klaczynski.mijnaanwijzingen.io;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class InOutOperator {

    private static final String TAG = "InOutOperator"; //debug tag
    Activity activity;

    public InOutOperator(Activity activity) {
        this.activity = activity;
    }

    public void saveList(ArrayList<Aanwijzing> aanwijzingen, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(aanwijzingen);
        Log.d(TAG, "saveArrayList: Opgeslagen json: " + json);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<Aanwijzing> loadList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Log.d(TAG, "loadMap: Geladen json: " + json);
        if (json == null || json.equals("null") || json.equals("[]")) {
            Log.d(TAG, "loadList: Geen data aanwezig, genereert nieuwe lijst..");
            return new ArrayList<Aanwijzing>();
        }
        Type type = new TypeToken<ArrayList<Aanwijzing>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<Aanwijzing> loadMockJson() {
        Gson gson = new Gson();
        String json = loadJSONFromAsset();
        Log.d(TAG, "loadMap: Geladen json: " + json);
        if (json == null || json.equals("null") || json.equals("[]")) {
            Log.d(TAG, "loadMockJson: Geen data aanwezig, genereert nieuwe lijst..");
            return new ArrayList<Aanwijzing>();
        }
        Type type = new TypeToken<ArrayList<Aanwijzing>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("mockdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

package com.klaczynski.mijnaanwijzingen.io;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klaczynski.mijnaanwijzingen.MainActivity;
import com.klaczynski.mijnaanwijzingen.misc.Definitions;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class InOutOperator {

    private static final String TAG = "InOutOperator"; //debug tag
    Activity activity;
    public boolean systemUses24HourFormat = true;

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
            String dataSource = "mockdata.json";
            if(!systemUses24HourFormat)
                dataSource = "mock_us_data.json";
            InputStream is = activity.getAssets().open(dataSource);
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

    public void saveName(String name, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, name);
        editor.apply();
    }

    public void setDev(boolean state) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Definitions.DEV_KEY, state);
        editor.apply();
    }

    public boolean isDev() {
        String key = Definitions.DEV_KEY;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean(Definitions.DEV_KEY, false);
    }


    public String loadName(String key) {
        String name = "";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        name = prefs.getString(key, null);
        return name;
    }

    public ArrayList<Aanwijzing> readJsonFromStorage(Context context) {
        File file = new File(commonDocumentDirPath("Mijn Aanwijzingen") + "/" + Definitions.BACKUP_FILE_NAME);
        byte[] content = new byte[(int) file.length()];
        Gson gson = new Gson();
        String json = "";
        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(content);
            json = new String(content);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Fout opgetreden!", Toast.LENGTH_LONG).show();
        }
        if (json == null || json.equals("null") || json.equals("[]") || json.equals("")) {
            Log.d(TAG, "readJsonFromStorage: Geen data aanwezig, houdt huidige lijst..");
            return MainActivity.aanwijzingen;
        }
        Type type = new TypeToken<ArrayList<Aanwijzing>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void writeJsonToStorage(Context context, ArrayList<Aanwijzing> aanwijzingen){
        File dir = commonDocumentDirPath("Mijn Aanwijzingen");
        Gson gson = new Gson();
        String json = gson.toJson(aanwijzingen);

        try {
            File file = new File(dir, Definitions.BACKUP_FILE_NAME);
            FileWriter writer = new FileWriter(file);
            writer.append(json);
            writer.flush();
            writer.close();

            Log.d(TAG, "writeJsonToStorage: Opgeslagen json: " + json+" naar pad: "+file.getAbsolutePath());
            Toast.makeText(context, "Backup opgeslagen naar pad: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Definitions.BACKUP_SET_KEY, Definitions.BACKUP_SET_KEY);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Fout opgetreden!", Toast.LENGTH_LONG).show();
        }
    }

    public static File commonDocumentDirPath(String FolderName) { //This is terrible. Thanks Android.
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + FolderName);
        }
        else
            dir = new File(Environment.getExternalStorageDirectory() + File.separator + FolderName);

        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }
}

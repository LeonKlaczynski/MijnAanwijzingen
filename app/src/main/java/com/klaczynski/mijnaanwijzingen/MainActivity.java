package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.klaczynski.mijnaanwijzingen.io.InOutOperator;
import com.klaczynski.mijnaanwijzingen.io.MockData;
import com.klaczynski.mijnaanwijzingen.misc.Definitions;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean warningAcknowledged = false;
    public static ArrayList<Aanwijzing> aanwijzingen = new ArrayList<>();
    private InOutOperator io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        io = new InOutOperator(MainActivity.this);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (!Definitions.DEBUG)
        warningDialog(MainActivity.this);
        //MockData.addData();
        try {
            aanwijzingen = io.loadList(Definitions.LIJST_KEY);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e.fillInStackTrace());
        }

        AanwijzingenAdapter adapter = new AanwijzingenAdapter(this.getApplicationContext(), aanwijzingen);
        ListView lijst = findViewById(R.id.lijst);
        lijst.setAdapter(adapter);
        lijst.setClickable(true);
    }

    @Override
    protected void onResume() {
        //if(!warningAcknowledged) finishAffinity();
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Definitions.DEBUG)
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_disclaimer:
                warningDialog(MainActivity.this);
                break;
            case R.id.action_testActivity:
                Intent i = new Intent(this, CreationActivity.class);
                i.putExtra("TYPE", R.layout.ttv_create);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void warningDialog(Activity a) {
        AlertDialog alertDialog = new AlertDialog.Builder(a).create();
        alertDialog.setTitle("Waarschuwing");
        alertDialog.setMessage("Het gebruik van deze app is op eigen risico. De ontwikkelaar van deze app kan " +
                "niet verantwoordelijk worden gehouden voor gebruik van deze app, evenals eventuele gevolgen hiervan. " +
                "De machinist is ten alle tijden zelf verantwoordelijk voor het juist aannemen, opvolgen en bewaren van aanwijzingen");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Akkoord",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        warningAcknowledged = true;
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onStop();
    }
}
package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.klaczynski.mijnaanwijzingen.misc.Definitions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        warningDialog(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Definitions.DEBUG)
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_disclaimer:
                warningDialog(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void warningDialog(Activity a) {
        AlertDialog alertDialog = new AlertDialog.Builder(a).create();
        alertDialog.setTitle("Waarschuwing");
        alertDialog.setMessage("Het gebruik van deze app is op eigen risico. De ontwikkelaar van deze app kan " +
                "niet verantwoordelijk worden gehouden voor gebruik van deze app, evenals eventuele gevolgen hiervan. " +
                "De machinist is ten alle tijden zelf verantwoordelijk voor het juist aannemen, opvolgen en bewaren van aanwijzingen");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ik ga akkoord.",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
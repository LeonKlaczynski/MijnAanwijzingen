package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.klaczynski.mijnaanwijzingen.io.InOutOperator;

public class AboutActivity extends AppCompatActivity {
    int devCounter = 0;
    InOutOperator io;

    /**
     * Shows information about the app, including disclaimers + copyright information
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        io = new InOutOperator(AboutActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Over deze app");
        TextView versieView = findViewById(R.id.textViewVersion);
        versieView.setText("Versie " + BuildConfig.VERSION_NAME);

        //Settings
        SwitchCompat switchHints = findViewById(R.id.switchHint);
        switchHints.setChecked(MainActivity.showTextHints);
        switchHints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switchHints.isChecked()) {
                    MainActivity.showTextHints = true;
                    io.setShowHints(true);
                } else {
                    MainActivity.showTextHints = false;
                    io.setShowHints(false);
                }
            }
        });

        //Email stuff
        MaterialButton mb = findViewById(R.id.emailButton);
        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mijnaanwijzingen.dev@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Over: Mijn Aanwijzingen");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Mijn Aanwijzingen v" + BuildConfig.VERSION_NAME);
                startActivity(Intent.createChooser(emailIntent, "E-mail versturen..."));
            }
        });

        //Dev mode stuff
        ImageView iconView = findViewById(R.id.iconView);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isDev) {
                    devCounter++;
                    if (devCounter == 10) {
                        Toast.makeText(AboutActivity.this, "Je bent nu een ontwikkelaar! Start de app opnieuw op..", Toast.LENGTH_LONG).show();
                        MainActivity.isDev = true;
                        io.setDev(true);
                        finishAffinity();
                    }
                } else {
                    Toast.makeText(AboutActivity.this, "Je bent al een ontwikkelaar!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Makes return button function
     * @param item menu item
     * @return idk
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.klaczynski.mijnaanwijzingen.io.InOutOperator;

public class AboutActivity extends AppCompatActivity {
    int devCounter = 0;
    InOutOperator io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        io = new InOutOperator(AboutActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Over deze app");
        TextView versieView = findViewById(R.id.textViewVersion);
        versieView.setText("Versie " + BuildConfig.VERSION_NAME);

        MaterialButton mb = findViewById(R.id.emailButton);

        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"technischetreinchauffeur@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Over: Mijn Aanwijzingen");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Mijn Aanwijzingen v" + BuildConfig.VERSION_NAME);
                startActivity(Intent.createChooser(emailIntent, "E-mail versturen..."));
            }
        });

        ImageView iconView = findViewById(R.id.iconView);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.isDev) {
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
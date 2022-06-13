package org.treinchauffeur.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.klaczynski.mijnaanwijzingen.BuildConfig;
import com.klaczynski.mijnaanwijzingen.R;

import org.treinchauffeur.mijnaanwijzingen.io.InOutOperator;
import org.treinchauffeur.mijnaanwijzingen.misc.Definitions;

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

        TextView versieView = findViewById(R.id.textViewVersion);
        versieView.setText("Versie " + BuildConfig.VERSION_NAME + " (BETA)");

        //Settings
        MaterialCheckBox switchHints = findViewById(R.id.switchHint);
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

        //Name changing
        TextView nameView = findViewById(R.id.textViewName);
        nameView.setText("'"+MainActivity.driverName+"'");
        MaterialButton nameButton = findViewById(R.id.buttonChangeName);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameDialog();
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

    /**
     * Asks user to input name
     */
    private void nameDialog() {
        Dialog viewDialog = new Dialog(AboutActivity.this);
        viewDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        viewDialog.setContentView(R.layout.dialog_name);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewDialog.show();
        Button btnAdd = viewDialog.findViewById(R.id.buttonAdd);
        TextInputLayout editLayout = viewDialog.findViewById(R.id.editTextTextMcnName);
        EditText naamVeld = editLayout.getEditText();
        if (!MainActivity.driverName.equalsIgnoreCase(""))
            naamVeld.setText(MainActivity.driverName);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (naamVeld.getText().toString().equalsIgnoreCase("")) {
                    naamVeld.setError("Geen geldige invoer");
                    return;
                }
                io.saveName(naamVeld.getText().toString(), Definitions.NAAM_KEY);
                MainActivity.driverName = naamVeld.getText().toString();
                Snackbar.make(findViewById(R.id.aboutLayout), "Opgeslagen naam: " + naamVeld.getText().toString(), Snackbar.LENGTH_SHORT).show();
                TextView nameView = findViewById(R.id.textViewName);
                nameView.setText("'"+MainActivity.driverName+"'");
                viewDialog.dismiss();
            }
        });
    }
}
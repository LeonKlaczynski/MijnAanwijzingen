package org.treinchauffeur.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.klaczynski.mijnaanwijzingen.R;

import org.treinchauffeur.mijnaanwijzingen.obj.Aanwijzing;

import java.util.Calendar;

public class CreationActivity extends AppCompatActivity {
    int aanwijzingType = 0;

    /**
     * Ultimately adds new instruction. User input fields dependant of type (passed through Intent).
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String actionbarString = "Nieuwe aanwijzing ";
        Intent intent = getIntent();
        int TYPE = intent.getIntExtra("TYPE", R.layout.vr_create);
        setContentView(TYPE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(TYPE != 0) {
        switch(TYPE) {
            case R.layout.vr_create:
                aanwijzingType = Aanwijzing.TYPE_VR;
                actionbarString += "VR";
                break;
            case R.layout.ovw_create:
                aanwijzingType = Aanwijzing.TYPE_OVW;
                actionbarString += "OVW";
                break;
            case R.layout.sts_create:
                aanwijzingType = Aanwijzing.TYPE_STS;
                actionbarString += "STS";
                break;
            case R.layout.stsn_create:
                aanwijzingType = Aanwijzing.TYPE_STSN;
                actionbarString += "STS Normale Snelheid";
                break;
            case R.layout.sb_create:
                aanwijzingType = Aanwijzing.TYPE_SB;
                actionbarString += "SB";
                break;
            case R.layout.ttv_create:
                aanwijzingType = Aanwijzing.TYPE_TTV;
                actionbarString += "TTV";
                break;
        }
            Aanwijzing nieuweAanwijzing = new Aanwijzing(aanwijzingType, "", null, "", -1, "", "", "", "", "", "", "");
            getSupportActionBar().setTitle(actionbarString);

            //Hides all hints depending on user settings
            ConstraintLayout layout = findViewById(R.id.creationLayout);
            if(!MainActivity.showTextHints) {
                hideHints(layout);
            }

            Button acknowledgeButton = findViewById(R.id.buttonAcklowledge);
            acknowledgeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText etTrNr = findViewById(R.id.editTextTrNr);
                    EditText etTrdl = findViewById(R.id.editTextTrdl);
                    EditText etLocatie = findViewById(R.id.editTextLocation);
                    if(etTrNr.getText().toString().equalsIgnoreCase(""))
                        etTrNr.setError("Geen invoer");
                    if(etLocatie.getText().toString().equalsIgnoreCase(""))
                        etLocatie.setError("Geen invoer");
                    if(aanwijzingType != -1) {
                        switch(aanwijzingType) {
                            case Aanwijzing.TYPE_VR:
                                EditText etSnelheid = findViewById(R.id.editTextSpeed);
                                EditText etReden = findViewById(R.id.editTextReason);
                                CheckBox cbPersonnel = findViewById(R.id.checkBoxPersonnel);
                                CheckBox cbSchouw = findViewById(R.id.checkBoxSchouw);
                                if(etSnelheid.getText().toString().equalsIgnoreCase(""))
                                    etSnelheid.setError("Geen invoer");
                                if(etReden.getText().toString().equalsIgnoreCase(""))
                                    etReden.setError("Geen invoer");

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etSnelheid.getText().toString().equalsIgnoreCase("") ||
                                        etLocatie.getText().toString().equalsIgnoreCase("") || (etReden.getText().toString().equalsIgnoreCase("") && !cbPersonnel.isChecked())) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setVRsnelheid(etSnelheid.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setMiscInfo(etReden.getText().toString());
                                nieuweAanwijzing.setVRhulpverleners(cbPersonnel.isChecked());
                                nieuweAanwijzing.setVRschouw(cbSchouw.isChecked());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_OVW:
                                EditText etCrossings = findViewById(R.id.editTextCrossings);
                                if(etCrossings.getText().toString().equalsIgnoreCase(""))
                                    etCrossings.setError("Geen invoer");

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etCrossings.getText().toString().equalsIgnoreCase("") ||
                                etLocatie.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setOverwegen(etCrossings.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_SB:
                                EditText etReden2 = findViewById(R.id.editTextReason);
                                EditText etSpeed = findViewById(R.id.editTextSpeed);
                                CheckBox cbLAE = findViewById(R.id.checkBoxLAE);
                                if(etReden2.getText().toString().equalsIgnoreCase(""))
                                    etReden2.setError("Geen invoer");
                                if(etSpeed.getText().toString().equalsIgnoreCase(""))
                                    etSpeed.setError("Geen invoer");

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etReden2.getText().toString().equalsIgnoreCase("") ||
                                        etLocatie.getText().toString().equalsIgnoreCase("") || etSpeed.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setSBsnelheid(etSpeed.getText().toString());
                                nieuweAanwijzing.setSBLAE(cbLAE.isChecked());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setMiscInfo(etReden2.getText().toString());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_STS:
                                EditText etSignal = findViewById(R.id.editTextSignal);
                                if(etSignal.getText().toString().equalsIgnoreCase(""))
                                    etSignal.setError("Geen invoer");

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etLocatie.getText().toString().equalsIgnoreCase("") ||
                                        etSignal.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setSTSseinNr(etSignal.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_STSN:
                                EditText etSignal2 = findViewById(R.id.editTextSignal);
                                EditText etCrossings2 = findViewById(R.id.editTextCrossings);
                                EditText etBridges = findViewById(R.id.editTextBridges);
                                if(etSignal2.getText().toString().equalsIgnoreCase(""))
                                    etSignal2.setError("Geen invoer");
                                if(etCrossings2.getText().toString().equalsIgnoreCase(""))
                                    etCrossings2.setError("Geen invoer");
                                if(etBridges.getText().toString().equalsIgnoreCase(""))
                                    etBridges.setError("Geen invoer");


                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etLocatie.getText().toString().equalsIgnoreCase("") ||
                                        etSignal2.getText().toString().equalsIgnoreCase("") || etCrossings2.getText().toString().equalsIgnoreCase("") ||
                                etBridges.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setSTSseinNr(etSignal2.getText().toString());
                                nieuweAanwijzing.setSTSNbruggen(etBridges.getText().toString());
                                nieuweAanwijzing.setOverwegen(etCrossings2.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_TTV:
                                EditText etReden3 = findViewById(R.id.editTextReason);
                                if(etReden3.getText().toString().equalsIgnoreCase(""))
                                    etReden3.setError("Geen invoer");

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etLocatie.getText().toString().equalsIgnoreCase("") ||
                                        etReden3.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setMiscInfo(etReden3.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setNaamMcn(MainActivity.driverName);
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;
                        }
                    }
                }
            });
        }
    }

    /**
     * Loops through layout to find edittexts, in order to disable hints. This took me a while..
     * @param parent ViewGroup parent to loop through children
     */
    public void hideHints(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                hideHints((ViewGroup) child);
            } else {
                if (child != null) {
                    if(child instanceof EditText) {
                        ((EditText) child).setHint("");
                    }
                }
            }
        }
    }

    /**
     * Makes return button work
     * @param item
     * @return
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
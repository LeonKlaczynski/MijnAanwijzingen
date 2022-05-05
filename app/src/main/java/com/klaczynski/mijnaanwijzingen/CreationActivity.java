package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.util.Calendar;

public class CreationActivity extends AppCompatActivity {
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String actionbarString = "Nieuwe aanwijzing ";
        Intent i = getIntent();
        int TYPE = i.getIntExtra("TYPE", R.layout.vr_create);
        setContentView(TYPE);

        if(TYPE != 0) {
        switch(TYPE) {
            case R.layout.vr_create:
                type = Aanwijzing.TYPE_VR;
                actionbarString += "VR";
                break;
            case R.layout.ovw_create:
                type = Aanwijzing.TYPE_OVW;
                actionbarString += "OVW";
                break;
            case R.layout.sts_create:
                type = Aanwijzing.TYPE_STS;
                actionbarString += "STS";
                break;
            case R.layout.stsn_create:
                type = Aanwijzing.TYPE_STSN;
                actionbarString += "STS Normale Snelheid";
                break;
            case R.layout.sb_create:
                type = Aanwijzing.TYPE_SB;
                actionbarString += "SB";
                break;
            case R.layout.ttv_create:
                type = Aanwijzing.TYPE_TTV;
                actionbarString += "TTV";
                break;
        }
            Aanwijzing nieuweAanwijzing = new Aanwijzing(type, "", null, "", -1, "", "", "", "", "", "", "");
            getSupportActionBar().setTitle(actionbarString);

            Button acknowledgeButton = findViewById(R.id.buttonAcklowledge);

            acknowledgeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText etTrNr = findViewById(R.id.editTextTrNr);
                    EditText etTrdl = findViewById(R.id.editTextTrdl);
                    EditText etLocatie = findViewById(R.id.editTextLocation);
                    if(type != -1) {
                        switch(type) {
                            case Aanwijzing.TYPE_VR:
                                EditText etSnelheid = findViewById(R.id.editTextSpeed);
                                EditText etReden = findViewById(R.id.editTextReason);
                                CheckBox cbPersonnel = findViewById(R.id.checkBoxPersonnel);
                                CheckBox cbSchouw = findViewById(R.id.checkBoxSchouw);

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
                                nieuweAanwijzing.setMiscInfo((cbPersonnel.isChecked() ? "Hulpverleners aanwezig. " : "")+etReden.getText().toString());
                                nieuweAanwijzing.setVRschouw(cbSchouw.isChecked());
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_OVW:
                                EditText etCrossings = findViewById(R.id.editTextCrossings);

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
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_SB:
                                EditText etReden2 = findViewById(R.id.editTextReason);
                                EditText etSpeed = findViewById(R.id.editTextSpeed);
                                CheckBox cbLAE = findViewById(R.id.checkBoxLAE);

                                if(etTrNr.getText().toString().equalsIgnoreCase("") || etReden2.getText().toString().equalsIgnoreCase("") ||
                                        etLocatie.getText().toString().equalsIgnoreCase("") || etSpeed.getText().toString().equalsIgnoreCase("")) {
                                    Snackbar.make(view, "Niet alle gegevens zijn correct ingevuld!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                nieuweAanwijzing.setDatum(Calendar.getInstance().getTime());
                                nieuweAanwijzing.setTreinNr(Integer.parseInt(etTrNr.getText().toString()));
                                nieuweAanwijzing.setTrdl(etTrdl.getText().toString());
                                nieuweAanwijzing.setSBsnelheid(etSpeed.getText().toString());
                                nieuweAanwijzing.setLocatie(etLocatie.getText().toString());
                                nieuweAanwijzing.setMiscInfo((cbLAE.isChecked() ? "L/A/E wel geplaatst. " : "L/A/E niet geplaatst. ")+etReden2.getText().toString());
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_STS:
                                EditText etSignal = findViewById(R.id.editTextSignal);

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
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_STSN:
                                EditText etSignal2 = findViewById(R.id.editTextSignal);
                                EditText etCrossings2 = findViewById(R.id.editTextCrossings);
                                EditText etBridges = findViewById(R.id.editTextBridges);

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
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;

                            case Aanwijzing.TYPE_TTV:
                                EditText etReden3 = findViewById(R.id.editTextReason);

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
                                MainActivity.aanwijzingen.add(0, nieuweAanwijzing);
                                finish();
                                break;
                        }
                    }
                }
            });
        }
    }
}
package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.klaczynski.mijnaanwijzingen.io.InOutOperator;
import com.klaczynski.mijnaanwijzingen.io.MockData;
import com.klaczynski.mijnaanwijzingen.misc.Definitions;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static ArrayList<Aanwijzing> aanwijzingen;
    private InOutOperator io;
    public static String driverName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        io = new InOutOperator(MainActivity.this);
        io.twentyFourHourFormat = DateFormat.is24HourFormat(getApplicationContext()); //This is stupid but necessary because Americans exist
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (io.loadName(Definitions.NAAM_KEY) == null || io.loadName(Definitions.NAAM_KEY).equals(""))
            warningDialog(MainActivity.this);
        else
            driverName = io.loadName(Definitions.NAAM_KEY);

        try {
            aanwijzingen = io.loadList(Definitions.LIJST_KEY);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e.fillInStackTrace());
            aanwijzingen = new ArrayList<Aanwijzing>();
        }

        AanwijzingenAdapter adapter = new AanwijzingenAdapter(MainActivity.this, aanwijzingen);
        ListView lijst = findViewById(R.id.lijst);
        lijst.setAdapter(adapter);
        lijst.setClickable(true);

        lijst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Aanwijzing a = aanwijzingen.get(pos);
                showDialog(a);
            }
        });

        lijst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                deletionDialog(pos);

                return true;
            }
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(driverName.equalsIgnoreCase("")) {
                    nameDialog();
                    return;
                }

                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.aanwijzingen_menu);
                dialog.show();

                Button buttonVR = dialog.findViewById(R.id.buttonVR);
                buttonVR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.vr_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonOVW = dialog.findViewById(R.id.buttonOVW);
                buttonOVW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.ovw_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSB = dialog.findViewById(R.id.buttonSB);
                buttonSB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.sb_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSTS = dialog.findViewById(R.id.buttonSTS);
                buttonSTS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.sts_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSTSN = dialog.findViewById(R.id.buttonSTSN);
                buttonSTSN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getRootView().getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.stsn_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonTTV = dialog.findViewById(R.id.buttonTTV);
                buttonTTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.ttv_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setColorSchemeResources(R.color.primary); //can't be done in XML apparently
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateView();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        updateView();
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (!Definitions.DEBUG)
            menu.findItem(R.id.action_MockData).setVisible(false);
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
            case R.id.action_clearData:
                deleteAllDialog();
                break;
            case R.id.action_MockData:
                aanwijzingen = io.loadMockJson();
                updateView();
                io.saveList(aanwijzingen, Definitions.LIJST_KEY);
                break;
            case R.id.action_name:
                nameDialog();
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
                        dialog.dismiss();
                        if (io.loadName(Definitions.NAAM_KEY) == null || io.loadName(Definitions.NAAM_KEY).equals(""))
                            nameDialog();
                    }
                });
        alertDialog.show();
    }

    private void nameDialog() {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.name_dialog);
        viewDialog.show();
        Button btnAdd = viewDialog.findViewById(R.id.buttonAdd);
        EditText naamVeld = viewDialog.findViewById(R.id.editTextTextMcnName);
        if (!driverName.equalsIgnoreCase(""))
            naamVeld.setText(driverName);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (naamVeld.getText().toString().equalsIgnoreCase("")) {
                    naamVeld.setError("Geen geldige invoer");
                    return;
                }
                io.saveName(naamVeld.getText().toString(), Definitions.NAAM_KEY);
                driverName = naamVeld.getText().toString();
                Snackbar.make(findViewById(R.id.lijst), "Opgeslagen naam: " + naamVeld.getText().toString(), Snackbar.LENGTH_SHORT).show();
                viewDialog.dismiss();
            }
        });
    }

    private void showDialog(Aanwijzing a) {
        Dialog viewDialog = new Dialog(MainActivity.this);
        switch (a.getType()) {
            case Aanwijzing.TYPE_VR:
                viewDialog.setContentView(R.layout.vr_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewVR = viewDialog.findViewById(R.id.editTextTrNrVR);
                TextView trdlViewVR = viewDialog.findViewById(R.id.editTextTrdlVR);
                TextView locationViewVR = viewDialog.findViewById(R.id.editTextLocationVR);
                TextView speedVR = viewDialog.findViewById(R.id.editTextSpeedVR);
                TextView reasonVR = viewDialog.findViewById(R.id.editTextReasonVR);
                CheckBox cbSchouwVR = viewDialog.findViewById(R.id.checkBoxSchouwVR);
                CheckBox cbPersonnelVR = viewDialog.findViewById(R.id.checkBoxPersonnelVR);
                TextView nameVR = viewDialog.findViewById(R.id.editTextNameDriverVR);
                TextView dateVR = viewDialog.findViewById(R.id.editTextDateVR);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateString = format.format(a.getDatum());
                trNrViewVR.setText(Integer.toString(a.getTreinNr()));
                trdlViewVR.setText(a.getTrdl());
                locationViewVR.setText(a.getLocatie());
                speedVR.setText(a.getVRsnelheid());
                reasonVR.setText(a.getMiscInfo());
                cbSchouwVR.setChecked(a.getVRschouw());
                cbPersonnelVR.setChecked(a.getVRhulpverleners());
                nameVR.setText(a.getNaamMcn());
                dateVR.setText(dateString);

                break;
            case Aanwijzing.TYPE_OVW:
                viewDialog.setContentView(R.layout.ovw_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewOVW = viewDialog.findViewById(R.id.editTextTrNr);
                TextView trdlViewOVW = viewDialog.findViewById(R.id.editTextTrdl);
                TextView locationViewOVW = viewDialog.findViewById(R.id.editTextLocation);
                TextView crossingsOVW = viewDialog.findViewById(R.id.editTextCrossings);
                TextView nameOVW = viewDialog.findViewById(R.id.editTextNameDriver);
                TextView dateOVW = viewDialog.findViewById(R.id.editTextDate);
                SimpleDateFormat formatOVW = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateStringOVW = formatOVW.format(a.getDatum());
                trNrViewOVW.setText(Integer.toString(a.getTreinNr()));
                trdlViewOVW.setText(a.getTrdl());
                locationViewOVW.setText(a.getLocatie());
                crossingsOVW.setText(a.getOverwegen());
                nameOVW.setText(a.getNaamMcn());
                dateOVW.setText(dateStringOVW);
                break;
            case Aanwijzing.TYPE_SB:
                viewDialog.setContentView(R.layout.sb_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewSB = viewDialog.findViewById(R.id.editTextTrNr);
                TextView trdlViewSB = viewDialog.findViewById(R.id.editTextTrdl);
                TextView locationViewSB = viewDialog.findViewById(R.id.editTextLocation);
                TextView reasonSB = viewDialog.findViewById(R.id.editTextReason);
                TextView speedSB = viewDialog.findViewById(R.id.editTextSpeed);
                TextView nameSB = viewDialog.findViewById(R.id.editTextNameDriver);
                TextView dateSB = viewDialog.findViewById(R.id.editTextDate);
                CheckBox cbLAE = viewDialog.findViewById(R.id.checkBoxLAE);
                SimpleDateFormat formatSB = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateStringSB = formatSB.format(a.getDatum());
                trNrViewSB.setText(Integer.toString(a.getTreinNr()));
                trdlViewSB.setText(a.getTrdl());
                locationViewSB.setText(a.getLocatie());
                speedSB.setText(a.getSBsnelheid());
                cbLAE.setChecked(a.getLAE());
                reasonSB.setText(a.getMiscInfo());
                nameSB.setText(a.getNaamMcn());
                dateSB.setText(dateStringSB);
                break;
            case Aanwijzing.TYPE_STS:
                viewDialog.setContentView(R.layout.sts_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewSTS = viewDialog.findViewById(R.id.editTextTrNr);
                TextView trdlViewSTS = viewDialog.findViewById(R.id.editTextTrdl);
                TextView locationViewSTS = viewDialog.findViewById(R.id.editTextLocation);
                TextView signalSTS = viewDialog.findViewById(R.id.editTextSignal);
                TextView nameSTS = viewDialog.findViewById(R.id.editTextNameDriver);
                TextView dateSTS = viewDialog.findViewById(R.id.editTextDate);
                SimpleDateFormat formatSTS = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateStringSTS = formatSTS.format(a.getDatum());
                trNrViewSTS.setText(Integer.toString(a.getTreinNr()));
                trdlViewSTS.setText(a.getTrdl());
                locationViewSTS.setText(a.getLocatie());
                signalSTS.setText(a.getSTSseinNr());
                nameSTS.setText(a.getNaamMcn());
                dateSTS.setText(dateStringSTS);
                break;

            case Aanwijzing.TYPE_STSN:
                viewDialog.setContentView(R.layout.stsn_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewSTSN = viewDialog.findViewById(R.id.editTextTrNr);
                TextView trdlViewSTSN = viewDialog.findViewById(R.id.editTextTrdl);
                TextView locationViewSTSN = viewDialog.findViewById(R.id.editTextLocation);
                TextView signalSTSN = viewDialog.findViewById(R.id.editTextSignal);
                TextView crossingsSTSN = viewDialog.findViewById(R.id.editTextCrossings);
                TextView bridgesSTSN = viewDialog.findViewById(R.id.editTextBridges);
                TextView nameSTSN = viewDialog.findViewById(R.id.editTextNameDriver);
                TextView dateSTSN = viewDialog.findViewById(R.id.editTextDate);
                SimpleDateFormat formatSTSN = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateStringSTSN = formatSTSN.format(a.getDatum());
                trNrViewSTSN.setText(Integer.toString(a.getTreinNr()));
                trdlViewSTSN.setText(a.getTrdl());
                locationViewSTSN.setText(a.getLocatie());
                signalSTSN.setText(a.getSTSseinNr());
                crossingsSTSN.setText(a.getOverwegen());
                bridgesSTSN.setText(a.getSTSNbruggen());
                nameSTSN.setText(a.getNaamMcn());
                dateSTSN.setText(dateStringSTSN);
                break;
            case Aanwijzing.TYPE_TTV:
                viewDialog.setContentView(R.layout.ttv_view);
                viewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                viewDialog.show();

                TextView trNrViewTTV = viewDialog.findViewById(R.id.editTextTrNr);
                TextView trdlViewTTV = viewDialog.findViewById(R.id.editTextTrdl);
                TextView locationViewTTV = viewDialog.findViewById(R.id.editTextLocation);
                TextView reasonTTV = viewDialog.findViewById(R.id.editTextReason);
                TextView nameTTV = viewDialog.findViewById(R.id.editTextNameDriver);
                TextView dateTTV = viewDialog.findViewById(R.id.editTextDate);
                SimpleDateFormat formatTTV = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateStringTTV = formatTTV.format(a.getDatum());
                trNrViewTTV.setText(Integer.toString(a.getTreinNr()));
                trdlViewTTV.setText(a.getTrdl());
                locationViewTTV.setText(a.getLocatie());
                reasonTTV.setText(a.getMiscInfo());
                nameTTV.setText(a.getNaamMcn());
                dateTTV.setText(dateStringTTV);
                break;
        }
    }

    private void deletionDialog(int pos) {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.deletion_dialog);
        viewDialog.show();
        Button btnYes = viewDialog.findViewById(R.id.buttonYes);
        Button btnNo = viewDialog.findViewById(R.id.buttonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aanwijzingen.remove(pos);
                updateView();
                viewDialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
            }
        });
    }

    private void deleteAllDialog() {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.delete_all_dialog);
        viewDialog.show();
        Button btnYes = viewDialog.findViewById(R.id.buttonYes);
        Button btnNo = viewDialog.findViewById(R.id.buttonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aanwijzingen = new ArrayList<>();
                updateView();
                io.saveList(aanwijzingen, Definitions.LIJST_KEY);
                viewDialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
            }
        });
    }


    public void updateView() {
        ListView lijst = findViewById(R.id.lijst);
        ArrayList<Aanwijzing> aTemp = new ArrayList<>();
        aTemp.addAll(aanwijzingen);
        ((ArrayAdapter) lijst.getAdapter()).clear();
        ((ArrayAdapter) lijst.getAdapter()).addAll(aTemp);
        ((AanwijzingenAdapter) lijst.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onStop();
    }
}
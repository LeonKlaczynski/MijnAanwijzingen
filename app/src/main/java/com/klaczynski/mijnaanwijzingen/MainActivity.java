package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.klaczynski.mijnaanwijzingen.io.InOutOperator;
import com.klaczynski.mijnaanwijzingen.misc.Definitions;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Logcat TAG
    private static final String TAG = "MainActivity";

    public static ArrayList<Aanwijzing> aanwijzingen;
    private InOutOperator io;
    public static String driverName = "";
    public static boolean isDev;


    /**
     * Draws interface (see individual annotations)
     * @param savedInstanceState
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        io = new InOutOperator(MainActivity.this);
        io.systemUses24HourFormat = DateFormat.is24HourFormat(getApplicationContext()); //This is stupid but necessary because Americans exist
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_mijnaanwijzingen_icon_white_foreground);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Determining whether dev or not
        isDev = io.isDev();
        TextView devView = findViewById(R.id.devView);
        if(isDev) devView.setVisibility(View.VISIBLE);

        //loading name, if not asking for one
        if (io.loadName(Definitions.NAAM_KEY) == null || io.loadName(Definitions.NAAM_KEY).equals(""))
            warningDialog(MainActivity.this);
        else
            driverName = io.loadName(Definitions.NAAM_KEY);

        //Defining list of aanwijzingen, if none existant -> create new
        try {
            aanwijzingen = io.loadList(Definitions.LIJST_KEY);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e.fillInStackTrace());
            aanwijzingen = new ArrayList<Aanwijzing>();
        }

        //Defining list + add items + add click listeners
        AanwijzingenAdapter adapter = new AanwijzingenAdapter(MainActivity.this, aanwijzingen);
        ListView lijst = findViewById(R.id.lijst);
        lijst.setAdapter(adapter);
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

        //Fab that launches dialog to create new Aanwijzing (pick a type)
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driverName.equalsIgnoreCase("")) {
                    nameDialog();
                    return;
                }

                Dialog dialog = new Dialog(view.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        //Enabling pull down to refresh
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

    /**
     * Refresh data when activity resumes
     */
    @Override
    protected void onResume() {
        updateView();
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onResume();
    }

    /**
     * Hides certain meny entries when not dev
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (Definitions.DEBUG || isDev)
            menu.findItem(R.id.action_MockData).setVisible(true);
        if(Definitions.DEBUG || isDev)
            menu.findItem(R.id.action_revokeDev).setVisible(true);
        return true;
    }

    /**
     * Sets menu item actions
     * @param item
     * @return
     */
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
            /*case R.id.action_write_backup:
                verifyStoragePermissions(this);
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
                io.writeJsonToStorage(MainActivity.this, aanwijzingen);
                break;
            case R.id.action_read_backup:
                aanwijzingen = io.readJsonFromStorage(MainActivity.this);
                updateView();
                io.saveList(aanwijzingen, Definitions.LIJST_KEY);
                break;*/ //Prepping for backups
            case R.id.action_about:
                Intent i = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(i);
                break;
            case R.id.action_revokeDev:
                isDev = false;
                io.setDev(false);
                Toast.makeText(MainActivity.this, "Je bent geen ontwikkelaar meer! Start de app opnieuw op..", Toast.LENGTH_LONG).show();
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * warning dialog with operational disclaimer
     * @param a
     */
    public void warningDialog(Activity a) {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.dialog_disclaimer);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewDialog.show();
        Button btnCheck = viewDialog.findViewById(R.id.buttonCheck);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
                if (io.loadName(Definitions.NAAM_KEY) == null || io.loadName(Definitions.NAAM_KEY).equals(""))
                    nameDialog();
            }
        });
    }

    /**
     * Asks user to input name
     */
    private void nameDialog() {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.name_dialog);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    /**
     * Shows Aanwijzing data when list item Onclick is called
     * @param a
     */
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

    /**
     * Verifies whether user wants to erase list entry
     * @param pos
     */
    private void deletionDialog(int pos) {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.dialog_delete);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    /**
     * Verifies whether user wants to delete all entries
     */
    private void deleteAllDialog() {
        Dialog viewDialog = new Dialog(MainActivity.this);
        viewDialog.setContentView(R.layout.dialog_delete_all);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    /**
     * Updates the list
     */
    public void updateView() {
        ListView lijst = findViewById(R.id.lijst);
        ArrayList<Aanwijzing> aTemp = new ArrayList<>();
        aTemp.addAll(aanwijzingen);
        ((ArrayAdapter) lijst.getAdapter()).clear();
        ((ArrayAdapter) lijst.getAdapter()).addAll(aTemp);
        ((AanwijzingenAdapter) lijst.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Makes sure to save list when app is stopped gracefully
     */
    @Override
    protected void onStop() {
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onStop();
    }

}
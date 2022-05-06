package com.klaczynski.mijnaanwijzingen;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AanwijzingenAdapter extends ArrayAdapter<Aanwijzing> {
    private static final String TAG = "AanwijzingenAdapter";

    public AanwijzingenAdapter(Context context, ArrayList<Aanwijzing> aanwijzingen) {
        super(context, 0, aanwijzingen);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Aanwijzing a = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //Defining fields
        TextView locationView = convertView.findViewById(R.id.textViewLocation);
        TextView mainInfoView = convertView.findViewById(R.id.textViewMainInfo);
        TextView dateView = convertView.findViewById(R.id.textViewDate);
        ImageView icon = convertView.findViewById(R.id.imageView);
        LinearLayout layout = (LinearLayout) convertView;

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(a);
                Log.d(TAG, "onClick: clicked item: "+position);
            }
        });

        //Setting general information regardless of type
        locationView.setText(a.getLocatie());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = format.format(a.getDatum());
        dateView.setText(dateString);

        //Setting type-specific data
        switch (a.getType()) {
            case Aanwijzing.TYPE_VR:
                icon.setImageResource(R.drawable.ic_aanwvr);
                layout.setBackgroundResource(R.color.VRlight);
                mainInfoView.setText("Snelheid: " + a.getVRsnelheid() + ". Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_OVW:
                icon.setImageResource(R.drawable.ic_aanwovw);
                layout.setBackgroundResource(R.color.OVWlight);
                mainInfoView.setText("Overwegen: " + a.getOverwegen() + ".");
                break;
            case Aanwijzing.TYPE_SB:
                icon.setImageResource(R.drawable.ic_aanwsb);
                layout.setBackgroundResource(R.color.SBlight);
                mainInfoView.setText("Snelheid: " + a.getSBsnelheid() + ". Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_STS:
                icon.setImageResource(R.drawable.ic_aanwsts);
                layout.setBackgroundResource(R.color.STSlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr());
                break;
            case Aanwijzing.TYPE_STSN:
                icon.setImageResource(R.drawable.ic_aanwstsn);
                layout.setBackgroundResource(R.color.STSNlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr() + ". Overwegen: " + a.getOverwegen() + ". Bruggen: " + a.getSTSNbruggen());
                break;
            case Aanwijzing.TYPE_TTV:
                icon.setImageResource(R.drawable.ic_aanwttv);
                layout.setBackgroundResource(R.color.TTVlight);
                mainInfoView.setText("Reden: " + a.getMiscInfo());
                break;
        }
        return convertView;
    }

    private void showDialog(Aanwijzing a) {
        Dialog viewDialog = new Dialog(getContext());
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
}

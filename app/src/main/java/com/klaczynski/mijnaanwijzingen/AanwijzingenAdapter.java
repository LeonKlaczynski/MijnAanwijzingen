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

        /*layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(a);
                Log.d(TAG, "onClick: clicked item: "+position);
            }
        });*/

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

}

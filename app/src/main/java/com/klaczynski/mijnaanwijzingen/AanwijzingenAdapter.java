package com.klaczynski.mijnaanwijzingen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        ConstraintLayout cl = convertView.findViewById(R.id.item_layout);

        //Setting general information regardless of type
        locationView.setText(a.getLocatie());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(a.getDatum());
        dateView.setText(dateString);

        //Setting type-specific data
        switch (a.getType()) {
            case Aanwijzing.TYPE_VR:
                icon.setImageResource(R.drawable.ic_aanwvr);
                cl.setBackgroundResource(R.color.VRlight);
                mainInfoView.setText("Snelheid: " + a.getVRsnelheid() + ". Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_OVW:
                icon.setImageResource(R.drawable.ic_aanwovw);
                cl.setBackgroundResource(R.color.OVWlight);
                mainInfoView.setText("Overwegen: " + a.getOverwegen() + ".");
                break;
            case Aanwijzing.TYPE_SB:
                icon.setImageResource(R.drawable.ic_aanwsb);
                cl.setBackgroundResource(R.color.SBlight);
                mainInfoView.setText("Snelheid: " + a.getSBsnelheid() + ". Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_STS:
                icon.setImageResource(R.drawable.ic_aanwsts);
                cl.setBackgroundResource(R.color.STSlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr());
                break;
            case Aanwijzing.TYPE_STSN:
                icon.setImageResource(R.drawable.ic_aanwstsn);
                cl.setBackgroundResource(R.color.STSNlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr() + ". Overwegen: " + a.getOverwegen() + ". Bruggen: " + a.getSTSNbruggen());
                break;
            case Aanwijzing.TYPE_TTV:
                icon.setImageResource(R.drawable.ic_aanwttv);
                cl.setBackgroundResource(R.color.TTVlight);
                mainInfoView.setText("Reden: " + a.getMiscInfo());
                break;
        }
        return convertView;
    }
}

package org.treinchauffeur.mijnaanwijzingen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.klaczynski.mijnaanwijzingen.R;

import org.treinchauffeur.mijnaanwijzingen.obj.Aanwijzing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AanwijzingenAdapter extends ArrayAdapter<Aanwijzing> {
    private static final String TAG = "AanwijzingenAdapter";

    /**
     * Custom list adapter to display all saved instructions
     * @param context
     * @param aanwijzingen list of all instructions
     */
    public AanwijzingenAdapter(Context context, ArrayList<Aanwijzing> aanwijzingen) {
        super(context, 0, aanwijzingen);
    }

    /**
     * Creates view, sets lines to data of instruction
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        Aanwijzing a = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //Defining fields
        TextView locationView = convertView.findViewById(R.id.textViewLocation);
        TextView mainInfoView = convertView.findViewById(R.id.textViewMainInfo);
        TextView dateView = convertView.findViewById(R.id.textViewDate);
        TextView icon = convertView.findViewById(R.id.typeView);
        LinearLayout layout = convertView.findViewById(R.id.item_layout_bg);
        MaterialCardView cardView = convertView.findViewById(R.id.lijstCardView);

        //Setting general information regardless of type
        locationView.setText(a.getLocatie());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = format.format(a.getDatum());
        dateView.setText(dateString);

        //Setting type-specific data
        switch (a.getType()) {
            case Aanwijzing.TYPE_VR:
                icon.setBackgroundResource(R.color.VR);
                cardView.setRippleColorResource(R.color.VR);
                icon.setText("VR");
                layout.setBackgroundResource(R.color.VRlight);
                mainInfoView.setText("Snelheid: " + a.getVRsnelheid() + " km/h. Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_OVW:
                icon.setBackgroundResource(R.color.OVW);
                cardView.setRippleColorResource(R.color.OVW);
                icon.setText("OVW");
                layout.setBackgroundResource(R.color.OVWlight);
                mainInfoView.setText("Overwegen: " + a.getOverwegen() + ".");
                break;
            case Aanwijzing.TYPE_SB:
                icon.setBackgroundResource(R.color.SB);
                cardView.setRippleColorResource(R.color.SB);
                icon.setText("SB");
                layout.setBackgroundResource(R.color.SBlight);
                mainInfoView.setText("Snelheid: " + a.getSBsnelheid() + " km/h. Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_STS:
                icon.setBackgroundResource(R.color.STS);
                cardView.setRippleColorResource(R.color.STS);
                icon.setText("STS");
                layout.setBackgroundResource(R.color.STSlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr());
                break;
            case Aanwijzing.TYPE_STSN:
                icon.setBackgroundResource(R.drawable.border_stsn_icon);
                cardView.setRippleColorResource(R.color.TTV);
                icon.setText("STS");
                layout.setBackgroundResource(R.color.STSNlight);
                mainInfoView.setText("Sein: " + a.getSTSseinNr() + ". Overwegen: " + a.getOverwegen() + ". Bruggen: " + a.getSTSNbruggen());
                break;
            case Aanwijzing.TYPE_TTV:
                icon.setBackgroundResource(R.color.TTV);
                cardView.setRippleColorResource(R.color.TTV);
                icon.setText("TTV");
                layout.setBackgroundResource(R.color.TTVlight);
                mainInfoView.setText("Reden: " + a.getMiscInfo());
                break;
        }
        return convertView;
    }

}

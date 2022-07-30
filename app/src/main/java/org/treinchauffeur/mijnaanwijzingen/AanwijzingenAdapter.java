package org.treinchauffeur.mijnaanwijzingen;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_material3, parent, false);
        }
        //Defining fields
        MaterialCardView cardView = convertView.findViewById(R.id.lijstItemView);
        TextView typeLongView = convertView.findViewById(R.id.textViewType);
        TextView locationView = convertView.findViewById(R.id.textViewLocation);
        TextView mainInfoView = convertView.findViewById(R.id.textViewMainInfo);
        TextView dateView = convertView.findViewById(R.id.textViewDate);
        TextView icon = convertView.findViewById(R.id.typeView);
        //Setting general information regardless of type
        typeLongView.setText(a.getLongType());
        locationView.setText(a.getLocatie());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = format.format(a.getDatum());
        dateView.setText(dateString);

        //Setting type-specific data
        switch (a.getType()) {
            case Aanwijzing.TYPE_VR:
                icon.setBackgroundResource(R.color.VR);
                icon.setText("VR");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.VRlight));
                mainInfoView.setText("Snelheid: " + a.getVRsnelheid() + " km/h. Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_OVW:
                icon.setBackgroundResource(R.color.OVW);
                icon.setText("OVW");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.OVWlight));
                mainInfoView.setText("Overwegen: " + a.getOverwegen() + ".");
                break;
            case Aanwijzing.TYPE_SB:
                icon.setBackgroundResource(R.color.SB);
                icon.setText("SB");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.SBlight));
                mainInfoView.setText("Snelheid: " + a.getSBsnelheid() + " km/h. Reden: " + a.getMiscInfo());
                break;
            case Aanwijzing.TYPE_STS:
                icon.setBackgroundResource(R.color.STS);
                icon.setText("STS");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.STSlight));
                mainInfoView.setText("Sein: " + a.getSTSseinNr());
                break;
            case Aanwijzing.TYPE_STSN:
                icon.setBackgroundResource(R.drawable.border_stsn_icon);
                icon.setText("STS");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.STSNlight));
                mainInfoView.setText("Sein: " + a.getSTSseinNr() + ". Overwegen: " + a.getOverwegen() + ". Bruggen: " + a.getSTSNbruggen());
                break;
            case Aanwijzing.TYPE_TTV:
                icon.setBackgroundResource(R.color.TTV);
                icon.setText("TTV");
                //cardView.setStrokeColor(getContext().getResources().getColor(R.color.TTVlight));
                mainInfoView.setText("Reden: " + a.getMiscInfo());
                break;
        }


        return convertView;
    }


    private void expandView(ViewGroup v, Aanwijzing a) {

    }

}

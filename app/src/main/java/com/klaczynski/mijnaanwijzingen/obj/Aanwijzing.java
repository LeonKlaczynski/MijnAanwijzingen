package com.klaczynski.mijnaanwijzingen.obj;

import java.util.Date;

public class Aanwijzing {
    public final static int TYPE_STS = 0, TYPE_STSN = 1, TYPE_TTV = 2,
            TYPE_VR = 3, TYPE_OVW = 4, TYPE_SB = 5;

    private int type;
    private String locatie; //Zowel enkel drgl-punt als traject tussen meerdere punten.
    private Date datum;
    private String trdl;
    private int treinNr;
    private String miscInfo; //Reden, hulpverleners, baanwerkers, LAE borden wel/niet geplaatst etc
    private String naamMcn;

    private String STSseinNr;
    private String Overwegen, STSNbruggen;

    private String VRsnelheid, SBsnelheid;
    private boolean VRschouw;

    public Aanwijzing(int type, String locatie, Date datum, String trdl,
                      int treinNr, String miscInfo, String naamMcn, String STSseinNr,
                      String Overwegen, String STSNbruggen, String VRsnelheid, String SBsnelheid) {
        this.type = type;
        this.locatie = locatie;
        this.datum = datum;
        this.trdl = trdl;
        this.treinNr = treinNr;
        this.miscInfo = miscInfo;
        this.naamMcn = naamMcn;
        this.STSseinNr = STSseinNr;
        this.Overwegen = Overwegen;
        this.STSNbruggen = STSNbruggen;
        this.VRsnelheid = VRsnelheid;
        this.SBsnelheid = SBsnelheid;
        this.VRschouw = false;
    }

    public Aanwijzing(int type, String locatie, Date datum, String trdl,
                      int treinNr, String miscInfo, String naamMcn, String STSseinNr,
                      String Overwegen, String STSNbruggen, String VRsnelheid, String SBsnelheid, boolean VRschouw) {
        this.type = type;
        this.locatie = locatie;
        this.datum = datum;
        this.trdl = trdl;
        this.treinNr = treinNr;
        this.miscInfo = miscInfo;
        this.naamMcn = naamMcn;
        this.STSseinNr = STSseinNr;
        this.Overwegen = Overwegen;
        this.STSNbruggen = STSNbruggen;
        this.VRsnelheid = VRsnelheid;
        this.SBsnelheid = SBsnelheid;
        this.VRschouw = VRschouw;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getTrdl() {
        return trdl;
    }

    public void setTrdl(String trdl) {
        this.trdl = trdl;
    }

    public int getTreinNr() {
        return treinNr;
    }

    public void setTreinNr(int treinNr) {
        this.treinNr = treinNr;
    }

    public String getMiscInfo() {
        return miscInfo;
    }

    public void setMiscInfo(String miscInfo) {
        this.miscInfo = miscInfo;
    }

    public String getNaamMcn() {
        return naamMcn;
    }

    public void setNaamMcn(String naamMcn) {
        this.naamMcn = naamMcn;
    }

    public String getSTSseinNr() {
        return STSseinNr;
    }

    public void setSTSseinNr(String STSseinNr) {
        this.STSseinNr = STSseinNr;
    }

    public String getOverwegen() {
        return Overwegen;
    }

    public void setOverwegen(String Overwegen) {
        this.Overwegen = Overwegen;
    }

    public String getSTSNbruggen() {
        return STSNbruggen;
    }

    public void setSTSNbruggen(String STSNbruggen) {
        this.STSNbruggen = STSNbruggen;
    }

    public String getVRsnelheid() {
        return VRsnelheid;
    }

    public void setVRsnelheid(String VRsnelheid) {
        this.VRsnelheid = VRsnelheid;
    }

    public String getSBsnelheid() {
        return SBsnelheid;
    }

    public void setSBsnelheid(String SBsnelheid) {
        this.SBsnelheid = SBsnelheid;
    }

    public boolean getVRschouw() {
        return VRschouw;
    }

    public void setVRschouw(boolean VRschouw) {
        this.VRschouw = VRschouw;
    }
}

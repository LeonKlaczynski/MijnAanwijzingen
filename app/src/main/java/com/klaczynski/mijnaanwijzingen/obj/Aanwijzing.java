package com.klaczynski.mijnaanwijzingen.obj;

import java.util.Date;

public class Aanwijzing {
    public final static int TYPE_STS = 0, TYPE_STSN = 1, TYPE_TTV = 2,
            TYPE_VR = 3, TYPE_OVW = 4, TYPE_SB = 5;

    private int type;
    private String location; //Zowel enkel drgl-punt als traject tussen meerdere punten.
    private Date datum;
    private String trdl;
    private int treinNr;
    private String miscInfo; //Reden, hulpverleners, baanwerkers, LAE borden wel/niet geplaatst etc
    private String naamMcn;

    private String STSseinNr;
    private String STSNoverwegen, STSNbruggen;

    private String VRsnelheid, SBsnelheid;

    public Aanwijzing(int type, String location, Date datum, String trdl,
                      int treinNr, String miscInfo, String naamMcn, String STSseinNr,
                      String STSNoverwegen, String STSNbruggen, String VRsnelheid, String SBsnelheid) {
        this.type = type;
        this.location = location;
        this.datum = datum;
        this.trdl = trdl;
        this.treinNr = treinNr;
        this.miscInfo = miscInfo;
        this.naamMcn = naamMcn;
        this.STSseinNr = STSseinNr;
        this.STSNoverwegen = STSNoverwegen;
        this.STSNbruggen = STSNbruggen;
        this.VRsnelheid = VRsnelheid;
        this.SBsnelheid = SBsnelheid;
    }
}

package com.klaczynski.mijnaanwijzingen.obj;

import java.util.Date;

public class Aanwijzing {
    public final static int TYPE_STS = 0, TYPE_STSN = 1, TYPE_TTV = 2,
            TYPE_VR = 3, TYPE_OVW = 4, TYPE_SB = 5;

    private int type;
    private String location;
    private Date datum;
    private String trdl;
    private int treinNr;

}

package com.laola.apa.costant;

import java.util.HashMap;
import java.util.Map;

public class ADCostant extends BaseCostant {
    public static final Map<String, String> adMap = new HashMap<String, String>() {
        {
            put("READ_AD","00 00 00 ");
            put("READ_TEMPERATURE","01 01 00 ");
            put("CHANGE_TEMPERATURE","02 HI LO ");
        }
    };
}


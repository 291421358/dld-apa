package com.laola.apa.costant;

import java.util.HashMap;
import java.util.Map;

public class RWCostant {
    public static final Map<String,String> RWMap = new HashMap<String, String>(){
        {
            put("READ","00 64 00 ");
            put("WRITE","E5 90 9A ");
        }
    };
}

package com.laola.apa.costant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: T ZHH
 * Date: 2021/8/20
 * Time: 9:37
 * To change this template use File | Settings | File Templates.
 */
public class ProjectNodeConstant {
    public static final Map<String, String> nodeMap = new HashMap<String, String>() {
        {
            put("1","加试剂1");
            put("2","加样品");
            put("3","搅拌样品");
            put("4","加试剂2");
            put("5","搅拌试剂2");
        }
    };
}

package com.laola.apa.costant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: T ZHH
 * Date: 2021/5/17
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class AlgorithmConstant {
    public static Map<String,String> algorithm = new HashMap<String, String>(){
        {
            put("1",            "三次样条函数");
            put("2",            "一次曲线");
            put("3",            "二次曲线拟合");
            put("4",            "RodBard");
            put("5",            "三次曲线拟合");
            put("三次样条函数",   "1");
            put("一次曲线" ,      "2");
            put("二次曲线拟合",   "3");
            put("RodBard",      "4");
            put("三次曲线拟合",   "5");
        }
    };
}

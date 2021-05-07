package com.laola.apa.costant;

import java.util.HashMap;
import java.util.Map;

public class AdjustedNewCostant extends BaseCostant {


    public static final Map<String, String> adjuestNew = new HashMap<String, String>() {
        {
//进入
            put("IN", "00 IN 00 ");
//复制
            put("COPY", "46 FROM TO");
//y-100步
            put("Y-100", "16 00 00 ");
//y-10步
            put("Y-10", "01 00 00 ");
//y+10步
            put("Y+10", "08 00 00 ");
//y+100步
            put("Y+100", "09 00 00 ");
//x-200步
            put("X-200", "06 00 00 ");
//x-20步
            put("X-20", "07 00 00 ");
//x+20步
            put("X+20", "03 00 00 ");
//x+200步
            put("X+200", "02 00 00 ");
//返回
            put("BACK", "04 00 00 ");
//返回+保存
            put("SAVE+BACK", "05 00 00 ");
/**
 * 加样针 SAMPLE
 */
//-100
            put("SAMPLE-100", "0A 00 00 ");
//-10
            put("SAMPLE-10", "0B 00 00 ");
//+10
            put("SAMPLE+10", "0C 00 00 ");
//+100
            put("SAMPLE+100", "0D 00 00 ");
//+50%（下降）
            put("SAMPLE+50%", "0E 00 00 ");
//+100%（下降）
            put("SAMPLE+100%", "15 00 00 ");
//样品臂返回
            put("SAMPLE+BACK", "17 00 00 ");
//样品臂保存+返回+
            put("SAMPLE+SAVE+BACK", "18 00 00 ");
/**
 * 抓手 GRIPPER
 */
//-100
            put("GRIPPER-100", "1A 00 00 ");
//-10
            put("GRIPPER-10", "1B 00 00 ");
//+10
            put("GRIPPER+10", "1C 00 00 ");
//+100
            put("GRIPPER+100", "1D 00 00 ");
//+50%
            put("GRIPPER+50%", "1E 00 00 ");
//+100%
            put("GRIPPER+100%", "1F 00 00 ");
//抓手返回
            put("GRIPPER+BACK", "20 00 00 ");
//抓手保存+返回+
            put("GRIPPER+SAVE+BACK", "21 00 00 ");
/**
 * 搅拌 STIR
 */
//-100
            put("STIR-100", "29 00 00 ");
//-10
            put("STIR-10", "28 00 00 ");
//+10
            put("STIR+10", "27 00 00 ");
//+100
            put("STIR+100", "26 00 00 ");
//+50%
            put("STIR+50%", "25 00 00 ");
//+100%
            put("STIR+100%", "24 00 00 ");
//搅拌返回
            put("STIR+BACK", "23 00 00 ");
//转动
            put("STIR+SPIN", "0D 08 00 ");
//搅拌保存+返回+
            put("STIR+SAVE+BACK", "22 00 00 ");
/**
 * 反应杯 REACT
 */
//第四个点
            put("REACT_FOURTH", "30 00 00 ");
//第一个点
            put("REACT_FIRST", "0F 00 00 ");
//第二个点
            put("REACT_SECOND", "10 00 00 ");
//第三个点
            put("REACT_THIRD", "11 00 00 ");
//+100
            put("REACT+100", "12 00 00 ");
//-100
            put("REACT-100", "13 00 00 ");
//反应杯返回
            put("REACT+BACK", "14 00 00 ");
//反应杯保存+返回+
            put("REACT+SAVE+BACK", "19 00 00 ");
/**
 * 清洗CLEAN
 */
//第一个深度
            put("CLEAN_FIRST", "35 00 00 ");
//第二个深度
            put("CLEAN_SECOND", "2A 00 00 ");
//+100
            put("CLEAN+100", "2B 00 00 ");
//-100
            put("CLEAN-100", "2C 00 00 ");
//清洗返回
            put("CLEAN+BACK", "2D 00 00 ");
//清洗保存+返回
            put("CLEAN+SAVE+BACK", "2E 00 00 ");


            /**
             * 进样架 SAMPLING
             */
//条码位
            put("SAMPLING-BCode", "3C 00 00 ");
//1号位
            put("SAMPLING_FIRST", "3D 00 00 ");
//5号位
            put("SAMPLING_FIFTY", "3E 00 00 ");
//+100步
            put("SAMPLING+100", "42 00 00 ");
//-100步
            put("SAMPLING-100", "43 00 00 ");
//进样架返回
            put("SAMPLING+BACK", "44 00 00 ");
//进样架保存+返回
            put("SAMPLING+SAVE+BACK", "45 00 00 ");

            /**
             * 试剂仓 SAMPLING
             */

//1号位
            put("RB_FIRST", "47 00 00 ");
//2号位
            put("RB_SECOND", "48 00 00 ");
//+100步
            put("RB+100", "49 00 00 ");
//-100步
            put("RB-100", "4A 00 00 ");
//试剂仓返回
            put("RB+BACK", "4B 00 00 ");
//试剂仓保存+返回
            put("RB+SAVE+BACK", "4C 00 00 ");
        }
    };
}

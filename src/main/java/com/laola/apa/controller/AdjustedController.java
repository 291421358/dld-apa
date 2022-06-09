package com.laola.apa.controller;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.impl.AdjustedImpl;
import com.laola.apa.server.impl.EquipmentStateImpl;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;
import gnu.io.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping(value = "adjusted")
public class AdjustedController {
    @Autowired
    AdjustedImpl adjusted;
    @Autowired
    EquipmentStateImpl equipmentState;

    @RequestMapping(value = "stirSpin" , method ={ RequestMethod.POST,RequestMethod.GET})
    public String stirSpin(String str){
        return adjusted.stirSpin(str);
    }
    /**
     * 打开串口
     */
    @RequestMapping(value = "connect" , method = RequestMethod.GET)
    public String connect(){
        SerialUtil cRead = new SerialUtil();
        SerialPort serialPort = cRead.startComPort();
        if (serialPort == null) {
            return "200";
        }
        p(serialPort.getName());
        return "200";
    }
    /**
     * 初始化
     */
    @RequestMapping(value = "init" , method = RequestMethod.GET)
    public String init(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 82 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     * 清洗八个 E5 90 86 01 01 08 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "cleanEight" , method = RequestMethod.GET)
    public String cleanEight(String no){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 86 01 01 0"+no+" 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }
    /**
     * 注射器吸 E5 90 91 0B 01 01 2C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "syringeAbsorb" , method = RequestMethod.GET)
    public String syringeAbsorb(String volume){
        if (volume.length() == 1){
            volume = "00 0"+volume;
        }
        if (volume.length() == 2){
            volume = "00 "+volume;
        }
        if (volume.length() == 3){
            volume = "0"+volume.substring(0,1)+" "+volume.substring(1);
        }
        if (volume.length() == 4){
            volume = volume.substring(0,2)+" "+volume.substring(2);
        }
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 0B 01 "+volume+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }
    /**
     * 注射器吹 E5 90 91 0B 00 01 2C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "syringeBlow" , method = RequestMethod.GET)
    public String syringeBlow(String volume){
        p(volume);
        if (volume.length() == 1){
            volume = "00 0"+volume;
        }
        if (volume.length() == 2){
            volume = "00 "+volume;
        }
        if (volume.length() == 3){
            volume = "0"+volume.substring(0,1)+" "+volume.substring(1);
        }
        if (volume.length() == 4){
            volume = volume.substring(0,2)+" "+volume.substring(2);
        }
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 0B 00 "+volume+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     * 移开反应杯子 removeCup E5 90 83 05 6D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     * 读透射ad
     */
    @RequestMapping(value = "removeCupReadTransmission" , method = RequestMethod.POST)
    public String removeCupReadTransmission(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }


    /**
     * 移开反应杯子 removeCup E5 90 83 05 6D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     * 读散射ad
     */
    @RequestMapping(value = "removeCupReadScattering" , method = RequestMethod.POST)
    public String removeCupReadScattering(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 05 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     * moveBackCup E5 90 83 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "moveBackCup" , method = RequestMethod.POST)
    public String moveBackCup(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     *  读取 E5 90 9A 00 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "readAddress" , method = RequestMethod.GET)
    public String readAddress(String address){
        address = DateUtils.DEC2HEX(address);
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 9A 00 "+address+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        InputStream inputStream;
        byte[] b = new byte[1];
        try {
            inputStream = cRead.startComPort().getInputStream();
            inputStream.read(b);

            return String.valueOf(Integer.parseInt(DateUtils.bytes2hexStr(b), 16));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     *  写入 E5 90 9A 88 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "writeAddress" , method = RequestMethod.POST)
    public String writeAddress(String address,String value){
        SerialUtil cRead = new SerialUtil();
        address = DateUtils.DEC2HEX(address);
        value = DateUtils.DEC2HEX(value);

        String init = cRead.init("E5 90 9A 88 "+address+" "+value+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }


    /**
     *  进水 E5 90 91 10 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "inlet" , method = RequestMethod.POST)
    public String inlet(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     *  进水停止 E5 90 91 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "inletSto" , method = RequestMethod.POST)
    public String inletSto(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     *  出水 E5 90 91 10 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "effluent" , method = RequestMethod.POST)
    public String effluent(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }

    /**
     *  出水停止 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "effluentSto" , method = RequestMethod.POST)
    public String effluentSto(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return init;
    }



    /**
     *   试剂仓进入 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "tuijin" , method = RequestMethod.GET)
    public String tuijin(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 C9 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return "1";
    }



    /**
     *  试剂仓推出 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "tuichu" , method = RequestMethod.GET)
    public String tuichu(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 C9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);

        return "1";
    }


    /**
     * 请求发送命令取得温度 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "getTemp" , method = RequestMethod.GET)
    public String getTemp(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);

        return "1";
    }

    /**
     * 柱塞泵1 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "plungerPump1" , method = RequestMethod.GET)
    public String plungerPump1(String value){
        value = DateUtils.DEC2HEX4Place(value);
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 13 00 "+value+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);

        return "1";
    }

    /**
     * 停止 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "stop" , method = RequestMethod.GET)
    public String stop(String value){
        value = DateUtils.DEC2HEX4Place(value);
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 be 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        EquipmentState t = new EquipmentState(1, 11, null, null
                , null, null, 0, 0, 0, null,0);
        equipmentState.update(t);
        p(init);

        return "1";
    }
    /**
     * 暂停 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "suspend" , method = RequestMethod.GET)
    public String suspend(String value){
        value = DateUtils.DEC2HEX4Place(value);
        SerialUtil cRead = new SerialUtil();
        EquipmentState t = new EquipmentState(1, 11, null, null
                , null, null, 0, 0, 0, null,2);
        equipmentState.update(t);
        String init = cRead.init("E5 90 bf 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init = cRead.init("E5 90 D2 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return "1";
    }
    /**
     * 柱塞泵2 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "plungerPump2" , method = RequestMethod.GET)
    public String plungerPump2(String value){
        value = DateUtils.DEC2HEX4Place(value);
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 12 00 "+value+" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);

        return "1";
    }

    /**
     * 静音 E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "BuzzerStop" , method = RequestMethod.GET)
    public String BuzzerStop(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 D3 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
        p(init);
        return "1";
    }


    private static void p(String a){
        System.out.println(a);
    }
}

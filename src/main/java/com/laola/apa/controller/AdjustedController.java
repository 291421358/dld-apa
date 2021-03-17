package com.laola.apa.controller;

import com.laola.apa.server.impl.AdjustedImpl;
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
        System.out.println(serialPort);
        return "200";
    }
    /**
     * 初始化
     */
    @RequestMapping(value = "init" , method = RequestMethod.GET)
    public String init(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 82 00 00 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     * 清洗八个 E5 90 86 01 01 08 06 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "cleanEight" , method = RequestMethod.GET)
    public String cleanEight(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 86 01 01 08 06 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }
    /**
     * 注射器吸 E5 90 91 0B 01 01 2C 00 00 00 00 00 00 00 00 00
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
        String init = cRead.init("E5 90 91 0B 01 "+volume+" 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }
    /**
     * 注射器吹 E5 90 91 0B 00 01 2C 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "syringeBlow" , method = RequestMethod.GET)
    public String syringeBlow(String volume){
        System.out.println(volume);
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
        String init = cRead.init("E5 90 91 0B 00 "+volume+" 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     * 移开反应杯子 removeCup E5 90 83 05 6D 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "removeCup" , method = RequestMethod.POST)
    public String removeCup(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 05 6D 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     * moveBackCup E5 90 83 06 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "moveBackCup" , method = RequestMethod.POST)
    public String moveBackCup(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 83 06 00 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     *  读取 E5 90 9A 00 64 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "readAddress" , method = RequestMethod.GET)
    public String readAddress(String address){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 9A 00 "+address+" 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
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
     *  写入 E5 90 9A 88 64 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "writeAddress" , method = RequestMethod.POST)
    public String writeAddress(String address,String value){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 9A 88 "+address+" "+value+" 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }


    /**
     *  进水 E5 90 91 10 00 01 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "inlet" , method = RequestMethod.POST)
    public String inlet(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 00 01 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     *  进水停止 E5 90 91 10 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "inletSto" , method = RequestMethod.POST)
    public String inletSto(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 00 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     *  出水 E5 90 91 10 01 01 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "effluent" , method = RequestMethod.POST)
    public String effluent(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 01 01 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }

    /**
     *  出水停止 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "effluentSto" , method = RequestMethod.POST)
    public String effluentSto(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return init;
    }



    /**
     *  出水停止 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "tuijin" , method = RequestMethod.GET)
    public String tuijin(){
        SerialUtil cRead = new SerialUtil();
//        String init = cRead.init("E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00");
//        System.out.println(init);
        return "1";
    }



    /**
     *  出水停止 E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00 00
     */
    @RequestMapping(value = "tuichu" , method = RequestMethod.GET)
    public String tuichu(){
        SerialUtil cRead = new SerialUtil();
//        String init = cRead.init("E5 90 91 10 01 00 00 00 00 00 00 00 00 00 00");
//        System.out.println(init);
        
        return "1";
    }
}

package com.laola.apa.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("index")
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    /**
     * 关闭java
     */
    @RequestMapping("close")
    public void close(){
        logger.info("close dld");
//        // 启动后访问地址
        String cmd =  "taskkill /F /t /IM dld.exe ";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        logger.info("close java");
//        // 启动后访问地址
        cmd =  "taskkill /F /t /IM java.exe ";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        logger.info("close javaw");
             cmd =   "taskkill /F /t /IM javaw.exe ";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

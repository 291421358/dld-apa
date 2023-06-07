package com.laola.apa.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

    @RequestMapping("update")
    public void update() throws MalformedURLException {
        //1.实现文件上传
        // 设置更新包安装路径
        String property = System.getProperty("java.class.path");
        File[] files = FileUtil.ls(System.getProperty("user.dir"));
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.println(file+"\n");
        }
        System.out.println(property);
        //开始上传
        int bytesum = 0;
        int byteread = 0;

        String spec = "file://server/share/apa.jar";
        URL url = new URL(spec);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(property);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            System.out.println("len="+bytesum);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.执行更新bat
        restart();
        return;

    }


    public static void restart()  {
        File[] files = FileUtil.ls(System.getProperty("user.dir"));
        HashMap<Long, String> fileMap = new HashMap<>();
        String road = "";
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(".jar")) {
                fileMap.put(file.lastModified(), name);
            }
            if (name.endsWith("jre")){
                road = file + "\\bin\\javaw";
                System.out.println(road);
            }

        }
        Set<Long> longs = fileMap.keySet();
        Long max = Collections.max(longs);
        System.out.println(fileMap.get(max));
//        ProcessBuilder pb = new ProcessBuilder( road , " -jar", fileMap.get(max) ," >> \"%date:~0,4%-%date:~5,2%-%date:~8,2% %time:~0,2%-%time:~3,2%-%time:~6,2%.txt\"");
//        ProcessBuilder pb = new ProcessBuilder( road , " -jar", fileMap.get(max) );
        ProcessBuilder pb = new ProcessBuilder( "ap.bat");
        System.out.println("startttttttttttttt1");

        start start = new start(pb);
        System.out.println("startttttttttttttt2");

        start.start();
        new exit().start();
    }

}

class start extends Thread  {
    @Autowired
    ProcessBuilder pb;

    public start(ProcessBuilder pb){
        this.pb = pb;
    }
    @Override
    public void run() {
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class exit extends Thread  {

    @Override
    public void run() {
        System.exit(0);
    }
}

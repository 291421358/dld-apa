package com.laola.apa.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;

/**
 * @author tzh
 * @version V1.0.0
 * @projectName laob_special
 * @title     IndexConfig
 * @package    com.laob.laob_special.common
 * @date   2019/9/10 19:07
 * @explain 服务启动时自动打开项目
 */
@Configuration
public class IndexConfig {

    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent() {
        System.out.println("mini-100准备就绪 ... 启动浏览器");

//        // 启动后访问地址
        String cmd = " cmd /c \"C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe\"  --app=http://localhost:8081";// "cmd /c start"+path+"/"+filename;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent1() {
        System.out.println("mini-100准备就绪 ... 启动浏览器");

    }
}
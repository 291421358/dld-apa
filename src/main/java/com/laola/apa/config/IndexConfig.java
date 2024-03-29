package com.laola.apa.config;

import com.laola.apa.server.ProjectTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @explain 服务启动时自动打开项目 remote
 */
@Configuration
public class IndexConfig {
    private Logger logger = LoggerFactory.getLogger(IndexConfig.class);
    @Autowired
    private ProjectTest projectTest;
    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent() {
        System.out.println();
        System.out.println();
        System.out.println();
        logger.info("v1.1");
        System.out.println();
        System.out.println();
        System.out.println();
        logger.info("删除所有未做的项目");
        projectTest.deleteProjects();

        logger.info("mini-100准备就绪 ... 启动浏览器");
//        // 启动后访问地址
//        String cmd = " cmd /c \"C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe\"  --app=http://localhost:8081";// "cmd /c start"+path+"/"+filename;
        String closeEDGE = " cmd /c taskkill /F /IM msedge.exe ";// "cmd /c start"+path+"/"+filename;
        String cmd = " cmd /c   \"C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe\"  --kiosk http://localhost:8081";// "cmd /c start"+path+"/"+filename;
        try {
            Process exec = Runtime.getRuntime().exec(closeEDGE);
            int i = exec.waitFor();
            logger.info("edge关闭 ... 启动浏览器");
            Process exec1 = Runtime.getRuntime().exec(cmd);
            int i1 = exec1.waitFor();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent1() {
//        FutureTaskable.main(null);
    }

}
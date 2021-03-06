package com.laola.apa.config;

import com.laola.apa.server.ProjectTest;
import com.laola.apa.task.FutureTaskable;
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

        logger.info("删除所有未做的项目");
        projectTest.deleteProjects();

        logger.info("mini-100准备就绪 ... 启动浏览器");
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
//        FutureTaskable.main(null);
        }

}
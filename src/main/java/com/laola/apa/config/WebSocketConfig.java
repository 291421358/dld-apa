package com.laola.apa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Spring Boot中使用webSocket必备，讲ServerEndpointExporter注册成spring的bean
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExportern(){
        return new ServerEndpointExporter();
    }
}

package com.laola.apa.listener;

import com.laola.apa.common.Result;
import com.laola.apa.serial.SerialController;
import com.laola.apa.utils.WebSocket;
import com.laola.apa.websocket.CustomerWebSocketServer;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 串口事件监听
 *
 * @Description:
 * @Author: Fan
 * @Date: 2021/7/11 20:27
 * @Version 1.0
 **/
public class SerialPortListener implements SerialPortEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortListener.class);

    Result result = new Result();

    public SerialPortListener() {}
    @Autowired
    CustomerWebSocketServer ustomerWebSocketServer;
    @Autowired
    WebSocket customerWebSocketServer;
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            // 监听串口，当接收到数据时触发
            case SerialPortEvent.DATA_AVAILABLE:
                result = SerialController.readMsgFromPort(SerialController.globalPortName);
                // TODO 推送给前端，无法获取WebSocketServer实例！！！
                //将获取到的数据进行转码并输出
                ustomerWebSocketServer.broadcast(new String((byte[]) result.getData()));
                try {
                    customerWebSocketServer.sendMessageAll(new String((byte[]) result.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                LOGGER.info("发送消息缓冲区为空！");
                break;
            case SerialPortEvent.CTS:
                LOGGER.info("待发送数据已清除！");
                break;
            case SerialPortEvent.DSR:
                LOGGER.info("待发送数据已就绪！");
                break;
            case SerialPortEvent.RI:
                LOGGER.info("振铃指示！");
                break;
            case SerialPortEvent.CD:
                LOGGER.info("载波检测！");
                break;
            case SerialPortEvent.OE:
                LOGGER.info("位溢出！");
                break;
            case SerialPortEvent.PE:
                LOGGER.info("奇偶校验错误！");
                break;
            case SerialPortEvent.FE:
                LOGGER.info("帧错误！");
                break;
            case SerialPortEvent.BI:
                LOGGER.info("通讯中断!");
                break;
            default:
                break;
        }
    }
}

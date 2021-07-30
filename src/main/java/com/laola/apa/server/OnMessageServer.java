package com.laola.apa.server;

import com.laola.apa.costant.ADCostant;
import com.laola.apa.costant.AdjustedNewCostant;
import com.laola.apa.costant.LightQuasiConstant;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;
import com.laola.apa.utils.WebSocket;
import com.laola.apa.task.FutureTaskable;
import gnu.io.SerialPort;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Controller
//使websock在spring boot中生效，交由spring 管理
@Component
public class OnMessageServer extends Thread {
    @Autowired
    ProjectTest projectTest;
    @Autowired
    GetProjectResult getProjectResult;

    private static OnMessageServer onMessageServer;

    @PostConstruct
    public void init() {
        onMessageServer = this;
        this.projectTest = onMessageServer.projectTest;
    }
    Logger logger = LoggerFactory.getLogger(OnMessageServer.class);

    /**
     * @param username
     * @param message
     * @param webSocket
     * @throws IOException
     * @throws InterruptedException
     */
    public void onMessage(String username, Object message, WebSocket webSocket) throws IOException, InterruptedException {
        //将收到的数据转换为json格式
        JSONObject jsonTo = JSONObject.fromObject(message);
        //打印收到的数据
        System.out.println( "OnMessageServer " +jsonTo.get("message") );
        //将json格式数据转换为字符串
        String mes = String.valueOf(jsonTo.get("message"));
        //新建连接串口帮助类对象 SerialTest
        SerialUtil serialUtil = new SerialUtil();
        //连接串口并返回串口对象
        SerialPort serialPort = serialUtil.startComPort();
        if (serialPort == null || serialPort.getOutputStream() == null) {
            webSocket.sendMessageTo("serialPort:null", username);
            return;
        }
        //从串口对象中获得输出流
        OutputStream outputStream = serialPort.getOutputStream();
        //获得串口输入流
        InputStream inputStream = serialPort.getInputStream();
        //串口命令 字符串
        String st = null;
        try {
            st = "00";
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断是否为项目自动/手动测试
        if (jsonTo.get("code").equals("5") || jsonTo.get("code").equals(5)) {
            FutureTaskable.stop();
//            serialPort.notifyOnDataAvailable(false);
//            serialPort.removeEventListener();
            //查询当前有效的样本数量
            int projectDoing = onMessageServer.projectTest.isProjectDoing();
            logger.info("DOING NUMBERS:"+projectDoing);
            //样品 试剂位   试剂量  项目序号 波长
//            List<String> messageList = onMessageServer.projectTest.get(Integer.parseInt(String.valueOf(jsonTo.get("message"))));

//            if (projectDoing <= 0) {
                //样本数量为零  //需要先进行项目准备
                String before = "e5 90 82 01 02 30 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";//e5 90 81 01 02 30 20 00 00 00 00 00 00 00 00 00
                //项目准备指令转换为字节码
            byte[] beforeByte = new byte[0];
            try {
                beforeByte = DateUtils.hexStrToBinaryStr(before);
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert beforeByte != null;
                //发送指令
                outputStream.write(beforeByte, 0, beforeByte.length);
                for (int i = 0; i <= 20; ) {
                    i++;
                    Thread.sleep(1000);
                    int available = inputStream.available();
                    if (available <= 12 && available > 0) {
                        byte[] readBuffer = new byte[available];
                        int read = inputStream.read(readBuffer);
                        String hexStr = DateUtils.bytes2hexStr(readBuffer);
                        logger.info("read" + read + "\nreadBuffer:" + readBuffer.length + "\nhexStr:" + hexStr + "\nava" + available);
                        assert hexStr != null;
                        break;
                    }
//                    System.out.println(i + ":OnMessageServer 120 end for在·" + Line.Info.class);
                }
//            }
        } else {

            //生成命令处理
            st = dealwithst(st, jsonTo, mes, serialPort);
            //字符串转换成16进制字节码
            byte[] bytes = new byte[0];
            try {
                bytes = DateUtils.hexStrToBinaryStr(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //发送字节码串口通讯业务完成
            //code为5 测试项目 直接结束 在 监听处获得数据
            logger.info("st:Message153  the command " + st);
            outputStream.write(bytes, 0, bytes.length);
//            serialPort.notifyOnDataAvailable(false);
//            serialPort.removeEventListener();
            //code为3
        }
    }

    /**
     * @param st
     * @param jsonTo
     * @param mes
     * @param serialPort
     */
    private String dealwithst(String st, JSONObject jsonTo, String mes, SerialPort serialPort) {
        //调校
        if (jsonTo.get("code").equals("2") || jsonTo.get("code").equals(2)) {
            st = AdjustedNewCostant.head + AdjustedNewCostant.adjuestNew.get(mes) + AdjustedNewCostant.last;
        }
//        if (jsonTo.get("code").equals("1") || jsonTo.get("code").equals(1)) {
//            st = AdjustedCostant.adjustedMap.get(mes);
//        }
        //进入
        if ("IN".equals(mes)) {
            st = st.replace("IN", String.valueOf(jsonTo.get("number")));
        }
        //复制
        if ("COPY".equals(mes)) {
            st = st.replace("FROM", String.valueOf(jsonTo.get("number")));
            st = st.replace("TO", String.valueOf(jsonTo.get("number2")));
        }
        if (jsonTo.get("code").equals("3") || jsonTo.get("code").equals(3)) {
            st = ADCostant.ADhead + ADCostant.adMap.get(mes) + ADCostant.last;
        }
        if (jsonTo.get("code").equals("4") || jsonTo.get("code").equals(4)) {
            st = LightQuasiConstant.LightHead + LightQuasiConstant.lightQuasiMap.get(mes) + LightQuasiConstant.last;
        }
        logger.info(st);
        return st;
    } 
}

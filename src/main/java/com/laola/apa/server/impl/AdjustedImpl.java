package com.laola.apa.server.impl;

import com.laola.apa.utils.SerialUtil;
import com.laola.apa.costant.AdjustedNewConstant;
import com.laola.apa.utils.DateUtils;
import gnu.io.SerialPort;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
public class AdjustedImpl {


    public String stirSpin(String str) {
        try {
            //新建连接串口帮助类对象 SerialTest 连接串口并返回串口对象
            SerialPort serialPort = new SerialUtil().startComPort();
            OutputStream outputStream = serialPort.getOutputStream();
            str = AdjustedNewConstant.SpinHead + AdjustedNewConstant.adjuestNew.get("STIR+SPIN") + AdjustedNewConstant.last;
//字符串转换成16进制字节码
            byte[] bytes = DateUtils.hexStrToBinaryStr(str);
            //发送字节码串口通讯业务完成
            outputStream.write(bytes, 0, bytes.length);
//            serialPort.close();
//            InputStream inputStream = serialPort.getInputStream();

//                Thread.sleep(100);
//            int read = inputStream.read();
//            byte[] readBuffer = new byte[read];
//            inputStream.read(readBuffer);
//            String hexStr = DateUtils.bytes2hexStr(readBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}

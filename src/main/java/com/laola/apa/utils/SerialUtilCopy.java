package com.laola.apa.utils;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialUtilCopy implements SerialPortEventListener { // SerialPortEventListener
    // 监听器,我的理解是独立开辟一个线程监听串口数据
    static CommPortIdentifier portId; // 串口通信管理类
    static Enumeration<?> portList; // 有效连接上的端口的枚举
    InputStream inputStream; // 从串口来的输入流
    public static OutputStream outputStream;// 向串口输出的流
    static SerialPort serialPort; // 串口的引用
    // 堵塞队列用来存放读到的数据
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    public OutputStream getOutputStream(){
        return outputStream;
    }

    @Override
    /**
     * SerialPort EventListene 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {
        //
    }

    /**
     * 通过程序打开COM4串口，设置监听器以及相关的参数
     *
     * @return 返回1 表示端口打开成功，返回 0表示端口打开失败
     */
    public int startComPort() {
        // 判断是否串口
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements())
        {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)//如果端口类型是串口则判断名称
            {
                if(portId.getName().equals("COM2")){//如果是COM2端口则退出循环
                    this.portId = portId;
                }else{
                    portId=null;
                }
            }
        }
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            // 判断如果COM4串口存在，就打开该串口
            if (portId.getName().equals("COM3")) {
                try {
                    // 打开串口名字为COM_4(名字任意),延迟为2毫秒
                    serialPort = (SerialPort) portId.open("COM_3", 1000);
                } catch (PortInUseException e) {
                    e.printStackTrace();
                    return 0;
                }
                // 设置当前串口的输入输出流
                try {
                    inputStream = serialPort.getInputStream();
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
                // 给当前串口添加一个监听器
                try {
                    serialPort.addEventListener(this);
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                    return 0;
                }
                // 设置监听器生效，即：当有数据时通知
                serialPort.notifyOnDataAvailable(true);

                // 设置串口的一些读写参数
                try {
                    // 比特率、数据位、停止位、奇偶校验位
                    serialPort.setSerialPortParams(9600,
                            SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                    return 0;
                }

                return 1;
            }
        }
        return 0;
    }

//    public static void main(String[] args) {
//        SerialUtilCopy cRead = new SerialUtilCopy();
//        cRead.initt("E5 90 82 00 00 00 00 00 00 00 00 00 00 00 00 00");
//    }

    public int initt(String command) {
        SerialUtilCopy cRead = new SerialUtilCopy();
        int i = cRead.startComPort();
        if (i == 0){
            System.out.println("未连接");
            return 0;
        }
        try {
            byte[] bytes = DateUtils.hexStrToBinaryStr(command);
            System.out.println("发出：" + command);
            outputStream.write(bytes, 0,
                    bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

}
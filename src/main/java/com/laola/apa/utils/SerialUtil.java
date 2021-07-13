package com.laola.apa.utils;

import com.laola.apa.server.GetProjectResult;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tzh
 */
@Service
@Component
@Controller
public class SerialUtil extends Thread implements SerialPortEventListener { // SerialPortEventListener
    // 监听器,我的理解是独立开辟一个线程监听串口数据
     static CommPortIdentifier portId;
    // 串口通信管理类
     static Enumeration<?> portList;
    // 有效连接上的端口的枚举
    private InputStream inputStream;
    // 从串口来的输入流
    private static OutputStream outputStream;
    // 向串口输出的流
    private static SerialPort serialPort;
    // 串口的引用
    // 堵塞队列用来存放读到的数据
    /**
     *
     */
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
    private SerialPortEventListener thisObj = this;

    public Object getThisObj() {
        return thisObj;
    }

    public void setThisObj(SerialPortEventListener thisObj) {
        this.thisObj = thisObj;
    }

    private volatile boolean shutdownRequested = false;

    @Autowired
    private GetProjectResult getProjectResult;

    public static SerialUtil serialUtil;
    @PostConstruct
    public void init(){
        serialUtil = this;
        this.getProjectResult = serialUtil.getProjectResult ;
    }

    SerialUtil SerialUtil(){
        return this;
    }

    @Override
    /**
     * @apiNote SerialPort EventListene 的方法,持续监听端口上是否有数据流
     * @author tzhh
     * @date 2021/5/27 17:03
     * @param event
     * @return
     **/
    public void serialEvent(SerialPortEvent event) {//

        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据
                // 时读取数据
                try {
                    int numBytes = -1;
                    //无限循环，每隔20毫秒对串口COM21进行一次扫描，检查是否有数据到达

                    while(true){

                        //获取串口COM21收到的可用字节数
                        numBytes = inputStream.available();

                        //如果可用字节数大于零则开始循环并获取数据
                        while(numBytes >  1){

                            System.out.println(numBytes);
                            //循环10秒，如果下一次的长度与上一次相同，便取走串口中的数据
                            int i = 100;
                            for (int j = 0; j < i; j++) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                int innerIfNumBytes = inputStream.available();
                                if(innerIfNumBytes == numBytes){
                                    break;
                                }else {
                                    numBytes = innerIfNumBytes;
                                }
                            }
                            numBytes = inputStream.available();
                            byte[] readBuffer = new byte[numBytes];
                            //从串口的输流象中读入数据并将数据存入对放到缓存数组中
                            inputStream.read(readBuffer);

                            //将获取到的数据进行转码并输出
                            String x = DateUtils.bytes2hexStr(readBuffer);
                            //处理项目结果
                            serialUtil.getProjectResult.dealProjectResult(x,serialPort);
//                            System.out.println(x + ":readBuffer serialUtil 99");
                            //更新循环条件
                            numBytes = inputStream.available();
                        }
                        try {
                            // Π
//                        让线程睡眠20毫秒
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
                default:
        }
    }

    /**
     * @apiNote 通过程序打开COM1串口，设置监听器以及相关的参数
     * @author tzhh
     * @date 2021/5/27 17:04
     * @param
     * @return {@link SerialPort}
     **/
    public SerialPort startComPort() {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {

            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();
            String currentOwner = portId.getCurrentOwner();

            if (null != serialPort && null != currentOwner && (currentOwner.equals("COM1") || currentOwner.equals("COM4") )){
                //如果串口对象不为空且是COM1 则返回该端口对象
                // 设置当前串口的输入输出流
                try{
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
                // 给当前串口添加一个监听器
                serialPort.addEventListener(this);
                // 设置监听器生效，即：当有数据时通知
                serialPort.notifyOnDataAvailable(true);

                // 设置串口的一些读写参数
                // 比特率、数据位、停止位、奇偶校验位
                serialPort.setSerialPortParams(9600,
                        SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                } catch (Exception e) {
                    return serialPort;
                }
                return serialPort;
            }
//            System.out.println("设备类型：--->" + portId.getPortType());
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM4//COME1串口存在，就打开该串口
                if ( "COM1".equals(portId.getName())) {
                    System.out.println("EquipmentName：---->" + portId.getName());
                    try {
                        // 打开串口名字为COM_4(名字任意),延迟为2毫秒
                        serialPort = (SerialPort) portId.open("COM1", 2000);
                    // 设置当前串口的输入输出流
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    // 给当前串口添加一个监听器
                        serialPort.addEventListener(this);
                    // 设置监听器生效，即：当有数据时通知
                    serialPort.notifyOnDataAvailable(true);

                    // 设置串口的一些读写参数
                        // 比特率、数据位、停止位、奇偶校验位
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (Exception e) {
                        return serialPort;
                    }
                    return serialPort;
                }
            }
        }
        return serialPort;
    }

    void doShutDown() throws InterruptedException {
        shutdownRequested = true;
        interrupt();
    }

    @Override
    public void run() {
        //判断是否中断

        try {
            doShutDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("--------------任务处理线程运行了--------------");
                while (true) {
                    // 如果堵塞队列中存在数据就将其输出
                    if (msgQueue.size() > 0) {
                        System.out.println(msgQueue.take()
                        );
                        serialPort.close();
                    }

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 发送指令
     * @return
     */
    public String init(String command) {
        SerialUtil cRead = new SerialUtil();
        SerialPort serialPort = cRead.startComPort();
        if (serialPort != null) {
            // 启动线程来处理收到的数据
            try {
                byte[] bytes = DateUtils.hexStrToBinaryStr(command);
                System.out.println("SEND COUNT：" + command.length());//.getBytes("gbk").length);
                System.out.println(command);
                outputStream.write(bytes, 0, bytes.length);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "200";
        } else {
            return "505";
        }
    }


    /**
     * 发送指令，然后关闭串口
     * @return
     */
    public static void sendCommond(String command){
        SerialUtil serialUtil = new SerialUtil();
        SerialPort serialPort = serialUtil.startComPort();
        byte[] bytes = DateUtils.hexStrToBinaryStr(command);
        System.out.println(command);
        try {
            outputStream.write(bytes,0,bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        serialPort.close();
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }



    /**
     * 发送指令
     * @return
     */
    public String init(String command,String com) {
//        command = DateUtils.unicodeEncode(command);
        SerialUtil cRead = new SerialUtil();
        SerialPort serialPort = cRead.startComPort(com);
        if (serialPort != null) {
            // 启动线程来处理收到的数据
            try {
                byte[] bytes = DateUtils.hexStrToBinaryStr(command);
                System.out.println("SEND COUNT：" + command.length());//.getBytes("gbk").length);
                System.out.println(command);
                outputStream.write(bytes, 0, bytes.length);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "200";
        } else {
            serialPort.close();
            return "505";
        }
    }

    /**
     * @apiNote 通过程序打开COM1串口，设置监听器以及相关的参数
     * @author tzhh
     * @date 2021/5/27 17:04
     * @param
     * @return {@link SerialPort}
     **/
    public SerialPort startComPort(String com) {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {

            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();
            String currentOwner = portId.getCurrentOwner();

            if (null != serialPort && null != currentOwner && (currentOwner.equals(com))){
                //如果串口对象不为空且是COM1 则返回该端口对象
                // 设置当前串口的输入输出流
                try{
                    inputStream = serialPort.getInputStream();
                    outputStream = serialPort.getOutputStream();
                    // 给当前串口添加一个监听器
                    serialPort.addEventListener(this);
                    // 设置监听器生效，即：当有数据时通知
                    serialPort.notifyOnDataAvailable(true);

                    // 设置串口的一些读写参数
                    // 比特率、数据位、停止位、奇偶校验位
                    serialPort.setSerialPortParams(9600,
                            SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (Exception e) {
                    return serialPort;
                }
                return serialPort;
            }
//            System.out.println("设备类型：--->" + portId.getPortType());
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM4//COME1串口存在，就打开该串口
                if ( com.equals(portId.getName())) {
                    System.out.println("EquipmentName：---->" + portId.getName());
                    try {
                        // 打开串口名字为COM_4(名字任意),延迟为2毫秒
                        serialPort = (SerialPort) portId.open(com, 2000);
                        // 设置当前串口的输入输出流
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                        // 给当前串口添加一个监听器
                        serialPort.addEventListener(this);
                        // 设置监听器生效，即：当有数据时通知
                        serialPort.notifyOnDataAvailable(true);

                        // 设置串口的一些读写参数
                        // 比特率、数据位、停止位、奇偶校验位
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (Exception e) {
                        return serialPort;
                    }
                    return serialPort;
                }
            }
        }
        return serialPort;
    }

}
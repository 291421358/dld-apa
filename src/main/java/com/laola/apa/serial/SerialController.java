package com.laola.apa.serial;

import com.laola.apa.DO.SerialDO;
import com.laola.apa.common.Result;
import com.laola.apa.common.StateCodeEnum;
import com.laola.apa.listener.SerialPortListener;
import com.laola.apa.utils.DateUtils;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author: fan
 * @DateTime: 2021-07-09 14:19
 **/
@Controller
@RequestMapping("/serial")
public class SerialController {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialController.class);

    /**
     * 存放串口对象
     */
    public static Map<String, Object> serialMap = new HashMap<>();

    /**
     * 串口
     */
    public static String globalPortName;

    @GetMapping
    public String index(){
        return "index";
    }

    /**
     * 查找当前正在使用的所有串口
     *
     * @return
     */
    @GetMapping("/listPorts")
    @ResponseBody
    public static ModelMap listPorts() {
        ModelMap modelMap = new ModelMap();
        CommPortIdentifier commPortIdentifier;
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        List<String> portList = new ArrayList<>();
        while (ports.hasMoreElements()) {
            commPortIdentifier = (CommPortIdentifier) ports.nextElement();
            if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(commPortIdentifier.getName());
                LOGGER.info("当前正在使用的串口有：{}", commPortIdentifier.getName());
            }
        }
        modelMap.put("data", portList);
        return modelMap;
    }

    /**
     * 打开指定端口
     *
     * @param serialDO 串口参数对象
     * @return 串口的对象
     */
    @PostMapping("/openPort")
    @ResponseBody
    public static Result openPort(SerialDO serialDO) {
        Result result = new Result();
        LOGGER.info("串口参数：{}", serialDO);
        SerialPort serialPort = null;
        try {
            // 获取指定端口对象
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialDO.getPortName());
            // 打开指定端口，同时指定超时时间
            CommPort commPort = portIdentifier.open(serialDO.getPortName(), 3000);
            // 判断端口是否是串口
            if (commPort instanceof SerialPort) {
                LOGGER.info("打开串口:{}", serialDO.getPortName());
                serialPort = (SerialPort) commPort;
                serialMap.put(serialDO.getPortName(), serialPort);
                globalPortName = serialDO.getPortName();
                // 添加串口监听事件
                serialPort.addEventListener(new SerialPortListener());
                // 设置串口参数
                serialPort.setSerialPortParams(serialDO.getBaudrate(), serialDO.getDataBits(), serialDO.getStopBits(), serialDO.getParity());
                // 当有数据到达时唤醒数据接收线程
                serialPort.notifyOnDataAvailable(true);
                // 当串口连接中断时唤醒中断线程
                serialPort.notifyOnBreakInterrupt(true);
                serialPort.notifyOnCarrierDetect(true);
                serialPort.notifyOnDSR(true);
                result.setCode(StateCodeEnum.SUCCESS.getCode());
                result.setMsg("成功打开串口" + serialDO.getPortName());
                result.setData(serialPort.getClass());
                return result;
            } else {
                result.setCode(StateCodeEnum.FAIL.getCode());
                result.setMsg("未找到指定串口！");
                throw new NoSuchPortException();
            }
        } catch (NoSuchPortException e) {
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("未找到指定串口！");
            LOGGER.error("未找到指定串口！", e.getMessage());
        } catch (PortInUseException e) {
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("串口被占用！");
            LOGGER.error("串口被占用！", e.getMessage());
        } catch (UnsupportedCommOperationException e) {
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("打开串口出错！");
            LOGGER.error("打开串口出错！", e.getMessage());
        } catch (TooManyListenersException e) {
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("串口监听实例过多！");
            LOGGER.error("串口监听实例过多！", e.getMessage());
        }
        return result;
    }

    /**
     * 关闭串口
     *
     * @param portName
     * @return
     */
    @PostMapping("/closePort")
    @ResponseBody
    public static Result closePort(String portName) {
        Result result = new Result();
        SerialPort serialPort = (SerialPort) serialMap.get(portName);
        if (null != serialPort) {
            serialPort.close();
            LOGGER.info("串口{}已关闭！", portName);
            result.setCode(StateCodeEnum.SUCCESS.getCode());
            result.setMsg("串口" + portName + "已关闭！");
        } else {
            LOGGER.error("串口错误，未找到指定串口{}", portName);
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("未找到指定串口"+portName);
        }
        return result;
    }

    /**
     * 发送消息给指定串口
     *
     * @param portName 串口名
     * @param msg      接收前端消息
     * @return
     */
    @PostMapping("/sendMsgToPort")
    @ResponseBody
    public static Result sendMsgToPort(String portName, String msg) {

//        SerialController.closePort("COM1");

        SerialDO serialDo3 = new SerialDO();
        serialDo3.setPortName("COM3");
        serialDo3.setBaudrate(9600);
        serialDo3.setDataBits(8);
        serialDo3.setStopBits(1);
        serialDo3.setParity(0);
        SerialController.openPort(serialDo3);
        byte[] bytess = new byte[0];
        try {
            bytess = DateUtils.hexStrToBinaryStr(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SerialPort serialPort = (SerialPort) serialMap.get("COM3");
        OutputStream outputStream = null;
        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.write(bytess, 0, bytess.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SerialController.closePort("COM3");
        SerialDO serialDo = new SerialDO();
        serialDo.setPortName("COM2");
        serialDo.setBaudrate(9600);
        serialDo.setDataBits(8);
        serialDo.setStopBits(1);
        serialDo.setParity(0);
        SerialController.openPort(serialDo);

        Result result = new Result();

//        OutputStream outputStream = null;
        try {
            serialPort = (SerialPort) serialMap.get("COM2");
            outputStream = serialPort.getOutputStream();
            byte[] bytes = DateUtils.hexStrToBinaryStr(msg);
//            System.out.println("SEND COUNT：" + bytes.length());//.getBytes("gbk").length);
            System.out.println(bytes);
            outputStream.write(bytes, 0, bytes.length);
//            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            result.setCode(StateCodeEnum.SUCCESS.getCode());
            result.setMsg("消息发送成功！");
            LOGGER.info("发送消息给串口{}:{}", portName, msg);
        } catch (IOException e) {
            LOGGER.error("发送消息异常！", e.getMessage());
            result.setMsg("发送消息异常！");
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setData(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("发送消息关闭流异常！", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 从指定串口读取数据
     *
     * @param portName 串口名
     * @return
     */
    @GetMapping("/readMsgFromPort")
    @ResponseBody
    public static Result readMsgFromPort(String portName) {
        Result result = new Result();
        SerialPort serialPort = (SerialPort) serialMap.get(portName);
        InputStream inputStream = null;
        byte[] msgBytes = null;
        try {
            inputStream = serialPort.getInputStream();
            int msgLength = inputStream.available();
            while (inputStream.available() > 0) {
                msgBytes = new byte[msgLength];

                inputStream.read(msgBytes);
                msgLength = inputStream.available();
            }
            if (null != msgBytes) {
                String  s= DateUtils.bytes2hexStr(msgBytes);
                LOGGER.info("从串口{}接收到数据:{}", portName, s);
                result.setCode(StateCodeEnum.SUCCESS.getCode());
                result.setMsg("成功接受到数据！");
                result.setData(new String(msgBytes, "GBK"));
                return result;
            }
        } catch (IOException e) {
            result.setCode(StateCodeEnum.FAIL.getCode());
            result.setMsg("读取数据异常！");
            LOGGER.error("读取数据异常！{}", e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                result.setCode(StateCodeEnum.FAIL.getCode());
                result.setMsg("读取数据时输入流关闭失败！");
                LOGGER.error("读取数据时输入流关闭失败！{}", e.getMessage());
            }
        }
        result.setCode(StateCodeEnum.SUCCESS.getCode());
        result.setMsg("消息接收中...");
        return result;
    }

    /**
     * 测试
     * @param args
     */
  /*  public static void main(String[] args) {
        String portName = "COM1";
        String sendMsg = "hello serial!";

        SerialDO serialDO = new SerialDO();
        serialDO.setPortName(portName);
        serialDO.setBaudrate(9600);
        serialDO.setDataBits(SerialPort.DATABITS_8);
        serialDO.setStopBits(SerialPort.STOPBITS_1);
        serialDO.setParity(SerialPort.PARITY_NONE);
        Result result = openPort(serialDO);
        SerialPort serialPort = (SerialPort) result.getData();

        // 当有数据到达时唤醒数据接收线程
        serialPort.notifyOnDataAvailable(true);
        // 当串口连接中断时唤醒中断线程
        serialPort.notifyOnBreakInterrupt(true);

        try {
            serialPort.addEventListener(serialPortEvent -> {
                switch (serialPortEvent.getEventType()) {
                    case SerialPortEvent.DATA_AVAILABLE:
                        SerialMain.readMsgFromPort(globalPortName);
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
            });
        } catch (TooManyListenersException e) {
            LOGGER.error("监听错误！", e.getMessage());
        }

        sendMsgToPort(portName, sendMsg);

        readMsgFromPort(portName);
    }*/
}

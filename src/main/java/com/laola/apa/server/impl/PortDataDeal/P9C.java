package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.costant.TestConstant;
import com.laola.apa.entity.EquipmentState;
import com.laola.apa.entity.Patient;
import com.laola.apa.entity.Project;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.DateUtils;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("p9c")
public class P9C implements PortDataDealService<String,Object> {
    @Autowired
    private ProjectTest projectTest;
    @Autowired
    private PatientService patientService;
    @Autowired
    EquipmentStateserver equipmentStateSever;
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    private ProjectMapper projectMapper;
    private Logger logger = LoggerFactory.getLogger(P9C.class);

    /**
     * 处理9c开头的命令。   来自于每一次样品架位移动时下位机发送到上位机
     * 收到架号位号信息
     *
     */
    @Override
    public String deal(Object... data) {
        String string = String.valueOf(data[0]);

        logger.info("GET PLACE&BAR DATA" + string);
        SerialPort serialPort = (SerialPort) data[1];
        if (string.indexOf("-")>0){
            return null;
        }
        //架号
        Integer rackNo = DateUtils.decodeHEX(string.substring(4, 6));
        //位号
        Integer placeNo = DateUtils.decodeHEX(string.substring(6, 8));
        //条形码scan_back
        String barCode = DateUtils.hexAscii2Str(string.substring(10, string.indexOf("0d")));


        List<Map<String, Object>> ableList = new ArrayList<>();
        //查询有效的项目
        if (data.length == 3 ){
            logger.info("replace new project PLACE&BAR DATA" + string);

            String sec = String.valueOf(data[2]);
            if (sec.equals("1")){
//                ableList = projectTest.selectNeverDo(1);
            }
        }else {
            //
            EquipmentState equipmentState = new EquipmentState(1, rackNo, placeNo);
            equipmentStateSever.update(equipmentState);
            ableList = projectTest.selectNeverDo(40);
        }
        for (Map<String, Object> ablemap : ableList) {
            int i = 0;
            //循环有效项目
            boolean b = (null != ablemap.get("rackNo")
                    && Integer.parseInt(String.valueOf(ablemap.get("placeNo"))) == placeNo
                    && Integer.parseInt(String.valueOf(ablemap.get("rackNo"))) == rackNo)
                    && !"2".equals(String.valueOf(ablemap.get("type")))
                    && !"6".equals(String.valueOf(ablemap.get("type")));
            if (    b
                    || (null !=ablemap.get("bar_code") && ablemap.get("bar_code").equals(barCode))
            ) {
                //架号位号相同，进入发送功能，取得命令 根据项目id
                String commond = projectTest.getIdList(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                //病员信息
                if ("1".equals(String.valueOf(ablemap.get("type")))){
                    Patient patient = new Patient(Integer.parseInt(String.valueOf(ablemap.get("human_code"))), "É".equals(barCode)?"":barCode);
                    //修改病员信息
                    patientService.update(patient);
                }
                //位号为一位，加上0 变成两个字符
                String strPlaceNo = String.valueOf(placeNo);
                if (strPlaceNo.length() == 1) {
                    strPlaceNo = "0" + strPlaceNo;
                }

                commond = TestConstant.TestHead + strPlaceNo + " " + commond + " 00 00";
                logger.info("发出的命令 123：" + commond);
                OutputStream outputStream = null;
                try {
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                if (send(commond, outputStream)) continue;
                i = 1;
            }
            if (("2".equals(String.valueOf(ablemap.get("type"))) || "6".equals(String.valueOf(ablemap.get("type"))) )
                    && (null != ablemap.get("rackNo") && Integer.parseInt(String.valueOf(ablemap.get("placeNo"))) == placeNo &&
                    rackNo == Integer.parseInt(String.valueOf(ablemap.get("rackNo"))))) {
                String commond = projectTest.getIdList(Integer.parseInt(String.valueOf(ablemap.get("id"))));

                String strPlaceNo = String.valueOf(placeNo);
                if (strPlaceNo.length() == 1) {
                    strPlaceNo = "0" + strPlaceNo;
                }

                commond = TestConstant.TestHead + strPlaceNo + " " + commond;
                logger.info("发出的命令；" + commond);
                OutputStream outputStream = null;
                try {
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                if (send(commond, outputStream)) continue;
                i = 2;
            }
            if (i > 0){
                Integer regentPlace = usedCodeServer.getCopies(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                logger.info("reagent count："+regentPlace);
                Project project = new Project();
                project.setId(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                if (regentPlace<=0){
                    project.setAbnormal(1);
                }else if (regentPlace<= 5){
                    project.setAbnormal(2);
                }
                //设置factor不为null 使得查询时不将该数据查出来
                project.setFactor(" ");
                projectMapper.updateByPrimaryKeySelective(project);
            }
        }
        return "200";
    }

    private boolean send(String commond, OutputStream outputStream) {
        try {
            byte[] binaryStr = DateUtils.hexStrToBinaryStr(commond);
            assert binaryStr != null;
            outputStream.write(binaryStr, 0, binaryStr.length);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}

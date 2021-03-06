package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.EquipmentStateserver;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
@Service("p86")
public class P86 implements PortDataDealService<Object, Object> {

    @Autowired
    private EquipmentStateserver equipmentStateSever;
    private Logger logger = LoggerFactory.getLogger(P86.class);

    /**
     * 处理仪器参数数据
     * @param data
     * @return
     */
    @Override
    public Object deal(Object... data) {

        EquipmentState thisEquipmentState = equipmentStateSever.queryById(1);

        String string = String.valueOf(data[0]);

        logger.info("GET INSTRUMENT STATE DATA" + string);
        //A12：进水标志   1/0代表进水
        int pureWater = DateUtils.decodeHEX(string.substring(30, 32));
        //A13：出水标志   1/0代表出水满
        int wasteWater = DateUtils.decodeHEX(string.substring(32, 34));
        //A15：撞针标志   1/0代表撞针
        int firingPin = DateUtils.decodeHEX(string.substring(36, 38));

        // A20：反应盘温度高位（计算方法：高位*256+高位）/1351（测试过程中返回有效）
        //A21：反应盘温度低位（测试过程中返回有效）
        float reactTemp = (Float.parseFloat(String.valueOf(DateUtils.decodeHEX(string.substring(46, 48)))) * 256F + Float.parseFloat(String.valueOf(DateUtils.decodeHEX(string.substring(48, 50))))) / 1351F;
        //A24：试剂盘温度
        int regentTemp = DateUtils.decodeHEX(string.substring(54, 56));
        //A25：发送数据组数。
        int numSent = DateUtils.decodeHEX(string.substring(56, 58));
        //A27：在测试的项目数
        int numberUnderTest = DateUtils.decodeHEX(string.substring(60, 62));
        //A28：所有项目数
        int numAll = DateUtils.decodeHEX(string.substring(62, 64));
        int b = 0;

if (numberUnderTest > 1){
    b = 1;
}
        //A28：所有项目数
        int a = DateUtils.decodeHEX(string.substring(4, 6));
//        logger.info("string==" + string);
        if (a == 1){

        }
        EquipmentState equipmentState = new EquipmentState(1, pureWater, wasteWater, firingPin, String.valueOf(new DecimalFormat("0.00").format(reactTemp)), String.valueOf(regentTemp), numSent, numberUnderTest, numAll, a,b);

        Integer thisA = thisEquipmentState.getA();
        if (null == thisA){
            thisA = 80;
        }
        if (a-thisA > 1 && a > 1){
            SerialUtil serialUtil = new SerialUtil();
            for (int i = 0; i < a-thisA-1; i++) {
                String no = String.valueOf(thisA + i + 1);
                no = DateUtils.DEC2HEX(no);
                String command = "E5 90 C1 " + no +" 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
                serialUtil.init(command);
            }
        }
//        logger.info("equipment=" + equipmentState.toString());
         equipmentStateSever.update(equipmentState);
        return null;
    }
}

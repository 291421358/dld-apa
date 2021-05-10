package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.EquipmentStateserver;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
@Service("p86")
public class P86 implements PortDataDealService<Object, String> {

    @Autowired
    private EquipmentStateserver equipmentStateSever;

    /**
     * 处理仪器参数数据
     * @param data
     * @return
     */
    @Override
    public Object deal(String... data) {
        String string = data[0];
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
//        logger.info("string==" + string);
        EquipmentState equipmentState = new EquipmentState(1, pureWater, wasteWater, firingPin, String.valueOf(new DecimalFormat("0.00").format(reactTemp)), String.valueOf(regentTemp), numSent, numberUnderTest, numAll);
//        logger.info("equipment=" + equipmentState.toString());
         equipmentStateSever.update(equipmentState);
        return null;
    }
}

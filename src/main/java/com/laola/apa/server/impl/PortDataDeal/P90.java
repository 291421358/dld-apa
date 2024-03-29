package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.EquipmentStateserver;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service("p90")
public class P90 implements PortDataDealService<EquipmentState,Object>{

    @Autowired
     public EquipmentStateserver equipmentStateSever;

    private Logger logger = LoggerFactory.getLogger(P90.class);
    /**
     * 处理仪器温度数据
     * @param data
     * @return
     */
    @Override
    public EquipmentState deal(Object ... data) {
        //取得窗口发来的字符串
        String string = String.valueOf(data[0]);
        logger.info("GET TEMPERATURE DATA" + string);
        String tHighBefore = string.substring(4, 6);
        String tHighBehind = string.substring(6, 8);
        // A20：反应盘温度高位（计算方法：高位*256+高位）/1351（测试过程中返回有效）
        //A21：反应盘温度低位（测试过程中返回有效）
        float reactTemp = (Float.parseFloat(String.valueOf(DateUtils.decodeHEX(tHighBefore))) * 256F + Float.parseFloat(String.valueOf(DateUtils.decodeHEX(tHighBehind)))) / 1351F;
        EquipmentState equipmentState = new EquipmentState(1, null, null, null, String.valueOf(new DecimalFormat("0.00").format(reactTemp)), null, null, null, null,null,null, null);
//        logger.info("equipment=" + equipmentState.toString());
         equipmentStateSever.update(equipmentState);

        return null;
    }

}

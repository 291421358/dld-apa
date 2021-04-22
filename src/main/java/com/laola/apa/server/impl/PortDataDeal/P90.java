package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.EquipmentStateserver;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service("p90")
public class P90 implements PortDataDealService<EquipmentState,String>{

    @Autowired
     public EquipmentStateserver equipmentStateSever;

    @Override
    public EquipmentState deal(String ... data) {
        //取得窗口发来的字符串
        String string = data[0];
        String tHighBefore = string.substring(4, 6);
        String tHighBehind = string.substring(6, 8);
        // A20：反应盘温度高位（计算方法：高位*256+高位）/1351（测试过程中返回有效）
        //A21：反应盘温度低位（测试过程中返回有效）
        float reactTemp = (Float.parseFloat(String.valueOf(DateUtils.decodeHEX(tHighBefore))) * 256F + Float.parseFloat(String.valueOf(DateUtils.decodeHEX(tHighBehind)))) / 1351F;
        EquipmentState equipmentState = new EquipmentState(1, null, null, null, String.valueOf(new DecimalFormat("0.00").format(reactTemp)), null, null, null, null);
//        logger.info("equipment=" + equipmentState.toString());
         equipmentStateSever.update(equipmentState);

        return null;
    }

}

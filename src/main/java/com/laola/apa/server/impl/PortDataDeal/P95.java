package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.RegentPlace;
import com.laola.apa.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("p95")
public class P95 implements PortDataDealService<String,Object> {
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    ProjectTest projectTest;
    @Autowired
    ScalingIntf scalingIntf;
    private static final Logger logger = LoggerFactory.getLogger(P95.class);

    /**
     * 处理二维码(无数据)
     * @param data
     * @return
     */
    @Override
    public String deal(Object... data) {

        String string = String.valueOf(data[0]);
        logger.info("PROJECT QR INFORMATION NO DATA" + string);

        //试剂位置
        String place = string.substring(4, 6);
        setRegentPlace(place);
        return "";
    }
    /**
     * @apiNote 设置试剂位置
     * @author tzhh
     * @date 2021/5/17 14:34
	 * @param place
     * @return
     **/
    private void setRegentPlace(String place) {
        //设置试剂位置
        RegentPlace regentPlace = new RegentPlace();
        //设置位置
        regentPlace.setId(Integer.parseInt(place));
        //设置项目id
        regentPlace.setProjectParamId(0);
        regentPlace.setPlace(Integer.parseInt(place));
        //设置试剂code
        regentPlace.setCode("0");
        int i1 = reagentPlaceIntf.updateRegentPlace(regentPlace);
    }

}

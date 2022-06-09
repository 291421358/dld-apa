package com.laola.apa.server;

import com.laola.apa.entity.RegentPlace;

import java.util.List;
import java.util.Map;

public interface ReagentPlaceIntf {
    List<Map<String, Object>> getRegentPlace();
    //修改试剂位置
    int updateRegentPlace(RegentPlace regentPlace);

    List<Map<String, Object>> getBoolScal();
    //获取试剂数
}

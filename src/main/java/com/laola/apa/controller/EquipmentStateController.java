package com.laola.apa.controller;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.server.EquipmentStateserver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (EquipmentState)表控制层
 *
 * @author tzhh
 * @since 2020-06-01 16:52:05
 */
@RestController
@RequestMapping("equipmentState")
public class EquipmentStateController {
    /**
     * 服务对象
     */
    @Resource
    private EquipmentStateserver equipmentStateService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public EquipmentState selectOne(Integer id) {
        return this.equipmentStateService.queryById(id);
    }
    
    /**
     * 增加数据
     *
     * @param equipmentState 对象
     * @return 对象
     */
    @GetMapping("insert")
    public EquipmentState insert(EquipmentState equipmentState) {
        return this.equipmentStateService.insert(equipmentState);
    }

}
package com.laola.apa.server.impl;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.mapper.EquipmentStateMapper;
import com.laola.apa.server.EquipmentStateserver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EquipmentState)表服务实现类
 *
 * @author tzhh
 * @since 2020-06-01 16:52:05
 */
@Service("equipmentStateService")
public class EquipmentStateImpl implements EquipmentStateserver {
    @Resource
    private EquipmentStateMapper equipmentStateMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EquipmentState queryById(Integer id) {
        EquipmentState equipmentState = this.equipmentStateMapper.queryById(id);
             float v = Float.parseFloat(String.valueOf(equipmentState.getTemperatureControlCalibration())) + Float.parseFloat(equipmentState.getReactTemp());
        equipmentState.setReactTemp(String.valueOf(v));
        return equipmentState;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EquipmentState> queryAllByLimit(int offset, int limit) {
        return this.equipmentStateMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param equipmentState 实例对象
     * @return 实例对象
     */
    @Override
    public EquipmentState insert(EquipmentState equipmentState) {
        this.equipmentStateMapper.insert(equipmentState);
        return equipmentState;
    }

    /**
     * 修改数据
     *
     * @param equipmentState 实例对象
     * @return 实例对象
     */
    @Override
    public EquipmentState update(EquipmentState equipmentState) {
        this.equipmentStateMapper.update(equipmentState);
        return this.queryById(equipmentState.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.equipmentStateMapper.deleteById(id) > 0;
    }

    @Override
    public int temperatureControlCalibration(int i) {
        return  this.equipmentStateMapper.temperatureControlCalibration(i);
    }
}
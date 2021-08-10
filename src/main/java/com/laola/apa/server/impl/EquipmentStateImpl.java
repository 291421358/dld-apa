package com.laola.apa.server.impl;

import com.laola.apa.entity.EquipmentState;
import com.laola.apa.mapper.EquipmentStateMapper;
import com.laola.apa.server.EquipmentStateserver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
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
        Integer temperatureControlCalibration = equipmentState.getTemperatureControlCalibration();
        Float v = (float) temperatureControlCalibration /100F+ Float.parseFloat(equipmentState.getReactTemp());
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String obj = v.toString();
        String temp=decimalFormat.format(v);//format 返回的是字符串
        equipmentState.setReactTemp(temp);
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
        this.equipmentStateMapper.updateByPrimaryKeySelective(equipmentState);
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
    public int temperatureControlCalibration(Float i) {
        return  this.equipmentStateMapper.temperatureControlCalibration(i*100);
    }
}
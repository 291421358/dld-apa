package com.laola.apa.server;

import com.laola.apa.entity.EquipmentState;

import java.util.List;

/**
 * (EquipmentState)表服务接口
 *
 * @author tzhh
 * @since 2020-06-01 16:52:05
 */
public interface EquipmentStateserver {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EquipmentState queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EquipmentState> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param equipmentState 实例对象
     * @return 实例对象
     */
    EquipmentState insert(EquipmentState equipmentState);

    /**
     * 修改数据
     *
     * @param equipmentState 实例对象
     * @return 实例对象
     */
    EquipmentState update(EquipmentState equipmentState);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    int temperatureControlCalibration(int i);
}
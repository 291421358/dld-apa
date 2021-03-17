package com.laola.apa.mapper;

import com.laola.apa.entity.EquipmentState;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EquipmentState)表数据库访问层
 *
 * @author tzhh
 * @since 2020-06-01 16:52:05
 */
public interface EquipmentStateMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EquipmentState queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EquipmentState> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param equipmentState 实例对象
     * @return 对象列表
     */
    List<EquipmentState> queryAll(EquipmentState equipmentState);

    /**
     * 新增数据
     *
     * @param equipmentState 实例对象
     * @return 影响行数
     */
    int insert(EquipmentState equipmentState);

    /**
     * 修改数据
     *
     * @param equipmentState 实例对象
     * @return 影响行数
     */
    int update(EquipmentState equipmentState);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
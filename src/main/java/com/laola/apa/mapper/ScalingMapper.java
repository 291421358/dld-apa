package com.laola.apa.mapper;

import com.laola.apa.entity.Scaling;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Scaling)表数据库访问层
 *
 * @author tzhh
 * @since 2020-05-28 16:05:58
 */
@Service
public interface ScalingMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param dateid 主键
     * @return 实例对象
     */
    Scaling queryById(String dateid);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Scaling> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param scaling 实例对象
     * @return 对象列表
     */
    List<Scaling> queryAll(Scaling scaling);

    /**
     * 新增数据
     *
     * @param scaling 实例对象
     * @return 影响行数
     */
    int insert(Scaling scaling);

    /**
     * 修改数据
     *
     * @param scaling 实例对象
     * @return 影响行数
     */
    int update(Scaling scaling);

    /**
     * 通过主键删除数据
     *
     * @param dateid 主键
     * @return 影响行数
     */
    int deleteById(String dateid);

}
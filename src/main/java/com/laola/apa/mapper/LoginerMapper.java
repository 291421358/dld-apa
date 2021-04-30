package com.laola.apa.mapper;

import com.laola.apa.entity.Loginer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Loginer)表数据库访问层
 *
 * @author tzhh
 * @since 2021-04-29 16:09:11
 */
public interface LoginerMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Loginer queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Loginer> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param loginer 实例对象
     * @return 对象列表
     */
    List<Loginer> queryAll(Loginer loginer);

    /**
     * 新增数据
     *
     * @param loginer 实例对象
     * @return 影响行数
     */
    int insert(Loginer loginer);

    /**
     * 修改数据
     *
     * @param loginer 实例对象
     * @return 影响行数
     */
    int update(Loginer loginer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int verfication(String u, String p);
}
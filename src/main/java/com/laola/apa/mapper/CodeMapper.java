package com.laola.apa.mapper;

import com.laola.apa.entity.Code;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Code)表数据库访问层
 *
 * @author tzhh
 * @since 2021-02-20 15:12:09
 */
public interface CodeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Code queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Code> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param code 实例对象
     * @return 对象列表
     */
    List<Code> queryAll(Code code);

    /**
     * 新增数据
     *
     * @param code 实例对象
     * @return 影响行数
     */
    int insert(Code code);

    /**
     * 修改数据
     *
     * @param code 实例对象
     * @return 影响行数
     */
    int update(Code code);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    Code queryLastOne();

    Code queryByCode(String code);
}
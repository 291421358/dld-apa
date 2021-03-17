package com.laola.apa.mapper;

import com.laola.apa.entity.QC;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (QC)表数据库访问层
 *
 * @author tzhh
 * @since 2020-06-16 13:41:45
 */
public interface QCMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    QC queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<QC> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param qC 实例对象
     * @return 对象列表
     */
    List<QC> queryAll(QC qC);

    /**
     * 新增数据
     *
     * @param qC 实例对象
     * @return 影响行数
     */
    int insert(QC qC);

    /**
     * 修改数据
     *
     * @param qC 实例对象
     * @return 影响行数
     */
    int update(QC qC);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
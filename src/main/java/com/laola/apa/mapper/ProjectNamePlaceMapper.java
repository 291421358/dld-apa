package com.laola.apa.mapper;

import com.laola.apa.entity.ProjectNamePlace;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ProjectNamePlace)表数据库访问层
 *
 * @author tzhh
 * @since 2020-05-13 11:03:28
 */
public interface ProjectNamePlaceMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProjectNamePlace queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ProjectNamePlace> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param projectNamePlace 实例对象
     * @return 对象列表
     */
    List<ProjectNamePlace> queryAll(ProjectNamePlace projectNamePlace);

    /**
     * 新增数据
     *
     * @param projectNamePlace 实例对象
     * @return 影响行数
     */
    int insert(ProjectNamePlace projectNamePlace);

    /**
     * 修改数据
     *
     * @param projectNamePlace 实例对象
     * @return 影响行数
     */
    int update(ProjectNamePlace projectNamePlace);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
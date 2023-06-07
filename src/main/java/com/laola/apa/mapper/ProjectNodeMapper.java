package com.laola.apa.mapper;

import com.laola.apa.entity.ProjectNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (ProjectNode)表数据库访问层
 *
 * @author tzhh
 * @since 2021-08-20 09:32:14
 */
@Service
public interface ProjectNodeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProjectNode queryById(Integer id);

    /**
     * 通过ID查询单条数据
     *
     * @param pId 主键
     * @return 实例对象
     */
    ProjectNode queryByPId(Integer pId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ProjectNode> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param projectNode 实例对象
     * @return 对象列表
     */
    List<ProjectNode> queryAll(ProjectNode projectNode);

    /**
     * 新增数据
     *
     * @param projectNode 实例对象
     * @return 影响行数
     */
    int insert(ProjectNode projectNode);

    /**
     * 修改数据
     *
     * @param projectNode 实例对象
     * @return 影响行数
     */
    int update(ProjectNode projectNode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
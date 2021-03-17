package com.laola.apa.server;

import com.laola.apa.entity.ProjectNamePlace;
import java.util.List;

/**
 * (ProjectNamePlace)表服务接口
 *
 * @author tzhh
 * @since 2020-05-13 11:03:28
 */
public interface ProjectNamePlaceServer {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProjectNamePlace queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ProjectNamePlace> queryAllByLimit(int offset, int limit);


    /**
     * 查询多条数据条件查询
     *
     * @param projectNamePlace 对象
     * @return 对象列表
     */
    List<ProjectNamePlace> queryAll(ProjectNamePlace projectNamePlace);

    /**
     * 新增数据
     *
     * @param projectNamePlace 实例对象
     * @return 实例对象
     */
    ProjectNamePlace insert(ProjectNamePlace projectNamePlace);

    /**
     * 修改数据
     *
     * @param projectNamePlace 实例对象
     * @return 实例对象
     */
    ProjectNamePlace update(ProjectNamePlace projectNamePlace);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
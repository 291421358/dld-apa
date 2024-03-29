package com.laola.apa.server;

import com.laola.apa.entity.ProjectNode;
import java.util.List;

/**
 * (ProjectNode)表服务接口
 *
 * @author tzhh
 * @since 2021-08-20 09:32:14
 */
public interface ProjectNodeServer {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProjectNode queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ProjectNode> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param projectNode 实例对象
     * @return 实例对象
     */
    ProjectNode insert(ProjectNode projectNode);

    /**
     * 修改数据
     *
     * @param projectNode 实例对象
     * @return 实例对象
     */
    ProjectNode update(ProjectNode projectNode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
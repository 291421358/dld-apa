package com.laola.apa.server.impl;

import com.laola.apa.entity.ProjectNode;
import com.laola.apa.mapper.ProjectNodeMapper;
import com.laola.apa.server.ProjectNodeServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProjectNode)表服务实现类
 *
 * @author tzhh
 * @since 2021-08-20 09:32:14
 */
@Service("projectNodeService")
public class ProjectNodeImpl implements ProjectNodeServer {
    @Resource
    private ProjectNodeMapper projectNodeMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ProjectNode queryById(Integer id) {
        return this.projectNodeMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ProjectNode> queryAllByLimit(int offset, int limit) {
        return this.projectNodeMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param projectNode 实例对象
     * @return 实例对象
     */
    @Override
    public ProjectNode insert(ProjectNode projectNode) {
        this.projectNodeMapper.insert(projectNode);
        return projectNode;
    }

    /**
     * 修改数据
     *
     * @param projectNode 实例对象
     * @return 实例对象
     */
    @Override
    public ProjectNode update(ProjectNode projectNode) {
        this.projectNodeMapper.update(projectNode);
        return this.queryById(projectNode.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.projectNodeMapper.deleteById(id) > 0;
    }
}
package com.laola.apa.server.impl;

import com.laola.apa.entity.ProjectNamePlace;
import com.laola.apa.mapper.ProjectNamePlaceMapper;
import com.laola.apa.server.ProjectNamePlaceServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProjectNamePlace)表服务实现类
 *
 * @author tzhh
 * @since 2020-05-13 11:03:28
 */
@Service("projectNamePlaceService")
public class ProjectNamePlaceImpl implements ProjectNamePlaceServer {
    @Resource
    private ProjectNamePlaceMapper projectNamePlaceMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ProjectNamePlace queryById(Integer id) {
        return this.projectNamePlaceMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ProjectNamePlace> queryAllByLimit(int offset, int limit) {
        return this.projectNamePlaceMapper.queryAllByLimit(offset, limit);
    }


    @Override
    public List<ProjectNamePlace> queryAll(ProjectNamePlace projectNamePlace){
        return this.projectNamePlaceMapper.queryAll(projectNamePlace);
    }

    /**
     * 新增数据
     *
     * @param projectNamePlace 实例对象
     * @return 实例对象
     */
    @Override
    public ProjectNamePlace insert(ProjectNamePlace projectNamePlace) {
        this.projectNamePlaceMapper.insert(projectNamePlace);
        return projectNamePlace;
    }

    /**
     * 修改数据
     *
     * @param projectNamePlace 实例对象
     * @return 实例对象
     */
    @Override
    public ProjectNamePlace update(ProjectNamePlace projectNamePlace) {
        this.projectNamePlaceMapper.update(projectNamePlace);
        return this.queryById(projectNamePlace.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.projectNamePlaceMapper.deleteById(id) > 0;
    }
}
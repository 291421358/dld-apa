package com.laola.apa.controller;

import com.laola.apa.entity.ProjectNamePlace;
import com.laola.apa.server.ProjectNamePlaceServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProjectNamePlace)表控制层
 *
 * @author tzhh
 * @since 2020-05-13 11:03:28
 */
@RestController
@RequestMapping("projectNamePlace")
public class ProjectNamePlaceController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNamePlaceServer projectNamePlaceService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ProjectNamePlace selectOne(Integer id) {
        return this.projectNamePlaceService.queryById(id);
    }

    /**
     * 查询所有数据
     *
     * @param
     * @return 对象列表
     */
    @GetMapping("queryAll")
    public List<ProjectNamePlace> queryAll() {
        return this.projectNamePlaceService.queryAll(null);
    }
    
    /**
     * 增加数据
     *
     * @param projectNamePlace 对象
     * @return 对象
     */
    @GetMapping("insert")
    public ProjectNamePlace insert(ProjectNamePlace projectNamePlace) {
        return this.projectNamePlaceService.insert(projectNamePlace);
    }

}
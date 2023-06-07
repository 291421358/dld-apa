package com.laola.apa.controller;

import com.laola.apa.entity.ProjectNode;
import com.laola.apa.server.ProjectNodeServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProjectNode)表控制层
 *
 * @author tzhh
 * @since 2021-08-20 09:32:14
 */
@RestController
@RequestMapping("projectNode")
public class ProjectNodeController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNodeServer projectNodeService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ProjectNode selectOne(Integer id) {
        return this.projectNodeService.queryById(id);
    }
    
    /**
     * 增加数据
     *
     * @param projectNode 对象
     * @return 对象
     */
    @GetMapping("insert")
    public ProjectNode insert(ProjectNode projectNode) {
        return this.projectNodeService.insert(projectNode);
    }

}
package com.laola.apa.controller;

import com.laola.apa.entity.Loginer;
import com.laola.apa.server.LoginerService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * (Loginer)表控制层
 *
 * @author tzhh
 * @since 2021-04-29 16:09:11
 */
@RestController
@RequestMapping("loginer")
public class LoginerController {
    /**
     * 服务对象
     */
    @Resource
    private LoginerService loginerService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Loginer selectOne(Integer id) {
        return this.loginerService.queryById(id);
    }
    
    /**
     * 增加数据
     *
     * @param loginer 对象
     * @return 对象
     */
    @GetMapping("insert")
    public Loginer insert(Loginer loginer) {
        return this.loginerService.insert(loginer);
    }

    @GetMapping("verification")
    public boolean verification(String un,String pa){
        return this.loginerService.verification(un,pa);
    }

}
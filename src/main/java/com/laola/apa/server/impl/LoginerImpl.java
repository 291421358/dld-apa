package com.laola.apa.server.impl;

import com.laola.apa.entity.Loginer;
import com.laola.apa.mapper.LoginerMapper;
import com.laola.apa.server.LoginerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Loginer)表服务实现类
 *
 * @author tzhh
 * @since 2021-04-29 16:09:11
 */
@Service("loginerService")
public class LoginerImpl implements LoginerService {
    @Resource
    private LoginerMapper loginerMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Loginer queryById(Integer id) {
        return this.loginerMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Loginer> queryAllByLimit(int offset, int limit) {
        return this.loginerMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param loginer 实例对象
     * @return 实例对象
     */
    @Override
    public Loginer insert(Loginer loginer) {
        this.loginerMapper.insert(loginer);
        return loginer;
    }

    /**
     * 修改数据
     *
     * @param loginer 实例对象
     * @return 实例对象
     */
    @Override
    public Loginer update(Loginer loginer) {
        this.loginerMapper.update(loginer);
        return this.queryById(loginer.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.loginerMapper.deleteById(id) > 0;
    }

    /**
     *验证
     * @param u 账号
     * @param p 密码
     * @return 是否成功
     */
    @Override
    public boolean verification(String u, String p) {


        return this.loginerMapper.verfication(u,p);
    }
}
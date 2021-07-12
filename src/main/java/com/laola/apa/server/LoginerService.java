package com.laola.apa.server;

import com.laola.apa.entity.Loginer;
import java.util.List;

/**
 * (Loginer)表服务接口
 *
 * @author tzhh
 * @since 2021-04-29 16:09:11
 */
public interface LoginerService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Loginer queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Loginer> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param loginer 实例对象
     * @return 实例对象
     */
    Loginer insert(Loginer loginer);

    /**
     * 修改数据
     *
     * @param loginer 实例对象
     * @return 实例对象
     */
    Loginer update(Loginer loginer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean delByName(Integer id);

    boolean delByName(Loginer loginer);

    /**
     * 验证
     * @param u 账号
     * @param p 密码
     * @return 是否成功
     */
    int verification(String u, String p);

}
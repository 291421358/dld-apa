package com.laola.apa.server;

import com.laola.apa.entity.Code;

import java.util.List;

/**
 * (Code)表服务接口
 *
 * @author tzhh
 * @since 2021-02-20 14:45:10
 */
public interface CodeServer {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Code queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Code> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param code 实例对象
     * @return 实例对象
     */
    Code insert(Code code);

    /**
     * 修改数据
     *
     * @param code 实例对象
     * @return 实例对象
     */
    Code update(Code code);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 获取下一个code
     *
     * @return Code
     */
    Code queryNextCode();

}
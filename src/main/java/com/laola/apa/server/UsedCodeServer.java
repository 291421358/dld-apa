package com.laola.apa.server;

import com.laola.apa.entity.UsedCode;
import java.util.List;

/**
 * (UsedCode)表服务接口
 *
 * @author tzhh
 * @since 2021-02-23 10:30:41
 */
public interface UsedCodeServer {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UsedCode queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UsedCode> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param usedCode 实例对象
     * @return 实例对象
     */
    UsedCode insert(UsedCode usedCode);

    /**
     * 修改数据
     *
     * @param usedCode 实例对象
     * @return 实例对象
     */
    UsedCode update(UsedCode usedCode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    UsedCode queryOrInsert(String code, int total);

    int minusOneCopyReagent(int id);

    Integer getCopies(int id);
}
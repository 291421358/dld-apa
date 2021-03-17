package com.laola.apa.server;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.QC;
import java.util.List;

/**
 * (QC)表服务接口
 *
 * @author tzhh
 * @since 2020-06-16 13:41:45
 */
public interface QCserver {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    QC queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<QC> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param qC 实例对象
     * @return 实例对象
     */
    int insert(QC qC);

    /**
     * 修改数据
     *
     * @param qC 实例对象
     * @return 实例对象
     */
    int update(QC qC);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 查询最后一个定标项目
     * @return
     */
    Project queryLast();
}
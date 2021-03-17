package com.laola.apa.server.impl;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.QC;
import com.laola.apa.mapper.QCMapper;
import com.laola.apa.server.QCserver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (QC)表服务实现类
 *
 * @author tzhh
 * @since 2020-06-16 13:41:45
 */
@Service("qCService")
public class QCImpl implements QCserver {
    @Resource
    private QCMapper qCMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public QC queryById(Integer id) {
        return this.qCMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<QC> queryAllByLimit(int offset, int limit) {
        return this.qCMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param qC 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(QC qC) {
        return  this.qCMapper.insert(qC);

    }

    /**
     * 修改数据
     *
     * @param qC 实例对象
     * @return 实例对象
     */
    @Override
    public int update(QC qC) {
        QC qc = queryById(Integer.valueOf(qC.getProjectParamNum()));
        if (qc != null){
            return this.qCMapper.update(qC);
        }else {
          return this.insert(qC);
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.qCMapper.deleteById(id) > 0;
    }

    @Override
    public Project queryLast() {
        return null;
    }
}
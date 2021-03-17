package com.laola.apa.server.impl;

import com.laola.apa.entity.UsedCode;
import com.laola.apa.mapper.UsedCodeMapper;
import com.laola.apa.server.UsedCodeServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UsedCode)表服务实现类
 *
 * @author tzhh
 * @since 2021-02-23 10:30:41
 */
@Service("usedCodeService")
public class UsedCodeImpl implements UsedCodeServer {
    @Resource
    private UsedCodeMapper usedCodeMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UsedCode queryById(Integer id) {
        return this.usedCodeMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<UsedCode> queryAllByLimit(int offset, int limit) {
        return this.usedCodeMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param usedCode 实例对象
     * @return 实例对象
     */
    @Override
    public UsedCode insert(UsedCode usedCode) {
        this.usedCodeMapper.insert(usedCode);
        return usedCode;
    }

    /**
     * 修改数据
     *
     * @param usedCode 实例对象
     * @return 实例对象
     */
    @Override
    public UsedCode update(UsedCode usedCode) {
        this.usedCodeMapper.update(usedCode);
        return this.queryById(usedCode.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.usedCodeMapper.deleteById(id) > 0;
    }


    /**
     * 查询是否有重复并插入
     */
    @Override
    public UsedCode queryOrInsert(String code, int total) {
        //查询是否有该code
        UsedCode i = usedCodeMapper.queryByCode(code);
        if (i == null){
            UsedCode usedCode = new UsedCode();
            usedCode.setCode(code);
            usedCode.setCount(total);
            usedCode.setTotal(total);
            usedCode.setCount2(total);
            usedCode.setTotal2(total);
            insert(usedCode);
        }
        return i;
    }

    @Override
    public int minusOneCopyReagent(int id) {
        usedCodeMapper.minusOneCopyReagentR1(id);
        usedCodeMapper.minusOneCopyReagentR2(id);
        return 0;
    }

    @Override
    public Integer getCopies(int id) {
        Integer copies = usedCodeMapper.getCopies(id);
        if (copies == null){
            copies = 0;
        }
        return copies;
    }
}
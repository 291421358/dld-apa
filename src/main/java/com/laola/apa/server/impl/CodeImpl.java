package com.laola.apa.server.impl;

import com.laola.apa.entity.Code;
import com.laola.apa.mapper.CodeMapper;
import com.laola.apa.server.CodeServer;
import com.laola.apa.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Code)表服务实现类
 *
 * @author tzhh
 * @since 2021-02-20 14:45:10
 */
@Service("codeService")
public class CodeImpl implements CodeServer {
    @Resource
    private CodeMapper codeMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Code queryById(Integer id) {
        return this.codeMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Code> queryAllByLimit(int offset, int limit) {
        return this.codeMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param code 实例对象
     * @return 实例对象
     */
    @Override
    public Code insert(Code code) {
        this.codeMapper.insert(code);
        return code;
    }

    /**
     * 修改数据
     *
     * @param code 实例对象
     * @return 实例对象
     */
    @Override
    public Code update(Code code) {
        this.codeMapper.update(code);
        return this.queryById(code.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.codeMapper.deleteById(id) > 0;
    }

    /**
     * 查询下一个code
     *
     * @return 是否成功
     */
    @Override
    public Code queryNextCode() {
        Code code = codeMapper.queryLastOne();
        Code newCode = new Code();
        String i = "";
        if (null == code){
            i="a";
        }else {
            String codeCode = code.getCode();
            i = DateUtils.ToNumberSystem26(DateUtils.FromNumberSystem26(codeCode) + 1);
        }
        newCode.setCode(i);
        codeMapper.insert(newCode);
        return newCode;
    }


}
package com.laola.apa.server.impl;

import com.laola.apa.entity.Doctor;
import com.laola.apa.mapper.DoctorMapper;
import com.laola.apa.server.DoctorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Doctor)表服务实现类
 *
 * @author tzhh
 * @since 2020-05-06 16:42:39
 */
@Service("doctorService")
public class DoctorImpl implements DoctorService {
    @Resource
    private DoctorMapper doctorMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Doctor queryById(Integer id) {
        return this.doctorMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Doctor> queryAllByLimit(int offset, int limit) {
        return this.doctorMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<Doctor> queryAll(Doctor doctor) {
        return this.doctorMapper.queryAll(doctor);
    }

    /**
     * 新增数据
     *
     * @param doctor 实例对象
     * @return 实例对象
     */
    @Override
    public Doctor insert(Doctor doctor) {

        this.doctorMapper.insert(doctor);
        return doctor;
    }

    /**
     * 修改数据
     *
     * @param doctor 实例对象
     * @return 实例对象
     */
    @Override
    public Doctor update(Doctor doctor) {
        this.doctorMapper.update(doctor);
        return this.queryById(doctor.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.doctorMapper.deleteById(id) > 0;
    }

    /**
     * 通过主键批量删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteByIds(int[] ids) {
        System.out.println(ids);
        return this.doctorMapper.deleteByIds(ids) > 0;
    }
}
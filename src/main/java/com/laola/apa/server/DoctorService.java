package com.laola.apa.server;

import com.laola.apa.entity.Doctor;
import java.util.List;

/**
 * (Doctor)表服务接口
 *
 * @author tzhh
 * @since 2020-05-06 16:42:39
 */
public interface DoctorService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Doctor queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Doctor> queryAllByLimit(int offset, int limit);

    List<Doctor> queryAll(Doctor doctor);

    /**
     * 新增数据
     *
     * @param doctor 实例对象
     * @return 实例对象
     */
    Doctor insert(Doctor doctor);

    /**
     * 修改数据
     *
     * @param doctor 实例对象
     * @return 实例对象
     */
    Doctor update(Doctor doctor);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    boolean deleteByIds(int[] ids);
}
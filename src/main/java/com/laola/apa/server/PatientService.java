package com.laola.apa.server;

import com.laola.apa.entity.Patient;

import java.util.List;
import java.util.Map;

/**
 * (Patient)表服务接口
 *
 * @author tzhh
 * @since 2020-05-06 16:18:03
 */
public interface PatientService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Patient queryById(Integer id);

    List<Patient> queryAll(Patient patient);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Patient> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param patient 实例对象
     * @return 实例对象
     */
    Patient insert(Patient patient);

    /**
     * 修改数据
     *
     * @param patient 实例对象
     * @return 实例对象
     */
    int update(Patient patient);

    int updateById(Patient patient);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * @apiNote 通过日期查询病员列表
     * @author tzhh
     * @date 2021/6/30 14:41
     * @param starttime
     * @param code
     * @param name
     * @param id
     * @return {@link List< Map< String, Object>>}
     **/
    List<Patient> getPatientListByDate(String starttime, String code, String name, String id);

    List<Map<String, String>> queryAllDay();
}
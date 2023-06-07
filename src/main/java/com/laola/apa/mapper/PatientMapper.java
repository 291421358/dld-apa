package com.laola.apa.mapper;

import com.laola.apa.entity.Patient;
import com.laola.apa.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * (Patient)表数据库访问层
 *
 * @author tzhh
 * @since 2020-05-06 16:36:23
 */
public interface PatientMapper extends MyMapper<Patient> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Patient queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Patient> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param patient 实例对象
     * @return 对象列表
     */
    List<Patient> queryAll(Patient patient);

    /**
     * 新增数据
     *
     * @param patient 实例对象
     * @return 影响行数
     */
    int insert(Patient patient);

    /**
     * 修改数据
     *
     * @param patient 实例对象
     * @return 影响行数
     */
    int update(Patient patient);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
    /**
     * 通过删除数据
     *
     */
    void deleteProjects();

/**
 * @apiNote 批量增加病员
 * @author tzhh
 * @date 2021/6/30 17:34
 * @param patients
 * @return {@link int}
 **/
    int insertPatientList(@Param("patients")  List patients);

    List<Map<String, String>> queryAllDay();
}
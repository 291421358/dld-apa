package com.laola.apa.server.impl;

import com.laola.apa.entity.Patient;
import com.laola.apa.entity.Project;
import com.laola.apa.mapper.PatientMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.server.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Patient)表服务实现类
 *
 * @author tzhh
 * @since 2020-05-06 16:38:03
 */
@Service("patientService")
public class PatientImpl implements PatientService {
    @Resource
    private PatientMapper patientMapper;
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Patient queryById(Integer id) {
        return this.patientMapper.queryById(id);
    }

    /**
     * 根据条件查询
     *
     * @param patient 对象
     * @return 对象列表
     */
    @Override
    public List<Patient> queryAll(Patient patient) {
        System.out.println(patient.toString());
        return this.patientMapper.queryAll(patient);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Patient> queryAllByLimit(int offset, int limit) {
        return this.patientMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param patient 实例对象
     * @return 实例对象
     */
    @Override
    public Patient insert(Patient patient) {
        this.patientMapper.insert(patient);
        return patient;
    }

    /**
     * 修改数据
     *
     * @param patient 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Patient patient) {
        if (null == patient.getId() || patient.getId()<0){
            return  2;
        }
        List<Patient> p = this.queryAll(patient);
        if (0 == p.size()){
            this.patientMapper.insert(patient);
            return 2;
        }
        this.patientMapper.update(patient);
        return 1;
    }

    /**
     * 通过主键删除数据
     * @param humanCode 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer humanCode) {
        Example example =new Example(Project.class);
        example.createCriteria().andEqualTo("humanCode",humanCode);
        projectMapper.deleteByExample(example);
        return this.patientMapper.deleteById(humanCode) > 0;
    }
}
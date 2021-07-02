package com.laola.apa.server.impl;

import com.laola.apa.entity.Patient;
import com.laola.apa.entity.Project;
import com.laola.apa.mapper.PatientMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.server.PatientService;
import com.laola.apa.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
     * 修改数据
     *
     * @param patient 实例对象
     * @return 实例对象
     */
    @Override
    public int updateById(Patient patient) {

        return this.patientMapper.updateByPrimaryKeySelective(patient);
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

    /**
     * @apiNote  通过日期查询病员列表
     * @author tzhh
     * @date 2021/6/30 14:42
     * @param starttime
     * @param code
     * @param name
     * @param id
     * @return {@link List< Map< String, Object>>}
     **/
    @Override
    public List<Patient> getPatientListByDate(String starttime, String code, String name, String id) {
        starttime = starttime+" 00:00";
//        Patient patient = new Patient();
        Example example = new Example(Patient.class);
        Example.Criteria criteria = example.createCriteria();
        String preDate = DataUtil.getPreDateByUnit(starttime, 1, 6);
//                    logger.info("factor:"+factor);
        criteria.andBetween("inspectionDate", starttime, preDate);
        if ( code != null && !code.equals("")){
            criteria.andEqualTo("code",code);
        }
        if ( name != null && !name.equals("")){
            criteria.andEqualTo("name",name);
        }
        if ( id != null && !id.equals("")){
            criteria.andEqualTo("id",id);
        }
        List<Patient> patients = patientMapper.selectByExample(example);
        return patients;
    }
}
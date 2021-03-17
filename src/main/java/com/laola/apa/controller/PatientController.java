package com.laola.apa.controller;

import com.laola.apa.entity.Patient;
import com.laola.apa.server.PatientService;
import com.laola.apa.server.ProjectTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (Patient)表控制层
 *
 * @author tzhh
 * @since 2020-05-06 16:20:51
 */
@RestController
@RequestMapping(value = "patient")
public class PatientController {
    /**
     * 服务对象
     */
    @Resource
    private PatientService patientService;
    @Autowired
    private ProjectTest projectTest;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Patient selectOne(Integer id) {
        return this.patientService.queryById(id);
    }
    /**
     * 通过主键修改单条数据
     *
     * @param patient 对象
     * @return 单条数据
     */
    @GetMapping("update")
    public int update(Patient patient) {
        System.out.println(patient.toString());
        return this.patientService.update(patient);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param patient 对象
     * @return 对象列表
     */
    @GetMapping("queryAll")
    public List<Patient> queryAll(Patient patient) {
        return this.patientService.queryAll(patient);
    }

    @GetMapping("deleteByHumanCode")
    public boolean deleteByHumanCode(Integer humanCode) {
        return this.patientService.deleteById(humanCode);
    }

    @GetMapping("getProjectsByCon")
    public List<Map<String, Object>> getProjectsByCon(String starttime, int humancode){
        return projectTest.getProjectsByCon(starttime, humancode);
    }
}
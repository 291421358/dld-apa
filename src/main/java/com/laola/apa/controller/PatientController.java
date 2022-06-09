package com.laola.apa.controller;

import com.laola.apa.DO.SerialDO;
import com.laola.apa.common.Result;
import com.laola.apa.common.StateCodeEnum;
import com.laola.apa.entity.Patient;
import com.laola.apa.serial.SerialController;
import com.laola.apa.server.PatientService;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.task.FutureTaskable;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
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
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
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
        String x = patient.toString();
        System.out.println(x);

        return this.patientService.updateById(patient);
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

    /**
     * @apiNote 通过用户id删除
     * @author tzhh
     * @date 2021/5/27 15:29
     * @param humanCode
     * @return {@link boolean}
     **/
    @GetMapping("deleteByHumanCode")
    public boolean deleteByHumanCode(Integer humanCode) {
        return this.patientService.deleteById(humanCode);
    }

    /**
     * @apiNote 根据条件查询
     * @author tzhh 
     * @date 2021/5/27 15:29
     * @param starttime
	 * @param humancode
     * @return {@link List< Map< String, Object>>}
     **/
    @GetMapping("getProjectsByCon")
    public List<Map<String, Object>> getProjectsByCon(String starttime, int humancode){
        List<Map<String, Object>> projectsByCon = projectTest.getProjectsByCon(starttime, humancode);
//        String string = projectsByCon.toString();
//        String s = DateUtils.voidConvertToASCII(string);
//        SerialUtil serialUtil = new SerialUtil();
//        SerialUtil.closeSerialP();
//        String com2 = serialUtil.init(s, "COM3");
//        SerialUtil.closeSerialP();
        return projectsByCon;
    }

    /**
     * @apiNote 通过日期查询病员列表
     * @author tzhh
     * @date 2021/6/30 14:427
     *
     * @param starttime
     * @return {@link List< Map< String, Object>>}
     **/
    @GetMapping("getPatientListByDate")
    public List<Patient> getPatientListByDate(String starttime,String code,String name,String id){
        return patientService.getPatientListByDate(starttime,code,name,id);
    }
}
package com.laola.apa.server.impl;

import com.laola.apa.DO.SerialDO;
import com.laola.apa.common.Result;
import com.laola.apa.common.StateCodeEnum;
import com.laola.apa.controller.PatientController;
import com.laola.apa.entity.Doctor;
import com.laola.apa.entity.Patient;
import com.laola.apa.entity.Project;
import com.laola.apa.mapper.DoctorMapper;
import com.laola.apa.mapper.PatientMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.serial.SerialController;
import com.laola.apa.server.PatientService;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private DoctorMapper doctorMapper;
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);
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

        List<Patient> patients = this.patientMapper.queryAll(patient);
        String x = patient.toString();
//        SerialDO serialDo = new SerialDO();
//        serialDo.setPortName("COM16");
//        serialDo.setBaudrate(9600);
//        serialDo.setDataBits(8);
//        serialDo.setStopBits(1);
//        serialDo.setParity(0);
//        Result result1 = SerialController.openPort(serialDo);
//        Result result = new Result();
//
//        OutputStream outputStream = null;
//        try {
//            SerialPort serialPort = (SerialPort) SerialController.serialMap.get("COM16");
//            outputStream = serialPort.getOutputStream();
//            String sx = DateUtils.voidConvertToASCII(x);
//            byte[] bytes = DateUtils.hexStrToBinaryStr(sx);
////            System.out.println("SEND COUNT：" + bytes.length());//.getBytes("gbk").length);
//            System.out.println(bytes);
//            outputStream.write(bytes, 0, bytes.length);
////            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
//            outputStream.flush();
//            result.setCode(StateCodeEnum.SUCCESS.getCode());
//            result.setMsg("消息发送成功！");
//            LOGGER.info("发送消息给串口{}:{}", "COM16", x);
//        } catch (IOException e) {
//            LOGGER.error("发送消息异常！", e.getMessage());
//            result.setMsg("发送消息异常！");
//            result.setCode(StateCodeEnum.FAIL.getCode());
//            result.setData(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                assert outputStream != null;
//                outputStream.close();
//            } catch (IOException e) {
//                LOGGER.error("发送消息关闭流异常！", e.getMessage());
//            }
//        }
        return patients;
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
//        List<Patient> p = this.queryAll(patient);
//        if (0 == p.size()){
//            this.patientMapper.insert(patient);
//            return 2;
//        }
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
        String inspectionDoc = patient.getInspectionDoc();
        if (inspectionDoc != null){
            Doctor doctor = new Doctor();
            doctor.setName(inspectionDoc);
            doctorMapper.updateByName(doctor);
        }
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
        example.setOrderByClause(" id");
        List<Patient> patients = patientMapper.selectByExample(example);
        return patients;
    }
}
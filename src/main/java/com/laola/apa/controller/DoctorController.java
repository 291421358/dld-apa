package com.laola.apa.controller;

import com.laola.apa.entity.Doctor;
import com.laola.apa.server.DoctorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Doctor)表控制层
 *
 * @author tzhh
 * @since 2020-05-06 16:52:45
 */
@RestController
@RequestMapping(value = "doctor")
public class DoctorController {
    /**
     * 服务对象
     */
    @Resource
    private DoctorService doctorService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Doctor selectOne(Integer id) {
        return this.doctorService.queryById(id);
    }

    @GetMapping("queryAll")
    public List<Doctor> queryAll(Doctor doctor){
        return this.doctorService.queryAll(doctor);
    }

    /**
     * 增加数据
     *
     * @param doctor 对象
     * @return 对象
     */
    @PostMapping("insert")
    public Doctor insert(Doctor doctor) {
        return this.doctorService.insert(doctor);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 对象
     */
    @PostMapping("delete")
    public boolean delete(Integer id) {
        return this.doctorService.deleteById(id);
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     * @return 对象
     */
    @PostMapping("deleteByIds")
    public boolean deleteByIds(int[] ids) {
        return this.doctorService.deleteByIds(ids);
    }
}
package com.laola.apa.mapper;

import com.laola.apa.entity.ProjectCurve;
import com.laola.apa.utils.MyMapper;
import org.springframework.stereotype.Service;

@Service
public interface ProjectCurveMapper extends MyMapper<ProjectCurve> {

    //获取第一个
    Integer get1st(Integer pid);
    //获取加试剂2的
    Float getAddR2(Integer pid);
    //获取最后一个
    Float getlast(Integer pid);

}
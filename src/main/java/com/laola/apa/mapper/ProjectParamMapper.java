package com.laola.apa.mapper;

import com.laola.apa.entity.ProjectParam;
import com.laola.apa.utils.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectParamMapper extends MyMapper<ProjectParam> {
    List<ProjectParam> presetQc();
}
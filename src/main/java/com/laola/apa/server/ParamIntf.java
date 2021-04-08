package com.laola.apa.server;

import com.laola.apa.entity.ProjectParam;
import com.laola.apa.vo.ProjectListVO;

import java.util.List;
import java.util.Map;

public interface ParamIntf {
    List<ProjectParam> presetQc();
    //查询列表
    Map<String, String> projectMap();

    List<ProjectListVO> projectList();

    //根据id查单条数据
    ProjectParam onePoject(int id);
    //修改
    boolean update(ProjectParam projectParam);
    //生成二维码
    String createQRCode(int id, int total);

    void insert(ProjectParam projectParam);
}

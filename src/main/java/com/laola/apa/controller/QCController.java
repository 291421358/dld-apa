package com.laola.apa.controller;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.entity.QC;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.server.QCserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (QC)表控制层
 *
 * @author tzhh
 * @since 2020-06-16 13:41:51
 */
@RestController
@RequestMapping("qC")
public class QCController {
    /**
     * 服务对象
     */
    @Resource
    private QCserver qCService;
    @Autowired
    private ProjectTest projectTest;
    @Autowired
    private ProjectParamMapper projectParamMapper;
    /**
     * 通过主键查询单条数据
     * @author tzhh
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public QC selectOne(Integer id) {
        return this.qCService.queryById(id);
    }
    
    /**
     * 修改数据
     * @author tzhh
     * @param
     * @return 对象
     */
    @GetMapping("update")
    public int update(int id,String staQuality,String type) {
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(id);
        if (type.equals("3")){
            projectParam.setPresetDensityHight(staQuality);
        }
        if (type.equals("4")){
            projectParam.setPreset_density_mid(staQuality);
        }
        if (type.equals("5")){
            projectParam.setPreset_density_low(staQuality);
        }
        return  projectParamMapper.updateByPrimaryKeySelective(projectParam);
    }

    /**
     * 根据条件查询质控项目
     * @author tzhh
     * @param projectParamId
     * @return
     */
    @GetMapping("getQcProjects")
    public List<Map<String, Object>> getQcProjects(int projectParamId,String beginDate,String endDate,String type){
        List<Map<String, Object>> projects = projectTest.getQcProjects(projectParamId,beginDate,endDate,type);
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(projectParamId);
        projectParam = projectParamMapper.selectOne(projectParam);

        QC qc = qCService.queryById(projectParamId);
        if (qc == null){
            qc = new QC();
        }
        String staQuality = qc.getStaQuality();
        if (staQuality == null || staQuality.equals("")){
          staQuality = "0";
        }
        float sta = 0.0f;
        if (type.equals("3")){
            sta =  Float.parseFloat(projectParam.getPresetDensityHight());
        }
        if (type.equals("4")){
            sta =  Float.parseFloat(projectParam.getPresetDensityMid());
        }
        if (type.equals("5")){
            sta =  Float.parseFloat(projectParam.getPresetDensityLow());
        }
        float MaxGap = 0;
        for (Map<String, Object> project:projects) {
            //求得所有density与标准值*（sta）的最大数差
            if (null == project.get("density") || project.get("density").equals("")){
                project.put("density","0");
            }
            float density = Float.parseFloat(String.valueOf(project.get("density")));
            float gap = density - sta;
            if (gap <0){
                gap = -gap;
            }
            if (gap >MaxGap){
                MaxGap = gap;
            }
        }
        float proportion = (sta*0.3F)/56;
        for (Map<String, Object> project:projects) {
            project.put("proportionateDensity",(Float.parseFloat(String.valueOf(project.get("density")))-sta)/proportion);

        }
        Map<String, Object> staMap = new HashMap<>();
        staMap.put("sta",sta);
        projects.add(staMap);
        return projects;
    }
    /**
     * @apiNote 获取正在做的定标结果
     * @author tzhh
     * @date 2021/5/27 15:24
     * @param
     * @return {@link com.laola.apa.entity.Project}
     **/
    @GetMapping("getQcProjectsResult")
    public Project getQcProjectsResult(){
        return qCService.queryLast();
    }

}
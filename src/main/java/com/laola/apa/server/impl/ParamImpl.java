package com.laola.apa.server.impl;

import com.laola.apa.costant.AlgorithmConstant;
import com.laola.apa.entity.Code;
import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.entity.Scaling;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.mapper.ScalingMapper;
import com.laola.apa.server.CodeServer;
import com.laola.apa.server.ParamIntf;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.QRCodeUtils;
import com.laola.apa.vo.ProjectListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ParamImpl implements ParamIntf {

    @Autowired
    private ProjectParamMapper projectParamMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ScalingMapper scalingMapper;
    @Autowired
    private CodeServer code;
    /**
     * 查询质控标准值
     */
    public List<ProjectParam> presetQc(){
        List<ProjectParam> projectParams = projectParamMapper.presetQc();
        return projectParams;
    }
    /**
     * 查询所有项目的id和项目名 Map
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> projectMap() {
        List<ProjectParam> projectParamList = projectParamMapper.selectAll();
        Map<String,String> projectMap = new HashMap();
        for (ProjectParam projectParam : projectParamList) {
            projectMap.put(String.valueOf(projectParam.getId()),projectParam.getName());
        }

        return projectMap;
    }
    /**
     * 查询所有项目的id和项目名 List
     * @return List<ProjectListVO>
     */
    @Override
    public List<ProjectListVO> projectList() {
        List<ProjectParam> projectParamList = projectParamMapper.selectAll();
        List<ProjectListVO> projectList = new ArrayList<>();
        for (ProjectParam projectParam : projectParamList) {
            ProjectListVO projectListVO = new ProjectListVO();
            projectListVO.setId(projectParam.getId());
            projectListVO.setName(projectParam.getName());
            projectList.add(projectListVO);
        }
        return projectList;
    }


    /**
     * 根据项目id查询项目详情
     * @param id
     * @return ProjectParam
     */
    @Override
    public ProjectParam onePoject(int id) {
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(id);
        projectParam = projectParamMapper.selectOne(projectParam);
        //浮点数退位
        carryDigit(projectParam,"P");
        return projectParam;
    }

    /**
     * 修改项目参数
     * @param projectParam
     * @return
     */
    @Override
    public boolean update(ProjectParam projectParam) {
        ProjectParam projectParam1 = projectParamMapper.selectByPrimaryKey(projectParam);
        if (!Objects.equals(projectParam.getName(), projectParam1.getName())){
            projectMapper.deleteProjectByParamId(projectParam.getId());
        }

        carryDigit(projectParam,"M");
        projectParamMapper.updateByPrimaryKeySelective(projectParam);

        return true;
    }

    /**
     * 生成二维码
     * @param id
     * @param total
     * @return
     *
     */
    @Override
    public String createQRCode(int id, int total) {
        ProjectParam p = new ProjectParam();

        p.setId(id);
        p = projectParamMapper.selectOne(p);
        String param = p.toLitString();
        param = param.replace("null", "");
        carryDigit(p,"P");


        Code code = this.code.queryNextCode();
        StringBuilder text =  new StringBuilder(code.getCode());
        text = text.append(",").append(total).append(",");

        text = text.append(param).append(",");
        text = new StringBuilder(DateUtils.unicodeEncodeOnlyCN(text.toString()));

        Scaling scaling = scalingMapper.queryById(p.getFactor());
        String algorithm = scaling.getAlgorithm();
        //根据算法全称获取 算法代码
        algorithm = AlgorithmConstant.algorithm.get(algorithm);
        text.append(algorithm).append(",");

        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectParamId", id);
        criteria.andEqualTo("type", 2);
        String preDate = DataUtil.getPreDateByUnit(p.getFactor(), 1, 12);
//                    logger.info("factor:"+factor);
        criteria.andBetween("starttime", p.getFactor(), preDate);

        List<Project> projects = projectMapper.selectByExample(example);


        for (Project project : projects) {
            text.append(project.getDensity()).append(",").append(project.getAbsorbance()).append(",");
        }


        text.deleteCharAt(text.lastIndexOf(","));

//        text.append("aaaaaaaaaa");
        String logoPath = "C:\\img\\test.jpg";
        String destPath = "C:\\img\\";
        String encode = null;
        try {
            encode = QRCodeUtils.encode(text.toString(), logoPath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(encode);

        Process process = null;//Process代表一个进程对象
        //打开二维码
        QRCodeUtils.openQRCode(encode, process);
        return null;
    }

    @Override
    public void insert(ProjectParam projectParam) {
        projectParam.setId(null);
        projectParamMapper.insert(projectParam);
    }

    private ProjectParam carryDigit(ProjectParam projectParam, String PorM){
        String minAbsorbance = projectParam.getMinAbsorbance();
        String maxAbsorbance = projectParam.getMaxAbsorbance();
        String normalLow = projectParam.getNormalLow();
        String normalHigh = projectParam.getNormalHigh();
        Float abs = 0F;
        Float nor = 0F;
        if (PorM.equals("P")){
            abs = 10F;
            nor= 100F;
        }
        if (PorM.equals("M")){
            abs = 0.1F;
            nor= 0.01F;
        }
        projectParam.setMinAbsorbance(DateUtils.carryDigit(minAbsorbance,abs));
        projectParam.setMaxAbsorbance(DateUtils.carryDigit(maxAbsorbance,abs));
        projectParam.setNormalLow(DateUtils.carryDigit(normalLow,nor));
        projectParam.setNormalHigh(DateUtils.carryDigit(normalHigh,nor));
        return projectParam;
    }
}

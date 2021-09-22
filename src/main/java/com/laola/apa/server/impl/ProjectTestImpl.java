package com.laola.apa.server.impl;

import com.laola.apa.entity.*;
import com.laola.apa.mapper.*;
import com.laola.apa.server.PatientService;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;
import com.laola.apa.utils.SpringBeanUtil;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProjectTestImpl implements ProjectTest {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectCurveMapper projectCurveMapper;
    @Autowired
    private SelectDao selectDao;
    @Autowired
    private ProjectParamMapper paramMapper;
    @Resource
    private PatientMapper patientMapper;
    @Resource
    private PatientService patientService;
    @Resource
    private EquipmentStateMapper equipmentState;

    private Logger logger = LoggerFactory.getLogger(ProjectTest.class);

    /**
     * 根据id查曲线
     *
     * @param id
     * @return
     */
    @Override
    public List<ProjectCurve> selectCurveById(Integer id) {
        ProjectCurve projectCurve = new ProjectCurve();
        projectCurve.setProjectId(id);
        return projectCurveMapper.select(projectCurve);
    }


    /**
     * * 保存项目
     *
     * @param project
     * @return
     */
    @Override
    public int insertProject(Project project) {
        return projectMapper.insert(project);
    }

    /**
     * * 批量保存项目
     *
     * @param projectList
     * @return
     */
    @Override
    public int insertProjectList(List<Map<String, Object>> projectList) {
        Boolean a = false;
        Map<String,String> pMap = new HashMap<>();
        Map<String,String> codeMap = new HashMap<>();
        for (Map<String, Object> map : projectList) {
//            Patient patient = new Patient(Integer.parseInt(String.valueOf(map.get("humanCode"))), String.valueOf(map.get("")));
//            patientService.update(patient);
            if (pMap.get(String.valueOf(map.get("humanCode"))) == null){
                pMap.put(String.valueOf(map.get("humanCode")),String.valueOf(map.get("humanCode")));
                codeMap.put(String.valueOf(map.get("humanCode")), String.valueOf(map.get("barCode")));
            }
            if ( String.valueOf(map.get("a")).equals("true")){
                map.put("rackId","-2");
                map.put("placeId","-2");
                a = true;
            };
            System.out.println( String.valueOf(map.get("a")));
        }
        List<Patient> pList= new ArrayList<>();
        for (String value : pMap.values()) {
            if (value != null && !value.equals("null")){
                Patient patient = new Patient(Integer.parseInt(value), codeMap.get(value));
                pList.add(patient);
            }
        }
        if (pList.size()>0)
        patientMapper.insertPatientList(pList);
        int i = projectMapper.insertProjectList(projectList);
        // 清空 新增的项目的对应的曲线和病员
        projectMapper.clean(i);
        if (a){
            PortDataDealService<String,Object> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p9c");
            SerialPort serialPort = new SerialUtil().startComPort();
            beanByName.deal("EB9c-2-2-2c90d",serialPort);
        }
        return i;
    }

    /**
     * 查询有效的项目
     *
     * @return 。
     * v
     */
    @Override
    public List<Map<String, Object>> selectAble() {
        return selectDao.selectList("select p.id,pc.project_id,pc.x,pc.y,p.project_num num from project_curve pc" +
                "               left join project p on pc.project_id = p.id" +
                "                 where TIMESTAMPDIFF(MINUTE,starttime,NOW())<30" +
                "  AND ISNULL(endtime);");
    }

    /**
     * 查询有效的项目           （working）
     * @return
     */
    @Override
    public List<Map<String, Object>> selectAbleProject() {
        return selectDao.selectList("select p.id id,p.project_num,main_indication_end length,place_no placeNo" +
                ",rack_no rackNo,type,density,project_param_id ppi,human_code,p.cup_number from project p" +
                "                       LEFT JOIN project_param pp on pp.id = p.project_param_id " +
                "    where \tISNULL(endtime) " +
                "order by p.id;");
    }


    /**
     * 查询有效未做的项目           （working）
     * @return
     * @param i
     */
    @Override
    public List<Map<String, Object>> selectNeverDo(int i) {
        return selectDao.selectList("SELECT " +
                " p.id id, " +
                " p.project_num, " +
                " main_indication_end length, " +
                " place_no placeNo, " +
                " rack_no rackNo, " +
                " type, " +
                " density, " +
                " project_param_id ppi, " +
                " human_code  ," +
                "bar_code " +
                "FROM " +
                " project p " +
                " LEFT JOIN project_param pp ON pp.id = p.project_param_id  " +
                " LEFT JOIN project_curve pc on pc.project_id = p.id " +
                "WHERE " +
                "  " +
                " ISNULL( endtime ) " +
                " and ISNULL(pc.id) " +
                " and ISNULL(p.factor)" +
                " order by p.a desc,p.id limit "+i+";");
    }

    /**
     * 查询下一个项目号  1-80
     *
     * @return
     */
    @Override
    public Integer selectNextProjectNum() {
        int project_num;
        List<Map<String, Object>> maps = selectDao.selectList("SELECT project_num FROM project p WHERE " +
                " ISNULL(endtime) ORDER BY p.id desc LIMIT 1");
        if (maps.size() == 0) {
            project_num = 1;
        } else {
            project_num = Integer.parseInt(String.valueOf(maps.get(0).get("project_num")));
            if (project_num == 80) {
                project_num = 1;
            } else {
                project_num++;
            }
        }
        return project_num;
    }

    /**
     * 查询下一个病人code
     *
     * @return
     */
    @Override
    public Integer selectNextHumanCode() {
        List<Map<String, Object>> maps = selectDao.selectList("SELECT human_code FROM project ORDER BY human_code DESC LIMIT 1");
        if (maps.size() == 0 || null == maps.get(0) ||null == maps.get(0).get("human_code")){
            return 1;
        }
        int human_code = Integer.parseInt(String.valueOf(maps.get(0).get("human_code")));
        human_code++;
        return human_code;
    }


    /**
     * @param id
     * @return 取得16个字节 List
     */
    @Override
    public String getIdList(int id) {
        System.out.println(id);
        List<Map<String, Object>> maps;
            maps = selectDao.selectList(
                    " SELECT pp.diluent_place ,pp.diluent_size ,pp.dilution_sample_size ,p.project_param_id, rp.place place" +
                            " , pp.samplesize samplesize,pp.reagent_quantity_no1 reagentQuantityNo1" +
                            " ,pp.reagent_quantity_no2 reagentQuantityNo2,p.project_num projectNum" +
                            " ,pp.main_wavelength mainWavelength,pp.main_indication_begin r2,pp.main_indication_end length,p.a " +
                            " ,pp.`dilution_delay_ period` wait,pp.auxiliary_indication_end xLength" +
                            " FROM project p \n" +
                            " LEFT JOIN project_param pp on pp.id = p.project_param_id\n" +
                            " LEFT JOIN regent_place rp on rp.project_param_id = p.project_param_id and rp.a = 0\n" +
                            " WHERE p.id = " + id +
                            "\n");
        System.out.println(maps);
        List<String> commndList = new ArrayList<>();
        try{
            getCommondList(maps, commndList);
        }catch (Exception e){
            logger.info("Exception generating project instruction ：id"+id+"content:"+maps);
            e.printStackTrace();
        }
        if (commndList.size() >= 1){
            return commndList.get(0);
        }else {
            System.out.println("项目不存在");
            return null;
        }
    }
    /**
     * 确定是否有项目在做(在发送项目开始指令时)
     */
    @Override
    public int isProjectDoing() {
        List<Map<String, Object>> mapList = selectDao.selectList(
                "SELECT\n" +
                "\tCOUNT(DISTINCT(project_id)) count\n" +
                "FROM\n" +
                "\tproject_curve pc\n" +
                "\tLEFT JOIN project p ON pc.project_id = p.id \n" +
                "WHERE\n" +
                "\t(" +
                "      DATE_FORMAT(p.starttime,\"%y-%m-%d\") = DATE_FORMAT(NOW(),\"%y-%m-%d\")\n" +
//                "      or" +
//                "      DATE_FORMAT(p.starttime,\"%y-%m-%d\") = DATE_SUB(curdate(),INTERVAL 1 DAY)\n" +
                "      ) \n" +
                "\tAND ISNULL( endtime );");
        return Integer.parseInt(String.valueOf(mapList.get(0).get("count")));
    }

    /**
     * 获取命令列表
     * @param maps
     * @param commndList
     */
    private void getCommondList(List<Map<String, Object>> maps, List<String> commndList) throws Exception {
        for (Map<String, Object> map : maps) {
//            String place2 = String.valueOf(map.get("place2"));
            String place = String.valueOf(map.get("place"));
            String[] splitSamplesize = String.valueOf(map.get("samplesize")).split("\\.");
//            System.out.println(splitSamplesize[0]+""+map.get("samplesize"));
            String samplesize0 = splitSamplesize[0];
            String samplesize1 = "0" + splitSamplesize[1].substring(0, 1);
            StringBuilder reagentQuantityNo1 = new StringBuilder(String.valueOf(map.get("reagentQuantityNo1")));
            String reagentQuantityNo2 = String.valueOf(map.get("reagentQuantityNo2"));
            String projectNum = String.valueOf(map.get("projectNum"));
            String mainWavelength = String.valueOf(map.get("mainWavelength"));
            String r2 = String.valueOf(map.get("r2"));
            //稀释延迟
            String wait = String.valueOf(map.get("wait"));
            //稀释长度
            String xLength = String.valueOf(map.get("xLength"));

            String length = String.valueOf(map.get("length"));
            String diluent_place = String.valueOf(map.get("diluent_place"));
            String diluent_size = String.valueOf(map.get("diluent_size"));
            String dilution_sample_size = String.valueOf(map.get("dilution_sample_size"));
            String a = String.valueOf(map.get("a"));
            //转换成16进制
            String place1 = DateUtils.DEC2HEX(String.valueOf(2 * Integer.parseInt(place) -1));
            String place2 = DateUtils.DEC2HEX(String.valueOf(2 * Integer.parseInt(place) -1+1));
//            place2 = DateUtils.DEC2HEX(place2);
            samplesize0 = DateUtils.DEC2HEX(samplesize0);
            reagentQuantityNo1 = new StringBuilder(DateUtils.DEC2HEX(reagentQuantityNo1.toString()));
            reagentQuantityNo2 = DateUtils.DEC2HEX(reagentQuantityNo2);
            projectNum = DateUtils.DEC2HEX(projectNum);
            mainWavelength = DateUtils.DEC2HEX(mainWavelength);
            r2 = DateUtils.DEC2HEX(r2);
            wait = DateUtils.DEC2HEX(wait);
            xLength = DateUtils.DEC2HEX(xLength.substring(0,xLength.length()-1));

            //时间除10，取退一位
            length = DateUtils.DEC2HEX(length.substring(0,length.length()-1));
            diluent_place = DateUtils.DEC2HEX(diluent_place);
            diluent_size = DateUtils.DEC2HEX(diluent_size);
            dilution_sample_size = DateUtils.DEC2HEX(dilution_sample_size);
            String Dilution_number = DateUtils.DEC2HEX(String.valueOf(Integer.parseInt(projectNum,16)+160));
            if (dilution_sample_size.equals("00")){
                Dilution_number = "00";
            }
            a = a.equals("1")?"01":"00";
            //生成指令
            for (int i = 0; i <= 4 - reagentQuantityNo1.length(); i++) {
                reagentQuantityNo1.insert(0, "0");
            }
            String i = String.valueOf(Integer.parseInt(place, 16) + 1);
            if (i.length() <2){
                i = "0"+i;
            }
            String mark = "00";
//            if (null != dilution_sample_size &&Integer.parseInt(dilution_sample_size,16) > 0){
//                mark = "01";
//            }
 //样品位     //     [2]试剂1位 	                               试剂2位 	                  样品量    	          试剂1量H
    String commnd = place1 + " " +                             place2 + " " +             samplesize0 + " " + reagentQuantityNo1.substring(0, 2) + " " +
             //     [6]试剂1量L                                 试剂2量                     项目序号
                    reagentQuantityNo1.substring(2, 4) + " " + reagentQuantityNo2 + " " + projectNum + " " +
             //     [9]样品量                                   波长                        总时长
                    samplesize1 + " " +                        mainWavelength + " " +     length + " "+

             //     [12]r2加入时间                              稀释延迟取样时间     空        稀释序号
                    r2 +        " "+                           wait+" "+         "00 " +   Dilution_number   + " " +

             //    [16]稀释样本量                               稀释液量                      稀释液位置
                   dilution_sample_size  +" "+                 diluent_size + " " +        diluent_place + " "+

             //    [19]红细胞压积波长              稀释运行时间             急诊                 稀释标记         取样项目序号
                        "00"+    " "+            xLength+ " "+           a + " "+             mark+  " "+      "00";
                commndList.add(commnd);
//            System.out.println(commnd+"projectTest 256");
        }
    }

    /**
     * 根据时间获得当天的项目
     * @param project
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getProjectListByData(Project project){
        String sql = "SELECT project.*,CONCAT(CONVERT(project.b/pp.`main_indication_end`*100,decimal(2,0)),\"%\")progress" +
                ",p.code  FROM project \n" +
                "LEFT JOIN project_curve  pc ON pc.project_id = project.id \n" +
                "LEFT JOIN project_param pp on pp.id = project.project_param_id \n" +
                "LEFT JOIN patient p on p.id = project.human_code " +
                "and DATE_FORMAT(p.inspection_date,\"%y-%M-%d\")=DATE_FORMAT(project.starttime,\"%y-%M-%d\") \n" +
                "WHERE   type=1 AND starttime BETWEEN \""+project.getEndtime()+" 00:00:00\" and \"" +
                DataUtil.getPreDateByDate(project.getEndtime(),1)+ " 00:00:00\" \n " +
                "GROUP BY project.id order by human_code desc\n" +
                ";";
        List<Map<String, Object>> mapList = selectDao.selectList(sql);
        //取出项目参数名字和id键值对
        List<ProjectParam> projectParams = paramMapper.selectAll();
        String params[] = new String[1280];
        //取出项目参数名字和id键值对
        for (ProjectParam param : projectParams) {
            params[param.getId()] = param.getName();
        }
        //取出

        Map<String,Object> pjtMap = new HashMap<>();
        //放入最大code和最小code
        pjtMap.put("maxCode", mapList.size()!=0?mapList.get(0).get("human_code"):0);
        pjtMap.put("minCode", mapList.size()!=0?mapList.get(0).get("human_code"):0);
        dealMapList(mapList, params, pjtMap);
        return pjtMap;
    }


    /**
     * 根据时间获得当天的项目 后十个
     * @param project
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getProjectListByDataTenToEnd(Project project){
        String sql = "SELECT project.*,CONCAT(CONVERT(count(pc.id)/pp.`main_indication_end`*100,decimal(2,0)),\"%\")progress " +
                ",p.code  FROM project \n" +
                "LEFT JOIN project_curve  pc ON pc.project_id = project.id \n" +
                "LEFT JOIN project_param pp on pp.id = project.project_param_id \n" +
                "LEFT JOIN patient p on p.id = project.human_code " +
                "and DATE_FORMAT(p.inspection_date,\"%y-%M-%d\")=DATE_FORMAT(project.starttime,\"%y-%M-%d\") \n" +
                "WHERE   type=1 AND starttime BETWEEN \""+project.getEndtime()+" 00:00:00\" " +
                "and \"" +DataUtil.getPreDateByDate(project.getEndtime(),1)+ " 00:00:00\" \n " +
                "GROUP BY project.id order by human_code desc  limit 15\n" +
                ";";
        List<Map<String, Object>> mapList = selectDao.selectList(sql);
        //取出项目参数名字和id键值对
        List<ProjectParam> projectParams = paramMapper.selectAll();
        String params[] = new String[1280];
        //取出项目参数名字和id键值对
        for (ProjectParam param : projectParams) {
            params[param.getId()] = param.getName();
        }
        //取出

        Map<String,Object> pjtMap = new HashMap<>();
        //放入最大code和最小code
        pjtMap.put("maxCode", mapList.size()!=0?mapList.get(0).get("human_code"):0);
        pjtMap.put("minCode", mapList.size()!=0?mapList.get(0).get("human_code"):0);
        dealMapList(mapList, params, pjtMap);
        return pjtMap;
    }
//
//eb91440000140000000000000000000001000000040000f8d300000f07000708
//    01012a0f640f64
//    030320397b397b
//    05061610f010f0
//    0608116e141d95
//    07010c0f640f64080207722b1f17090402802d802d

    /**
     */
    private void dealMapList(List<Map<String, Object>> mapList, String[] params, Map<String, Object> pjtMap) {
        for (Map<String, Object> project0 : mapList) {
            String humanCode = String.valueOf(project0.get("human_code"));
            pjtMap.computeIfAbsent(humanCode, k -> new ArrayList<>());
            if (Integer.parseInt(String.valueOf(pjtMap.get("maxCode"))) < Integer.parseInt(humanCode)){
                pjtMap.put("maxCode", Integer.parseInt(humanCode));
            }
            else if (Integer.parseInt(String.valueOf(pjtMap.get("minCode"))) > Integer.parseInt(humanCode)){
                pjtMap.put("minCode", Integer.parseInt(humanCode));
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("name",params[(int) project0.get("project_param_id")]);

            map.put("progress", String.valueOf(project0.get("progress")));
            if (null != project0.get("density") && !"".equals(String.valueOf(project0.get("density")))){
                map.put("progress", String.valueOf(project0.get("density")));
            }
            map.put("rack",String.valueOf(project0.get("rack_no")));
            map.put("place",String.valueOf(project0.get("place_no")));
            map.put("barCode",String.valueOf(project0.get("code")));
            map.put("projectParamId",String.valueOf(project0.get("project_param_id")));
            map.put("abnormal",String.valueOf(project0.get("abnormal")));
            map.put("absorbanceLow",String.valueOf(project0.get("absorbance_low")));
            map.put("absorbanceHeight",String.valueOf(project0.get("absorbance_height")));
            map.put("id",String.valueOf(project0.get("id")));
            map.put("a",String.valueOf(project0.get("a")));
            ((List)pjtMap.get(humanCode)).add(map);

        }
    }

    /**
     * 根据type，starttime，humancode 获得项目
     */
    @Override
    public List<Map<String, Object>> getProjectsByCon(String starttime, int humancode){
        String preDate = DataUtil.getPreDateByDate(starttime, 1);
        String thisDate = DataUtil.getPreDateByDate(starttime, 0);
        List<Map<String, Object>> mapList = selectDao.selectList(" SELECT p.id,p.bar_code,pp.chinese_name,pp.`name`" +
                ",p.density,pp.meterage_unit,pp.normal_high,pp.normal_low FROM project p \n" +
                " LEFT JOIN project_param pp on p.project_param_id = pp.id\n" +
                " WHERE type = 1 and human_code = " + humancode + " AND p.starttime BETWEEN '" + thisDate + "' and '" + preDate+"' ");
        return mapList;
    }

    /**
     * 根据项目参数id获取qc曲线
     *
     */
    @Override
    public List<Map<String, Object>> getQcProjects(int projectParamId, String beginDate, String endDate, String type){
        String preDateByDate = DataUtil.getPreDateByDate(endDate, 1);
        List<Map<String, Object>> mapList = selectDao.selectList(" SELECT DATE_FORMAT(p.starttime,\"%y-%m-%d\") " +
                "date,p.id,p.density FROM project p \n" +
                "LEFT JOIN project_param pp on p.project_param_id = pp.id\n" +
                    "WHERE ( starttime between '"+beginDate+"' and '"+preDateByDate+"') AND type = "+
                type+" AND project_param_id = "+projectParamId+"   ORDER BY id DESC ");
        List<Map<String, Object>> resultMap = new ArrayList<>();
        String date = "";
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            String indate = String.valueOf(map.get("date"));
            if (!indate.equals(date)    ){
                date = indate;
                resultMap.add(map);
            }
        }
        return resultMap;
    }

    /**
     * 删除所有项目
     */
    @Override
    public void deleteProjects() {
        EquipmentState t = new EquipmentState(1, 11, null, null
                , null, null, 0, 0, 0, null,0);
        equipmentState.updateByPrimaryKeySelective(t);
        patientMapper.deleteProjects();
        projectMapper.deleteProjects();
    }
    /**
     * 通过dateId paramId删除数据
     *
     */
    @Override
    public void deleteProjects(String dateId, Integer paramId) {
        projectMapper.deleteProjectsByDateIdParamId(dateId,paramId);
    }

    /**
     * 通过id删除项目
     * @param id
     */
    @Override
    public void deleteProjectById(int id) {
        projectMapper.deleteByPrimaryKey(id);
    }

    /**
     * @apiNote 查询每天每个项目最后一个质控
     * @author tzhh
     * @date 2021/6/2 11:05
     * @param
     * @return
     **/
    @Override
    public List<ProjectQC> getQcLastOneByDataAndType() {
        return projectMapper.getQcLastOneByDataAndType();
    }

    @Override
    public void addby37() {
        projectMapper.addby37();
    }

    @Override
    public int l(String density) {
        String[] split = density.split(",");
        if (split.length == 2){
            projectMapper.l(split[0], Integer.valueOf(split[1]));
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int getLastSca() {
        return projectMapper.getLastSca();
    }


}

//
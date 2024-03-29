package com.laola.apa.server.impl.PortDataDeal;

import com.google.gson.Gson;
import com.laola.apa.entity.*;
import com.laola.apa.mapper.ProjectCurveMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.mapper.ProjectNodeMapper;
import com.laola.apa.mapper.ScalingMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("p91")
public class P91 implements PortDataDealService<String, Object> {

    @Autowired
    private ProjectTest projectTest;
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    private ParamIntf projectParameters;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectCurveMapper projectCurveMapper;
    @Autowired
    private ScalingIntf scalingIntf;
    @Autowired
    ScalingMapper scalingMapper;
    @Autowired
    ProjectNodeMapper projectNodeMapper;
    @Autowired
    WebSocket webSocket;
    private static final Logger logger = LoggerFactory.getLogger(P91.class);

    /**
     * 处理91开头的结果。  在做项目时，出一个结果时
     *
     * @param strings
     * @throws Exception
     */
    @Override
    public String deal(Object... strings) {
        String string = String.valueOf(strings[0]);
        logger.info("GET RESULT DATA" + string);
        //A15：撞针标志   1/0代表撞针
        int firingPin = DateUtils.decodeHEX(string.substring(36, 38));
        //行为
        int behavior = DateUtils.decodeHEX(string.substring(24, 26));
        /*
         当前时间
         */
        int time = DateUtils.decodeHEX(string.substring(26, 30));//1111111
        //判断是结果
        //截取前32 * 2位
        String result = string.substring(64);
        //仪器状态设置
        PortDataDealService<String, String> beanByName = SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class, "p86");
        beanByName.deal(string);

        //查询没结束的项目
        List<Map<String, Object>> ableList = projectTest.selectAbleProject();

        //遍历32位后的七*2 位；每七*2 位遍历一次
        for (int i = 0; i < result.length() / 14; i++) {
            //x项目id
            int id = 0;
            int length = 0;
            int type = 0;
            float density = 0;
            int oldDensity = 0;
            int ppi = 0;
            int rackNo = 0;
            int placeNo = 0;
            boolean should = true;
            String projectNum = String.valueOf(DateUtils.decodeHEX(result.substring(i * 14, i * 14 + 2)));
            String num = String.valueOf(DateUtils.decodeHEX(string.substring(58, 60)));
            for (Map<String, Object> ablemap : ableList) {
                String projectNum1 = String.valueOf(ablemap.get("project_num"));
                if (projectNum1.equals(projectNum)) {
                    logger.info(projectNum1 + " project_num & project_num " + projectNum);
                    //串口传上来的前两位项目号和  数据库中的项目号相同
//                    logger.info(projectNum);
                    id = Integer.parseInt(String.valueOf(ablemap.get("id")));
                    length = Integer.parseInt(String.valueOf(ablemap.get("length")));
                    type = ablemap.get("type") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("type")));
                    density = ablemap.get("density") == null ? 1 : Float.parseFloat(String.valueOf(ablemap.get("density")));
                    oldDensity = ablemap.get("cup_number") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("cup_number")));
                    ppi = ablemap.get("ppi") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("ppi")));
                    rackNo = Integer.parseInt(String.valueOf(ablemap.get("rackNo")));
                    placeNo = Integer.parseInt(String.valueOf(ablemap.get("placeNo")));
                    //如果该项目存在。不需要再插入项目
                    should = false;
                    break;
                }

            }
            //插入项目节点
            if (behavior <= 5 && projectNum.equals(num)){
                ProjectNode projectNode = new ProjectNode(id,behavior,time);
                projectNode.setCt(new Date());
                projectNodeMapper.insert(projectNode);
            }
            //如果该项目存在。不需要再插入项目
            if (should) {
                System.err.println("unknown project when the result come out");
                continue;
            }
            int x = DateUtils.decodeHEX(result.substring(i * 14 + 4, i * 14 + 6));
            if (x == 1) {
                //试剂减一
                logger.info("MINUS ONE REAGENT WHEN GET THE FIRST DATA");
                int i1 = usedCodeServer.minusOneCopyReagent(Integer.parseInt(String.valueOf(id)));
            }

            ProjectCurve projectCurve = new ProjectCurve();
            //设置曲线参数
            projectCurve.setProjectId(id);
            //数据号
            projectCurve.setX(x);
            //设置曲线参数
            projectCurve.setT(time);
            //eb9125
            //辅波高位，辅波低位 //取一组数据，第10-11位 *256 + 12-13位 为ad数值
            int auxiliary = DateUtils.decodeHEX(result.substring(i * 14 + 10, i * 14 + 12)) * 256 + DateUtils.decodeHEX(result.substring(i * 14 + 12, i * 14 + 14));
            //主波高位，主波低位 //取一组数据，第10-11位 *256 + 12-13位 为ad数值
            int major = DateUtils.decodeHEX(result.substring(i * 14 + 6, i * 14 + 8)) * 256 + DateUtils.decodeHEX(result.substring(i * 14 + 8, i * 14 + 10));
            //40000/ad，取对数获得吸光度

//            setBeindState(string);
            ProjectParam projectParam = projectParameters.onePoject(ppi);
            String name = projectParam.getName();
            String ReadBegin = projectParam.getDilutionMultiple();
            //R2孵育时间
            String R2Incubation = projectParam.getAuxiliaryIndicationBegin();
            String mainWavelength = projectParam.getMainWavelength();
            if (auxiliary == 0) {
                auxiliary = 1;
            }
            if (major == 0) {
                major = 1;
            }
            double log = 0;
            //波长不为750时使用透射算法
            if (!"8".equals(mainWavelength)) {
                log = Math.log10(40000F / major);
            } else {
                //波长为750时使用散射算法
                log = major / 20000F;
            }
            //格式化吸光度 小数点后三位
            String formatAbs = new DecimalFormat("0.0000").format(log * 2);
            //取得项目参数值

            //最大吸光度
            String maxAbsorbance = projectParam.getMaxAbsorbance();
            //最小吸光度
            String minAbsorbance = projectParam.getMinAbsorbance();
            Project projectSetAbnormal = new Project();
            projectSetAbnormal.setId(id);
            if (Float.parseFloat(formatAbs) > Float.parseFloat(maxAbsorbance)) {
                projectSetAbnormal.setAbsorbanceHeight(1);
            }
            if (Float.parseFloat(formatAbs) < Float.parseFloat(minAbsorbance)) {
                projectSetAbnormal.setAbsorbanceLow(1);
            }
            if (firingPin == 1){
                projectSetAbnormal.setAbnormal(9);
            }
            projectMapper.updateByPrimaryKeySelective(projectSetAbnormal);
            projectSetAbnormal = null;

            //吸光度*1000
            projectCurve.setY(String.valueOf(new DecimalFormat("0.0").format(Float.parseFloat(formatAbs) * 1000)));
            projectCurve.setCreattime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));

            //qu取得第一个点时间
            Integer st = projectCurveMapper.get1st(id);
            logger.info("当前时间"+time);
            logger.info("开始时间"+st);
            //数据长度是否等读点数终点 代表最后和一个点读数结束
            if (st == null && x!=1){
                System.err.println("没有第一个点");
                continue;
            }
            //插入曲线上的一个点
            int insert = projectCurveMapper.insert(projectCurve);
            //取得r2 加入时间
            ProjectNode projectNode = projectNodeMapper.queryByPId(id);
            int R2t =  0;
            if (projectNode!= null && projectNode.getT()!=null){
                R2t = projectNode.getT();
            }
            //当前时间- 开始时间 = 实际总反应时间--> 当前时间-r2加入时间=r2 已经反应时间
            int usedT = time - st;
            //int usedR2T = time - st;
            //孵育时长
            String a = projectParam.getA();
            if (R2t >0 ){
                int length1 = Integer.parseInt(a) + R2t + Integer.parseInt(R2Incubation) - st;
                length = length1>length?length1:length;
            }
            logger.info("总时长"+length);
            if (usedT >= length) {
                SerialUtil cRead = new SerialUtil();
                String init = cRead.init("E5 90 C3 "+DateUtils.DEC2HEX(projectNum)+" 00 00 00 00 00 00 00 00 00 00 00 00");
                System.out.println("E5 90 C3 "+DateUtils.DEC2HEX(projectNum)+" 00 00 00 00 00 00 00 00 00 00 00 00");
//                PortDataDealService<String, Object> newName = SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class, "p9c");
//                newName.deal("eb9c0"+rackNo+"0"+placeNo+"c90d",strings[1],"1");
                Project project = new Project();
                //设置结束时间为当前时间
                project.setEndtime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
                //new Date().getTime() + 6000000
                project.setId(id);
                //取得定标因子
                String factor = projectParam.getFactor();

                if (null == factor || "".equals(factor) || factor.length() < 10) {
                    project.setDensity("无定标");
                    projectMapper.updateByPrimaryKeySelective(project);
                    continue;
                }
                // 取得曲线
                List<Map<String, Object>> selectOneCurve = scalingIntf.selectOneCurve(id);

                //取得读数点
                String mainBegin = projectParam.getMainIndicationBegin();
                String mainEnd = projectParam.getMainIndicationEnd();



                //如果 主/辅终点 为空或者0 这使其赋值为主/辅 始点
                //取得数差
                float absorbanceGap = 0;
                //处理 读点数法
                //取得项目读数法
                String computeMethod = projectParam.getComputeMethod();

//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);
                if (absorbanceGap < 0.1F) {
                    absorbanceGap = 0.1F;
                }
//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);
                logger.info("读数法：" + computeMethod);
                float y1 = 0;
                for (Map<String, Object> map : selectOneCurve) {
                    //第n个点
                    //第n个点
                    int xThis = Integer.parseInt(String.valueOf(map.get("x")));
                    //第n个点的数据
                    float t = Float.parseFloat(String.valueOf(map.get("t")));
                    if (xThis == 1){
                        y1 = t;
                    }
                }
                if (R2t >0 ){
                    mainEnd = String.valueOf(Integer.parseInt(a)+R2t+Integer.parseInt(R2Incubation));
                    mainEnd = String.valueOf(Float.parseFloat(mainEnd)-y1);
                }
                if ("终点法".equals(computeMethod)) {
                    absorbanceGap = DateUtils.terminalMethod(selectOneCurve, mainBegin, mainEnd,R2t,ReadBegin,R2Incubation);
                }
                if ("速率法".equals(computeMethod)) {
                    //速率法 取 absorbanceGap/两数时间差*60
                    absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, ReadBegin, mainEnd,R2t, R2Incubation) / 1000;
                    logger.info("光准差：" + absorbanceGap);
                    absorbanceGap = absorbanceGap / (Float.parseFloat(a) / 60);
                }

                if ("两点法".equals(computeMethod)) {
                    //两点法
                    absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, ReadBegin, mainEnd,R2t, R2Incubation) / 1000;
                    logger.info(String.valueOf(absorbanceGap));
                }
                logger.info("计算结果得出absorbanceGap"+absorbanceGap);

                if (type == 2 || type == 6) {
                    logger.info("定标");
                    // 定标项目
                    project.setFactor(String.valueOf(new DecimalFormat("0.00").format(density / (absorbanceGap))));
                    project.setAbsorbance(String.valueOf(absorbanceGap));
                }
                logger.info("project==="+project);

                if (type == 1 || type == 3 || type == 4 || type == 5) {
                    //普通项目或者质控项目
                    logger.info("项目");

                    setDensity(density, ppi, projectParam, project, factor, absorbanceGap);
                    if (type == 3) {
                        //qc项目发送id和结果到页面
                        sendQC(oldDensity, density, ppi);
                    }
//                    logger.info(project);
                    String serialId =  "";
                    try {
                        Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
                        process.getOutputStream().close();
                        Scanner sc = new Scanner(process.getInputStream());
                        serialId = sc.next();
                        serialId = sc.next();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String hostAddress = "";
                    try {
                        InetAddress ip4 = Inet4Address.getLocalHost();
                        hostAddress = ip4.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    HttpRequest h = new HttpRequest();

                    //向121.41.111.94/show发起POST请求，并传入name参数
                    String now = DataUtil.now();
                    String encode = null;
                    try {
                        encode = URLEncoder.encode(now, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    List<String> ipAddress = getIpAddress();
                    String param = "i=" + density + "--" + type + "--" + name + "--" + serialId + "--" + encode + "--" + ipAddress.get(4) +"--" +hostAddress;
//                    String content = h.sendGet("http://server:8082/add", param);
//                    System.out.println(content);

                }
                project.setB(usedT);
                projectMapper.updateByPrimaryKeySelective(project);
            }else {
                Project project = new Project();
                project.setId(id);
                project.setB(usedT);
                projectMapper.updateByPrimaryKeySelective(project);
            }
        }
        return "";
    }


    private void setDensity(float density, int ppi, ProjectParam projectParam, Project project, String factor, float absorbanceGap) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectParamId", ppi);
        Set<Integer> singleton = new HashSet<>(Arrays.asList(2,6));

        criteria.andIn("type", singleton);
        String preDate = DataUtil.getPreDateByUnit(factor, 1, 12);
//                    logger.info("factor:"+factor);
        criteria.andBetween("starttime", factor, preDate);
        List<Project> projects = projectMapper.selectByExample(example);

//                    logger.info(projects);
        //创建浓度list
        StringBuilder den = new StringBuilder();
        //创建吸光度list
        StringBuilder abs = new StringBuilder();
        StringBuilder calDen = new StringBuilder();;
        StringBuilder calAbs = new StringBuilder();;
        //取出浓度和吸光度
        DataUtil.setDAndA(projects, den, abs, calDen, calAbs);

        double[] calx = new double[0];
        double[] rely = new double[0];
        if (null != calDen){
            calx = DataUtil.string2Double(calDen.toString());
            rely = DataUtil.string2Double(calAbs.toString());
        }

        // xy 装换成 double类型的数组
        double[] y = DataUtil.string2Double(abs.toString());
        double[] x = DataUtil.string2Double(den.toString());
        DataUtil.DealXY dealXY = new DataUtil.DealXY(x, y).invoke();
        if (dealXY.is()) {
            project.setDensity("无定标");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        ;
        x = dealXY.getX();
        double[] xX = dealXY.getxX();
        double[] yY = dealXY.getyY();
        // 曲线list
        //根据时间取得定标参数
        logger.info(factor);
        Scaling scaling = scalingMapper.queryById(factor);
        if (xX.length < 1 || (xX.length > 1 && xX[0] == xX[1]) || null == scaling) {
            project.setDensity("定标异常");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }

        String algorithm;
        if (scaling == null) {
            algorithm = "一次曲线";
        } else {
            algorithm = scaling.getAlgorithm();
        }
        //根据项目定标参数  获得项目定标算法
        algorithm = scaling.getAlgorithm();
            density = Formula.getDensity(absorbanceGap, xX, yY, algorithm, calx, rely);
        if (density == -501) {
            project.setDensity("≤0.5");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        if (density == -502) {
            project.setDensity("≥"+xX[xX.length-1]);
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        if (density == -503) {
            project.setDensity("ABS越界");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        // 普通项目
        String decimalDigit = projectParam.getDecimalDigit();
        String formatDigit = "0.00";
        switch (decimalDigit) {
            case "0位":
                formatDigit = "0";
                break;
            case "1位":
                formatDigit = "0.0";
                break;
            case "2位":
                formatDigit = "0.00";
                break;
            default:

        }
        project.setDensity(new DecimalFormat(formatDigit).format(density));
    }




    /***
     * @apiNote
     * @author tzhh
     * @date 2021/5/21 9:46
     * @param densityOld
     * @param density
     * @param ppi
     * @return
     **/
    private void sendQC(float densityOld, float density, int ppi) {
        Map<String, Object> lightQuasiMap = new HashMap<>(11);
        lightQuasiMap.put("densityOld", densityOld);
        lightQuasiMap.put("density", density);
        lightQuasiMap.put("ppi", ppi);
//        lightQuasiMap.put("msg", lightQuasiMap);
        lightQuasiMap.put("id", "qc");
        Gson gson = new Gson();
        try {
            webSocket.sendMessageTo(gson.toJson(lightQuasiMap), "user1");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static List<String> getIpAddress()   {
        List<String> list = new LinkedList<>();
        Enumeration enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (enumeration.hasMoreElements()) {
            NetworkInterface network = (NetworkInterface) enumeration.nextElement();
            Enumeration addresses = network.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = (InetAddress) addresses.nextElement();
                if (address != null && (address instanceof Inet4Address || address instanceof Inet6Address)) {
                    list.add(address.getHostAddress());
                }
            }
        }
        return list;
    }
}

package com.laola.apa.server.impl;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.entity.Scaling;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.mapper.ScalingMapper;
import com.laola.apa.mapper.SelectDao;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.server.ScalingIntf;
import com.laola.apa.utils.DataUtil;
import ij.IJ;
import ij.measure.CurveFitter;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ScalingImpl implements ScalingIntf {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    SelectDao selectDao;
    @Autowired
    ProjectParamMapper projectParamMapper;
    @Autowired
    ProjectTest projectTest;
    @Autowired
    ScalingMapper scalingMapper;
    Logger logger = Logger.getGlobal();
    private String yStep = "0.0000";

    /**
     * 取得一个项目的曲线
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> selectOneCurve(int id) {
        return selectDao.selectList("select * from project_curve where project_id=" + id);
    }

    /**
     * 根据项目参数id获得定标参数
     *
     * @param id
     * @return
     */
    @Override
    public String getFactor(int id) {
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(id);
        projectParam = projectParamMapper.selectOne(projectParam);
        return projectParam.getFactor();
    }


    /**
     * 根据项目参数id修改定标因子
     *
     * @param projectParamId
     * @return
     */
    @Override
    public int updateProjectsScaling(int projectParamId, String projectId) {
//        ProjectParam projectParam = new ProjectParam();
//        projectParam.setId(projectParamId);
//        projectParam.setFactor(projectTest.get());
        selectDao.selectList("\tUPDATE project_param SET factor=(SELECT DATE_FORMAT(starttime, \"%Y-%m-%d %H:%i\" ) FROM project WHERE id =" + projectId + ") WHERE id =" + projectParamId);

        return 1;
    }

    /**
     * 根据项目id获得项目定标的时间
     *
     * @param projectParamId
     * @return
     */
    @Override
    public List<Map<String, Object>> getOneProjectsScalingTime(int projectParamId, int type) {
        String sql = "\tSELECT\n" +
                "\t DATE_FORMAT( starttime, \"%Y年%m月%d日%H时%i分\" ) date ,project.id,pp.id 'check'\n" +
                "FROM\n" +
                "\tproject " +
                " left join project_param pp on pp.factor = DATE_FORMAT( starttime, \"%Y-%m-%d %H:%i\" ) AND pp.id = project.project_param_id\n" +
                "WHERE\n" +
                "\tproject_param_id =" + projectParamId +
                "\tAND type = 2" +
                "\tgroup by date" +
                " order by date desc";
        List<Map<String, Object>> mapList = selectDao.selectList(sql);
        return mapList;
    }

    /**
     * 修改某个定标项目数值 updateProjectParam
     */
    @Override
    public int updateProject(Project project) {
        return projectMapper.updateByPrimaryKeySelective(project);
    }


    /**
     * 获得项目最新的定标
     * Get the latest project calibration
     *
     * @param projectParamId
     * @return
     */
    @Override
    public List<Map<String, Object>> getLatestOne(Integer projectParamId) {
        String sql = "SELECT * FROM project WHERE\n" +
                "\tDATE_FORMAT( starttime, '%y年%m月%d日%H时%i分' ) = ( SELECT DATE_FORMAT( starttime, '%y年%m月%d日%H时%i分' ) " +
                "FROM project WHERE project_param_id = " + projectParamId + " AND type = 2 ORDER BY starttime DESC LIMIT 1 ) AND" +
                " project_param_id = " + projectParamId + " AND type = 2";
        return selectDao.selectList(sql);
    }

    /**
     * 根据项目参数id 和类型 和 时间获得所有  project
     *
     * @param projectParamId
     * @return
     */
    @Override
    public Map<String, List> getOneProjectsAllScalingByCon(int projectParamId, int type, String time) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectParamId", projectParamId);
        criteria.andEqualTo("type", type);
        if (null != time && !"".equals(time)) {
            String preDate = DataUtil.getPreDateByUnit(time, 1, 12);
            time = time.replace("年", "-");
            time = time.replace("月", "-");
            time = time.replace("日", " ");
            time = time.replace("时", ":");
            time = time.replace("分", "");
            System.out.println(time);
//            System.out.println(preDate);
            criteria.andBetween("starttime", time, preDate);
        }

        //
        List<Project> projects = projectMapper.selectByExample(example);
        //从定标项目中取得定标算法
        //决定使用什么算法
        List<Object> value = dealByAlgorithm(time, projects);

        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("value", value);
        resultMap.put("projects", projects);
        return resultMap;
    }

    /**
     * 先取得吸光度和浓度
     * 再从定标项目中取得定标算法
     * 决定使用什么算法
     *
     * @param time
     * @param projects
     * @return
     */
    private List<Object> dealByAlgorithm(String time, List<Project> projects) {

        //根据时间取得定标参数
        logger.info(time);
        Scaling scaling = scalingMapper.queryById(time);
        String algorithm;
        if (scaling == null) {
            algorithm = "一次曲线";
            Scaling scaling1 = new Scaling();
            scaling1.setDateid(time);
            scaling1.setAlgorithm("一次曲线");
            scalingMapper.insert(scaling1);
        } else {
            algorithm = scaling.getAlgorithm();
        }

        List<Float> valueList = new ArrayList<>();
        //创建浓度list
        StringBuilder den = new StringBuilder();
        //创建吸光度list
        StringBuilder abs = new StringBuilder();
        //取出浓度和吸光度
        for (Project project : projects) {
            den.append(project.getDensity()).append(",");
            abs.append(project.getAbsorbance()).append(",");
        }
        // xy 装换成 double类型的数组 // xy交换 - 20201013
        double[] y = DataUtil.string2Double(abs.toString());
        double[] x = DataUtil.string2Double(den.toString());

        DataUtil.DealXY dealXY = new DataUtil.DealXY(x, y).invoke();
        ArrayList<Object> objects = new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("algorithm", algorithm);
        objects.add(map1);
        if (dealXY.is()) return objects;
        x = dealXY.getX();
        double[] xX = dealXY.getxX();
        double[] yY = dealXY.getyY();
        // 曲线list


        if (xX.length < 3 && "三次样条函数".equals(algorithm)) {
            algorithm = "一次曲线";
        }

        logger.info(algorithm);

        if (y[y.length - 1] - y[0] < 1) {
            yStep = "0.000";
        }
        if (y[y.length - 1] - y[0] < 0.1) {
            yStep = "0.0000";
        }
        if (y[y.length - 1] - y[0] < 0.01) {
            yStep = "0.00000";
        }
        if (y[y.length - 1] - y[0] < 0.001) {
            yStep = "0.000000";
        }
        if ("三次样条函数".equals(algorithm)) {
            // 样条曲线
            splineInterpolation(xX, yY, valueList);
        }
        if ("一次曲线".equals(algorithm)) {
            //一次曲线
            Instance(xX, yY, valueList);
        }
        if ("二次曲线拟合".equals(algorithm)) {
            //二次曲线拟合
            quadratic(xX, yY, valueList);
        }
        if ("RodBard".equals(algorithm)) {
           //log4p
            RodBard(xX, yY, valueList);
        }
        if ("三次曲线拟合".equals(algorithm)) {
            //三次曲线拟合
            cubicCurveFitting(xX, yY, valueList);
        }

        float max = 0;
        float min = 0;
        float maxX = 0;
        float minX = 0;
        //取得最大值 和最小值
        logger.info(String.valueOf(valueList.size()));
        for (Float val : valueList) {
            logger.info(String.valueOf(val));
            //取最大的 y
            if (val > max) {
                max = val;
            }
            //取最小的Y
            if (val < min) {
                min = val;
            }
        }
        if (max < yY[yY.length-1]){
            max = (float) yY[yY.length-1];
        }
        System.out.println("curve max"+max + "::::::::min" + min);

        float yLength = 200;

        float gap = (max - min) / yLength;
        if (max == min) {
            min = max;
            gap = 0.1f;
        }
        List<Object> resultList = new ArrayList<>();
        for (Float val : valueList) {
            //减去最小值算份数
            resultList.add((val - min) / gap);
        }
        float step = 10;
        if (x[x.length - 1] - x[0] < 1) {
            step = 100;
        }
        if (x[x.length - 1] - x[0] < 0.1) {
            step = 1000;
        }

        maxX = (float) xX[xX.length - 1];
        minX = (float) xX[0];
        if ("三次样条函数".equals(algorithm)) {
            maxX = ((maxX / (5 / step)) + 1) * (5 / step);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("max", max);
        param.put("min", min);


        param.put("maxX", maxX);
        if (minX > 0) {
            minX = minX*0.9F;
        }
        param.put("minX", minX);
        param.put("algorithm", algorithm);

        param.put("step", step);
        resultList.add(param);
        return resultList;
    }

    private void cubicCurveFitting(double[] x, double[] y, List<Float> value) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(2);
        double[] p = curveFitter.getParams();
        double a = Double.parseDouble(IJ.d2s(p[0], 3, 5));
        double b = Double.parseDouble(IJ.d2s(p[1], 3, 5));
        double c = Double.parseDouble(IJ.d2s(p[2], 3, 5));
        double d = Double.parseDouble(IJ.d2s(p[3], 3, 5));
        double minKey;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(a+b*key+c*key*key+d*key*key*key)));
        }

    }

    private void RodBard(double[] x, double[] y, List<Float> value) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(7);
        double[] p = curveFitter.getParams();
        double a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
        double b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
        double c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
        double d = Double.parseDouble(IJ.d2s(p[3], 5, 9));
        double minKey;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(d + (a - d) / (1 + Math.pow((key / c), b)))));
        }
    }

    /**
     * 取得一次多项式
     *
     * @param y
     * @param x
     * @param value
     */
    private void Instance(double[] x, double[] y, List<Float> value) {
        //多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(1);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            //遍历xy值，并加入权重坐标点
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
            weightedObservedPoints.add(weightedObservedPoint);
        }

        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);

        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[1] * key + doubles[0])));

        }
    }

    /**
     * 取得二次多项式
     *
     * @param y
     * @param x
     * @param value
     */
    private void quadratic(double[] x, double[] y, List<Float> value) {
        //多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(2);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            //遍历xy值，并加入权重坐标点
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
            weightedObservedPoints.add(weightedObservedPoint);
        }

        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);

        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[2] * key * key + doubles[1] * key + doubles[0])));
        }
    }

    /**
     * 取得三次样条多项式
     *
     * @param y
     * @param x
     * @param value
     */
    private void splineInterpolation(double[] x, double[] y, List<Float> value) {
        SplineInterpolator sp = new SplineInterpolator();
        //插入样条值
        PolynomialSplineFunction interpolate = sp.interpolate(x, y);
        //取得三次样条多项式
        PolynomialFunction[] polynomials = interpolate.getPolynomials();
//        int n = interpolate.getN();
        // knots is key list
        double[] knots = interpolate.getKnots();
        //v is
        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        ;
        double step = 0.1;
        double maxX = x[x.length - 1];
        if (maxX - minKey < 1) {
            step = 0.01;
        }
        if (maxX - minKey < 0.1) {
            step = 0.001;
        }
        double maxKey = ((maxX / (step * 5)) + 1) * (step * 5);
        for (double key = minKey; key < maxKey; key = key + step) {
            int i = Arrays.binarySearch(knots, key);

            if (i < 0) {
                i = -i - 2;
            }
            // key 大于最大的x值
            if (key > maxX) {
                i = x.length - 1;
            }
            // key小于最小的x值
            if (key <= x[0]) {
                i = 0;
            }
            // 这将处理v是最后一个结值的情况 只有n-1多项式，所以如果v是最后一个结
            //然后我们用最后一个多项式来计算这个值。
            if (i >= polynomials.length) {
                i--;
            }
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(polynomials[i].value(key - knots[i]))));
        }
    }


    /**
     * 添加定标项目  时间+计算方法
     *
     * @return
     */
    @Override
    public int insertScalingAlgorithm(Scaling scaling) {
        return scalingMapper.insert(scaling);
    }

    /**
     * 更新定标项目计算方法
     * @param scaling
     * @return
     */
    @Override
    public int updateScalingAlgorithm(Scaling scaling) {
        return scalingMapper.update(scaling);
    }

    /**
     *删除一个定标
     * @param dateId
     * @return
     */
    @Override
    public int deleteOneScaling(String dateId) {
        return scalingMapper.deleteById(dateId);
    }


}

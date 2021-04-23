//package com.laola.apa;
//
//import com.laola.apa.entity.ProjectParam;
//import com.laola.apa.mapper.ProjectParamMapper;
//import com.laola.apa.mapper.RegentPlaceMapper;
//import com.laola.apa.server.ParamIntf;
//import com.laola.apa.server.ReagentPlaceIntf;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DlaApaApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class LaolaTest {
//    @Autowired
//    private ProjectParamMapper projectParamMapper;
//    @Autowired
//    private ParamIntf paramIntf;
//    @Autowired
//    private ReagentPlaceIntf reagentPlaceIntf;
//
//    @Autowired
//    private RegentPlaceMapper RegentPlaceMapper;
//
//
//    @Test
//    public void fTest(){
////        Integer copies = reagentPlaceIntf.getCopies(3121);
////        System.out.println(copies);
////        List<ProjectParam> projectParams = projectParamMapper.selectAll();
////        for (ProjectParam projectParam : projectParams
////             ) {
////            System.out.println(projectParam.getName()+ projectParam.getChineseName());
////        }
//    }
//
////    @Test
//    public void projectListTest(){
////        List<ProjectListVO> projectListVOS = paramIntf.projectMap();
////        for (ProjectListVO projectListVO:projectListVOS
////        ) {
////            System.out.println(projectListVO.getId()+"\t"+projectListVO.getName());
////        }
//    }
//
////    @Test
//    public void onePojectTest() throws Exception {
//        ProjectParam projectParam = paramIntf.onePoject(3);
//        Field[] fields = projectParam.getClass().getDeclaredFields();
//        for (Field field:fields
//        ) {
//            // 获取属性的名字
//            String name = field.getName();
//            // 将属性的首字符大写，方便构造get，set方法
//            String aname = name.substring(0, 1).toUpperCase() + name.substring(1);
//            Method method = projectParam.getClass().getMethod("get" + aname);
//            System.out.println(name+"\t"+ method.invoke(projectParam)
//            );
//        }
//
//
//    }
////    @Test
//    public void update(){
//        ProjectParam projectParam = new ProjectParam();
//        projectParam.setId(3);
//        projectParam.setNormalHigh("10");
//        projectParam.setNormalLow("0");
//        projectParam.setMinAbsorbance("-2");
//        projectParam.setMaxAbsorbance("10");
//        paramIntf.update(projectParam);
//    }
//}

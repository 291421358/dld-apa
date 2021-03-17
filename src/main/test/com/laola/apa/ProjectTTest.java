package com.laola.apa;

import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.utils.Formula;
import ij.IJ;
import ij.gui.Plot;
import ij.measure.CurveFitter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectTTest extends LaolaTest {
//    @Autowired
//    ProjectTest projectCurveMapper;
    @Autowired
ProjectMapper projectMapper;
//    @Autowired
//    SelectDao selectMapper;
//    @Autowired
    private ProjectTest projectTest;
//    @Autowired
//    ReagentPlaceIntf regentPlaceIntf;
//    @Autowired
//    private ProjectTest projectTest;
//    @Autowired
//    private GetProjectResult getProjectResult;
//    @Test
    public void project1() {
        List<Map<String, Object>> projects = new ArrayList<>();
        HashMap<String,Object> project = new HashMap<>();
        project.put("id",null);
        project.put("projectParamNum","11");
        project.put("staQuality","12");
        projects.add(project);
        projectMapper.insertProjectList(projects);
    }
    @Test
    public void project() {
        double x[] = {0.001,5,15};
        double y[] = {0.001,0.1,1};
        CurveFitter curveFitter = new CurveFitter(x,y);
        curveFitter.doFit(7);
//        String name = curveFitter.getName();
        String formula = curveFitter.getFormula();
        double[] yPoints = curveFitter.getYPoints();
        int fit = curveFitter.getFit();
        String macroCode = curveFitter.getMacroCode();
        StringBuilder legend = new StringBuilder(100);
        legend.append(curveFitter.getFormula());
        legend.append('\n');
        double[] p = curveFitter.getParams();
        int n = curveFitter.getNumParams();
        char pChar = 'a';

        for(int i = 0; i < n; ++i) {
            legend.append(pChar + " = " + IJ.d2s(p[i], 5, 9) + '\n');
            ++pChar;
        }
        String a = IJ.d2s(p[0], 5, 9);
        String b = IJ.d2s(p[1], 5, 9);
        String c = IJ.d2s(p[2], 5, 9);
        String d = IJ.d2s(p[3], 5, 9);
        double pow = Math.pow(10, Double.parseDouble(b));
        legend.append("R^2 = " + IJ.d2s(curveFitter.getRSquared(), 4));
        legend.append('\n');
        Plot plot = curveFitter.getPlot(1);
    }

    public static void main(String[] a){
        try {
            URL url=new URL("https://yz.lol.qq.com/zh_CN/story/champion/syndra/");
            BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer=new BufferedWriter(new FileWriter("cindex.html"));
            String line;
            while((line=reader.readLine())!=null){
                System.out.println(line);
                writer.write(line);
                writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

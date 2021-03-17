package com.laola.apa;

import com.laola.apa.utils.DateUtils;
import com.laola.apa.utils.SerialUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DlaApaApplication.class)
public class SerialTest {
    static OutputStream outputStream;
//    @Test
    public void e59083(){
        String command = "E5 90 83 00 00 00 00 00 00 00 00 00 00 00 00 00";
        SerialUtil serialUtil = new SerialUtil();
        serialUtil.init(command);
         outputStream = serialUtil.getOutputStream();
        byte[] bytes = DateUtils.hexStrToBinaryStr(command);
        System.out.println("发出：" + command);
        try {
            outputStream.write(bytes, 0,
                    bytes.length);
        }catch (Exception e){

        }

        System.out.println(outputStream);
    }
    /**
     * int i = cRead.startComPort();
     *         if (true) {
     *             // 启动线程来处理收到的数据
     *             try {
     *                 String st = "E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00";
     *                 byte[] bytes = DateUtils.hexStrToBinaryStr(st);
     *                 System.out.println("发出：" + st);
     *                 outputStream.write(bytes, 0,
     *
     *                         bytes.length);
     *             } catch (IOException e) {
     *                 // TODO Auto-generated catch block
     *                 e.printStackTrace();
     *             }
     *
     *         } else {
     *             return;
     *         }
     */
    private static String PATH = "E:\\ideaProject\\dld-apa\\src\\main\\resources\\static\\js\\patients";

    private static int Number = 0;

    private static List<String> JavaCodeFilePath = new ArrayList<>();

//    public static void main(String[] args) {
//        getCodeFilePath(PATH);
//
//
//
//        JavaCodeFilePath.forEach(file->{
//            StatisticsCodeNumber(new File(file));
//        });
//
//        System.out.println(Number);
//
//    }

    private static void StatisticsCodeNumber(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine())!= null) {
                Number++;
            }
            fis.close();
            br.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static void getCodeFilePath(String path) {
        File file = new File(path);
        File[] filesArr = file.listFiles();
        if (filesArr == null) {
            return;
        } else {
            for (File item : filesArr) {
                if (item.isDirectory()) {
                    getCodeFilePath(item.getPath());
                } else {
                    if(item.getPath().substring(item.getPath().lastIndexOf(".")).equals(".js"))
                        JavaCodeFilePath.add(item.getPath());
                }
            }
        }
}
}
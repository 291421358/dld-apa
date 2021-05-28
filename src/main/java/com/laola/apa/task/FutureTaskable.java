package com.laola.apa.task;

import com.laola.apa.utils.SerialUtil;

import java.util.concurrent.*;

public class FutureTaskable   {


    public static ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * @apiNote 开启发送读取温度命令任务
     * @author tzhh
     * @date 2021/5/27 17:47
     * @param null
     * @return {@link null}
     **/
    public static FutureTask<?> futureTask = new FutureTask<Long>(new Callable<Long>() {
        @Override
        public Long call() throws Exception {
            Thread.currentThread().setName("t_1");
            String name = Thread.currentThread().getName();
            System.out.println("thread_name:"+name);
            for(int i=0;i < 10000&&!Thread.currentThread().isInterrupted();i++){
                SerialUtil.sendCommond("E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00");
                Thread.sleep(15000);
            }
            return Thread.currentThread().getId();
        }
    });


    public static  void main(String[] args) {
        executor.execute(futureTask);
        Future<?> submit = executor.submit(futureTask);
        System.out.println("futureTask start");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        executor.shutdownNow();
        futureTask.cancel(true);
        System.out.println("futureTask cancel");
    }

    /**
     * 停止一个线程
     */
    public static void stop(){
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        for (int j = 0; j < noThreads; j++) {

            String nm = lstThreads[j].getName();
            if (nm.equals("t_1")) {
                lstThreads[j].stop();
                System.out.println("STOP THREAD："+nm);
            }
        }
    }

}

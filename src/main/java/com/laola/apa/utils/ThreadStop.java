package com.laola.apa.utils;

public class ThreadStop extends Thread {
    //保证尽快开始处理
    private volatile boolean shutdownRequested = false;

    public final void run() {
        while (!shutdownRequested) {
            try {
                doWork();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            doShutDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void doWork() throws InterruptedException {
        System.out.println("doWork");
        doShutDown();
    }

    void doShutDown() throws InterruptedException {
        shutdownRequested = true;
        interrupt();
    }

    public static void main(String[] args) throws Exception {
        ThreadStop ts = new ThreadStop();
        ts.run();
        ts.doShutDown();
    }

}

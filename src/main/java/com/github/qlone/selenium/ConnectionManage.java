package com.github.qlone.selenium;

import okhttp3.internal.Util;
import org.jsoup.Connection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.*;

/**
 * 方便管理webdriver，防止多开窗口占用内存
 * @author heweinan
 * @date 2020-12-03 10:19
 */
public class ConnectionManage {

    private int maxRequest = 5;
    private Semaphore semaphore = new Semaphore(maxRequest);

    private ExecutorService executorService;

    public ConnectionManage(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ConnectionManage() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), Util.threadFactory("ConnectionManage", false));
        }
        return executorService;
    }

    public void setMaxRequest(int maxRequest) {
        if(maxRequest < 1){
            throw new IllegalArgumentException("max < 1: " + maxRequest);
        }
        this.maxRequest = maxRequest;
        this.semaphore = new Semaphore(maxRequest);
    }

    public Connection.Response execute(Connection connection) throws ExecutionException, InterruptedException {
        try {
            semaphore.acquire();
            FutureTask<Connection.Response> task = new FutureTask<>(connection::execute);
            executorService().submit(task);
            return task.get();
        } finally {
            semaphore.release();
        }
    }
}

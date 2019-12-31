package com.cf.glidedemo.glide;

import android.content.Context;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @作者：陈飞
 * @说明：这个是所有请求分配的管理者，负责分配到哪个请求去处理
 * @创建日期: 2019/12/27 16:40
 */
public class RequestManager {

    private static RequestManager requestManager;
    //分配请求队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();

    private BitmapDispatcher[] bitmapDispatchers;//管理者需要分配多少处理器来处理请求，处理器的多少是由手机内存线程数量来分配的


    /**
     * @作者：陈飞
     * @说明：线程池管理线程
     * @创建日期: 2019/12/30 15:13
     */
    public ExecutorService executorService;


    //上下文
    private Context context;


    public static RequestManager getInstance(Context context) {
        if (requestManager == null) {
            synchronized (DiskBitmapCache.class) {
                if (requestManager == null) {
                    requestManager = new RequestManager(context);
                }
            }
        }
        return requestManager;
    }


    private RequestManager(Context context) {
        this.context = context;
        //初始化线程池
        initThreadExecutor();
        //只有一个管理者
        start();
    }


    //初始化线程池
    public void initThreadExecutor() {
        //获取手机线程数量
        int size = Runtime.getRuntime().availableProcessors();
        if (size <= 0) {
            size = 1;
        }
        size *= 2;
        //创建固定的线程池
        executorService = Executors.newFixedThreadPool(size);
    }

    /**
     * @作者：陈飞
     * @说明：开始请求
     * @创建日期: 2019/12/30 16:43
     */
    public void start() {
        stop();
        startAllDispatcher();
    }


    public void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();//中断
                }
            }
        }
    }

    //处理并开始所有线程
    public void startAllDispatcher() {
        //获取手机最大线程数量
        final int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        if (bitmapDispatchers.length > 0) {
            for (int i = 0; i < threadCount; i++) {
                //线程开辟的请求分发去抢请求资源对象，谁抢到了，就由谁处理
                BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue, context);
                executorService.execute(bitmapDispatcher);
                //将每个dispatcher放到数组中，方便管理
                bitmapDispatchers[i] = bitmapDispatcher;
            }
        }
    }

    /**
     * @作者：陈飞
     * @说明：这里收集所有的请求
     * @创建日期: 2019/12/30 16:44
     */
    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) {
            return;
        }
        if (!requestQueue.contains(bitmapRequest)) {
            requestQueue.add(bitmapRequest);//将请求加入队列
        }
    }

}

package com.cf.glidedemo.glide;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *@作者：陈飞
 *@说明：bitmap下载显示线程
 *@创建日期: 2019/12/30 15:08
 */
public class BitmapDispatcher extends Thread {
    //主线程handler
    Handler handler = new Handler(Looper.getMainLooper());

    //创建一个阻塞队列 线程
    private LinkedBlockingQueue<BitmapRequest> requestsQueue;

    //获取三级缓存对象 //磁盘，内存
    private DoubleLruCache doubleLruCache;

    public BitmapDispatcher(LinkedBlockingQueue<BitmapRequest> requestsQueue, Context context) {
        this.requestsQueue = requestsQueue;
        doubleLruCache = new DoubleLruCache(context);
    }

    @Override
    public void run() {
        super.run();

        //如果该线程没有中断
        while (!isInterrupted()) {
            if (requestsQueue == null) {
                continue;
            }
            try {
                BitmapRequest bitmapRequest = requestsQueue.take();
                if (bitmapRequest == null) {
                    continue;
                }
                //设置占位图片
                showLoadingImg(bitmapRequest);
                //网络加载图片资源
                Bitmap bitmap = findBitmap(bitmapRequest);
                //将图片显示到ImageView
                showImageView(bitmapRequest, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @作者：陈飞
     * @说明：显示占位图片
     * @创建日期: 2019/12/30 14:45
     */
    private void showLoadingImg(BitmapRequest bitmapRequest) {
        final ImageView imageView = bitmapRequest.getImageView();
        if (bitmapRequest.getResId() > 0 && imageView != null) {
            final int resId = bitmapRequest.getResId();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }

    /**
     * @作者：陈飞
     * @说明：显示到imageView
     * @创建日期: 2019/12/30 15:00
     */
    private void showImageView(final BitmapRequest bitmapRequest, final Bitmap bitmap) {
        final ImageView imageView = bitmapRequest.getImageView();
        if (bitmap != null && imageView != null && bitmapRequest.getUrlMd5().equals(imageView.getTag())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                    RequestListener requestListener = bitmapRequest.getRequestListener();
                    if (requestListener != null) {
                        requestListener.onSuccess(bitmap);
                    }
                }
            });
        } else {
            RequestListener requestListener = bitmapRequest.getRequestListener();
            if (requestListener != null) {
                requestListener.onFaile();
            }
        }
    }

    /**
     * @作者：陈飞
     * @说明：查找图片
     * @创建日期: 2019/12/30 14:51
     */
    private Bitmap findBitmap(BitmapRequest bitmapRequest) {
        // 这里需要通过三级缓存缓存图片
        Bitmap bitmap = null;
        bitmap = doubleLruCache.get(bitmapRequest);
        //三级缓存没有图片的时候就去下载
        if (bitmap == null) {
            bitmap = downloadBitmap(bitmapRequest.getUrl());
            //下载完后放入三级缓存
            if (bitmap != null) {
                doubleLruCache.put(bitmapRequest, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * @作者：陈飞
     * @说明：下载图片
     * @创建日期: 2019/12/30 14:52
     */
    private Bitmap downloadBitmap(String uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}

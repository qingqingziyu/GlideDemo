package com.cf.glidedemo.glide;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @作者：陈飞
 * @说明：内存缓存
 * @创建日期: 2019/12/30 9:09
 */
public class MemberLruCache implements BitmapCache {

    private LruCache<String, Bitmap> lruCache;

    private static volatile MemberLruCache instance;

    private static final byte[] lock = new byte[0];

    public static MemberLruCache getInstance() {
        if (instance == null) {
            synchronized (MemberLruCache.class) {
                if (instance == null) {
                    instance = new MemberLruCache();
                }
            }
        }

        return instance;
    }

    public MemberLruCache() {
        //获取最大内存
        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory()) / 16;
        if (maxMemorySize <= 0) {
            maxMemorySize = 10 * 1024 * 1024;//开辟10M的缓存空间
        }
        lruCache = new LruCache<String, Bitmap>(maxMemorySize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //计算Bitmap所占的内存，行*高
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public void put(BitmapRequest bitmapRequest, Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(bitmapRequest.getUrlMd5(), bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest bitmapRequest) {
        return lruCache.get(bitmapRequest.getUrlMd5());
    }

    @Override
    public void remove(BitmapRequest bitmapRequest) {
        lruCache.remove(bitmapRequest.getUrlMd5());
    }
}

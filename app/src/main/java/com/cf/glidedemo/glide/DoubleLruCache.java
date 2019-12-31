package com.cf.glidedemo.glide;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @作者：陈飞
 * @说明：双缓存 磁盘，内存
 * @创建日期: 2019/12/30 9:01
 */
public class DoubleLruCache implements BitmapCache {

    //磁盘缓存
    private DiskBitmapCache diskBitmapCache;
    //内存缓存
    private MemberLruCache memberLruCache;

    public DoubleLruCache(Context context) {
        memberLruCache = MemberLruCache.getInstance();
        diskBitmapCache = DiskBitmapCache.getInstance(context);
    }

    @Override
    public void put(BitmapRequest bitmapRequest, Bitmap bitmap) {
        memberLruCache.put(bitmapRequest, bitmap);
        diskBitmapCache.put(bitmapRequest, bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest bitmapRequest) {
        //先读取内存缓存
        Bitmap bitmap = memberLruCache.get(bitmapRequest);
        //如果内存缓存不存在，读取磁盘
        if (bitmap == null) {
            bitmap = diskBitmapCache.get(bitmapRequest);
            //磁盘读取到，存入内存
            if (bitmap != null) {
                memberLruCache.put(bitmapRequest, bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest bitmapRequest) {
        memberLruCache.remove(bitmapRequest);
        diskBitmapCache.remove(bitmapRequest);
    }
}

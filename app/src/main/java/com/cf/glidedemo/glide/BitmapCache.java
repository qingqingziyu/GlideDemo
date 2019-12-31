package com.cf.glidedemo.glide;

import android.graphics.Bitmap;


/**
 *@作者：陈飞
 *@说明：Bitmap存入缓存接口
 *@创建日期: 2019/12/30 9:01
 */
public interface BitmapCache {

    void put(BitmapRequest bitmapRequest, Bitmap bitmap);//存入内存

    Bitmap get(BitmapRequest bitmapRequest);//读取内存中的图片

    void remove(BitmapRequest bitmapRequest);//清除内存中的图片
}

package com.cf.glidedemo.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    //请求路径
    private String url;

    //上下文
    private Context context;

    //占位图片
    private int resId;

    //需要加载图片的控件
    private SoftReference<ImageView> imageView;

    //回调对象
    private RequestListener requestListener;

    //图片标志，用md5加密
    private String urlMd5;


    public BitmapRequest(Context context) {
        this.context = context;
    }

    //链式调度

    //加载url
    public BitmapRequest load(String url) {
        this.url = url;

        //判断网址不为空
        if (!TextUtils.isEmpty(url)) {
            this.urlMd5 = MD5.MD516(url);
        }
        return this;
    }

    //设置占位图片
    public BitmapRequest loading(int resId) {
        this.resId = resId;
        return this;
    }

    //设置监听器
    public BitmapRequest setListener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    //设置显示图片控件
    public void into(ImageView imageView) {
        imageView.setTag(urlMd5);
        this.imageView = new SoftReference<>(imageView);
        RequestManager.getInstance(context).addBitmapRequest(this);
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public int getResId() {
        return resId;
    }

    public String getUrl() {
        return url;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }
}

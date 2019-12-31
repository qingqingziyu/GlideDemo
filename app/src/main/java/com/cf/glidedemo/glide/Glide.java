package com.cf.glidedemo.glide;

import android.content.Context;

/**
 * @作者：陈飞
 * @说明：手写Glide
 * @创建日期: 2019/12/27 15:22
 */
public class Glide {

    public static BitmapRequest with(Context context) {
        return new BitmapRequest(context);
    }
}

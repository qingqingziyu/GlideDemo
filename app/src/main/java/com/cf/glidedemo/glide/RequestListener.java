package com.cf.glidedemo.glide;

import android.graphics.Bitmap;

public interface RequestListener {
    boolean onSuccess(Bitmap bitmap);

    boolean onFaile();
}

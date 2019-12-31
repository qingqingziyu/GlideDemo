package com.cf.glidedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cf.glidedemo.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = findViewById(R.id.imageView);

        //开始显示
        findViewById(R.id.startImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(MainActivity.this).load("https://7169-qingqingziyu-oic90-1300574624.tcb.qcloud.la/backgrounds/bg1.jpg?sign=37c8db63d02211b407c9ca045400d441&t=1577758775").into(imageView);
            }
        });
    }
}

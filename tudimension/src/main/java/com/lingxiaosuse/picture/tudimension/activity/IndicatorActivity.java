package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IndicatorActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue4)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.first)
                .title("优质图源")
                .description("定时刷新图片")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue3)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.second)
                .title("定制专题")
                .description("创作自己的图片专题")
                .build());
        /*addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue2)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.thred)
                .title("在这里")
                .description("可以看一些喜欢的东西")
                .build());*/
        //addSlide(new CustomSlide());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue1)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.last)
                .title("图次元")
                .description("做一个简单的图片浏览器")
                .build(),
                new MessageButtonBehaviour(view -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    SpUtils.putBoolean(getApplicationContext(), ContentValue.ISFIRST_KEY,false);
                    finish();
                },"准备开车"));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(getApplicationContext(), ContentValue.ISFIRST_KEY,false);
        finish();
    }
}

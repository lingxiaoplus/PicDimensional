package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;

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
                .title("第一页取个什么标题好呢")
                .description("不知道说些什么，看图吧")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue3)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.second)
                .title("第二页了")
                .description("还是不知道说些啥--")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue2)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.thred)
                .title("这是第三页了")
                .description("............")
                .build());
        //addSlide(new CustomSlide());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.lightblue1)
                .buttonsColor(R.color.lightbluelast)
                .image(R.drawable.last)
                .title("好了，终于到最后一页了")
                .description("妹子们都在等着你呢，赶紧的")
                .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        SpUtils.putBoolean(getApplicationContext(), ContentValue.ISFIRST_KEY,false);
                        finish();
                    }
                },"准备开车！"));
    }
}

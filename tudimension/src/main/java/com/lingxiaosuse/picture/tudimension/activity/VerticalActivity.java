package com.lingxiaosuse.picture.tudimension.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lingxiaosuse.picture.tudimension.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalActivity extends BaseActivity {

    @BindView(R.id.toolbar_vertical)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}

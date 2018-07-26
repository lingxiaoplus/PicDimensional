package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.lingxiaosuse.picture.tudimension.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CrashActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.tv_errormsg)
    TextView tvErrormsg;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_crash;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Intent intent = getIntent();
        String errorMsg = intent.getStringExtra("msg");
        tvErrormsg.setText(errorMsg);
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("程序坏了");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityController.finishAll();
            finish();
            System.exit(0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityController.finishAll();
        finish();
        System.exit(0);
    }
}

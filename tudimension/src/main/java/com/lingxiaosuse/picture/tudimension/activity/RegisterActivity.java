package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.login.InputPhoneFragment;
import com.lingxiaosuse.picture.tudimension.fragment.login.RegisterFragment;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;

    private List<BaseFragment> mFragmentList = new ArrayList<>();
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("注册");
        BaseFragment fragment1 = new InputPhoneFragment();
        BaseFragment fragment2 = new RegisterFragment();
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);

        checkoutFragment(0,null);
    }

    public void checkoutFragment(int position, Bundle bundle){
        BaseFragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = mFragmentList.get(position);
        for (int i = 0; i < mFragmentList.size(); i++) {
            if (i != position){
                //未选中的要隐藏
                transaction
                        .hide(mFragmentList.get(i));

            }
        }
        if (null != bundle){
            fragment.setArguments(bundle);
        }
        if (position != 0){
            transaction.addToBackStack(null);
        }
        transaction.add(R.id.fl_register,fragment,position+"")
                .show(fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

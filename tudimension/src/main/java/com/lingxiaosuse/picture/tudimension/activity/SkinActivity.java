package com.lingxiaosuse.picture.tudimension.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.camera.lingxiao.common.RxBus;
import com.camera.lingxiao.common.SkinChangedEvent;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.camera.lingxiao.common.widget.BaseHolder;
import com.camera.lingxiao.common.widget.BaseRecyclerViewAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiao.skinlibrary.SkinLib;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.SkinAdapter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.RippleAnimation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SkinActivity extends BaseActivity{

    @BindView(R.id.toolbarSkin)
    Toolbar toolbarSkin;
    @BindView(R.id.recySkin)
    RecyclerView recySkin;

    private List<Integer> colorList = new ArrayList<>();
    private List<String> colorName = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_skin;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        toolbarSkin.setTitle("主题风格");
        setSupportActionBar(toolbarSkin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        colorList.add(R.color.colorPrimary);
        colorList.add(R.color.red_300);
        colorList.add(R.color.blue300);
        colorList.add(R.color.indigo300);
        colorList.add(R.color.deepPurple300);
        colorList.add(R.color.pink_300);
        colorList.add(R.color.orange_300);
        colorList.add(R.color.teal_300);

        colorName.add("red300");
        colorName.add("red300");
        colorName.add("blue300");
        colorName.add("indigo300");
        colorName.add("deepPurple300");
        colorName.add("pink300");
        colorName.add("orange300");
        colorName.add("teal300");
        //final int pos = SpUtils.getInt(this, ContentValue.SKIN_POSITION,0);
        SkinAdapter adapter = new SkinAdapter(colorList, new BaseRecyclerViewAdapter.AdapterListener() {
            @Override
            public void onItemClick(BaseHolder holder, Object o,int position) {
                RippleAnimation.create(holder.getView(R.id.bt_skin_use)).setDuration(1000).start();
                //SpUtils.putInt(UIUtils.getContext(), ContentValue.SKIN_POSITION,position);
                if (holder.getAdapterPosition() == 0){
                    SkinLib.resetSkin();
                }else {
                    SkinLib.loadSkin(colorName.get(position));
                    //UIUtils.changeSkinDef(colorName.get(position));
                }
                RxBus.getInstance().post(new SkinChangedEvent(colorList.get(position)));
                ToastUtils.show(colorName.get(position));
                SpUtils.putInt(UIUtils.getContext(),ContentValue.SKIN_ID,colorList.get(position));
                //EventBus.getDefault().post(new SkinChangeEvent(colorList.get(position)));
            }

            @Override
            public void onItemLongClick(BaseHolder holder, Object o,int position) {

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recySkin.setLayoutManager(manager);
        recySkin.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

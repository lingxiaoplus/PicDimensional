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

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.SkinAdapter;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkinActivity extends BaseActivity {

    @BindView(R.id.toolbarSkin)
    Toolbar toolbarSkin;
    @BindView(R.id.recySkin)
    RecyclerView recySkin;

    private List<Integer> colorList = new ArrayList<>();
    private List<String> colorName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        ButterKnife.bind(this);
        toolbarSkin.setTitle("主题风格");
        setSupportActionBar(toolbarSkin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        initData();

    }

    private void initData() {
        colorList.add(R.color.colorPrimary);
        colorList.add(R.color.red_300);
        colorList.add(R.color.blue300);
        colorList.add(R.color.indigo300);
        colorList.add(R.color.deepPurple300);

        colorName.add("red300");
        colorName.add("red300");
        colorName.add("blue300");
        colorName.add("indigo300");
        colorName.add("deepPurple300");
        //final int pos = SpUtils.getInt(this, ContentValue.SKIN_POSITION,0);
        final SkinAdapter adapter = new SkinAdapter(colorList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recySkin.setLayoutManager(manager);
        recySkin.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseHolder holder, int position) {
                RippleAnimation.create(holder.getView(R.id.bt_skin_use)).setDuration(2000).start();
                //SpUtils.putInt(UIUtils.getContext(), ContentValue.SKIN_POSITION,position);
                if (position == 0){
                    UIUtils.restoreDefaultTheme();
                }else {
                    UIUtils.changeSkinDef(colorName.get(position));
                }

                //EventBus.getDefault().post(new SkinChangeEvent(colorList.get(position)));
            }
        });

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

package com.lingxiaosuse.picture.tudimension.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.HotRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.LocalImgAdapter;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.rxbus.DeleteEvent;
import com.lingxiaosuse.picture.tudimension.rxbus.RxBus;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class SeeDownLoadImgActivity extends BaseActivity {
    //存储每个目录下的图片路径,key是文件名
    private List<File> mFileList = new ArrayList<>();
    private List<String> mPicList = new ArrayList<>();
    @BindView(R.id.toolbar_download)
    Toolbar toolbar;
    @BindView(R.id.tv_download_null)
    TextView textNull;
    @BindView(R.id.recycle_download)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private LocalImgAdapter mAdapter;
    private Subscription rxSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_down_load_img);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("下载的图片");
        File file = new File(ContentValue.PATH);
        List<File> fileList = getFiles(file);
        if (null != fileList && fileList.size() != 0){
            textNull.setVisibility(View.GONE);
            for (int i = 0; i < fileList.size(); i++) {
                //Log.i("下载的图片路径", fileList.get(i).getAbsolutePath());
                String path = "file://"+fileList.get(i).getAbsolutePath();
                //String path = fileList.get(i).getAbsolutePath();
                mPicList.add(path);
            }
        }
        mLayoutManager = new GridLayoutManager(this,2,
                LinearLayoutManager.VERTICAL,false);
        mAdapter = new LocalImgAdapter(mPicList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new LocalImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(UIUtils.getContext(),LocalImgActivity.class);
                intent.putExtra("position",position);
                intent.putStringArrayListExtra("list", (ArrayList<String>) mPicList);
                startActivity(intent);
            }
        });

        initSubscription();
    }

    /**
     *订阅消息
     */
    private void initSubscription() {
        rxSubscription = RxBus.getDefault().tObservable(DeleteEvent.class)
                .subscribe(new Action1<DeleteEvent>() {
                    @Override
                    public void call(DeleteEvent deleteEvent) {
                        //刷新数据
                        mPicList.remove(deleteEvent.getPosition());
                        mAdapter.notifyDataSetChanged();
                        Log.i("code", "接收到删除数据了: "+deleteEvent.getPosition());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.show(throwable.toString());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private List<File> getFiles(File file){
        try {
            File[] fileList = file.listFiles();
            for (File f:fileList) {
                if (f.isFile()){
                    mFileList.add(f);
                }else {
                    getFiles(f);
                }
            }
            return mFileList;
        }catch (NullPointerException e){
            //ToastUtils.show("出错了："+e.getMessage());
            Log.i("seedownloadimgact", "出错了");
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (!rxSubscription.isUnsubscribed()){
            rxSubscription.unsubscribe();
        }
    }
}

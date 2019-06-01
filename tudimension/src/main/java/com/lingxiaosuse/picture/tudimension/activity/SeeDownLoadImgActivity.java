package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.rxbus.RxBus;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.LocalImgAdapter;
import com.lingxiaosuse.picture.tudimension.rxbusevent.DeleteEvent;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.BezierRefreshLayout;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rx.Subscription;

public class SeeDownLoadImgActivity extends BaseActivity {
    @BindView(R.id.swip_download)
    BezierRefreshLayout bezierRefreshLayout;
    //存储每个目录下的图片路径,key是文件名
    private List<File> mFileList = new ArrayList<>();
    private List<String> mPicList = new ArrayList<>();
    @BindView(R.id.toolbar_title)
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private SmartSkinRefreshLayout refreshLayout;
    private LocalImgAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_see_down_load_img;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbar);
        toolbar.setTitle("下载的图片");

        refreshLayout = bezierRefreshLayout.getRefreshLayout();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });
        mAdapter = new LocalImgAdapter(R.layout.local_img_item,mPicList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(), LocalImgActivity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("list", (ArrayList<String>) mPicList);
            startActivity(intent);
        });
        View view = View.inflate(this,R.layout.empty_data_layout,null);
        mAdapter.setEmptyView(view);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view1 -> new Animator[]{
                ObjectAnimator.ofFloat(view1, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view1, "scaleX", 0f, 1.05f, 1f)
        });
        mAdapter.isFirstOnly(false);

        recyclerView = bezierRefreshLayout.getRecyclerView();
        // 错列网格布局
        recyclerView.setHasFixedSize(true);      //设置固定大小
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        initSubscription();
        getLocalImages();
    }


    private void getLocalImages(){
        File file = new File(ContentValue.PATH);
        List<File> fileList = getFiles(file);
        if (null != fileList && fileList.size() != 0) {
            for (int i = 0; i < fileList.size(); i++) {
                //Log.i("下载的图片路径", fileList.get(i).getAbsolutePath());
                String path = "file://" + fileList.get(i).getAbsolutePath();
                //String path = fileList.get(i).getAbsolutePath();
                mAdapter.addData(path);
            }
        }
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    /**
     * 订阅消息
     */
    private void initSubscription() {
        Disposable regist = RxBus.getInstance().register(DeleteEvent.class, new Consumer<DeleteEvent>() {
            @Override
            public void accept(DeleteEvent deleteEvent) throws Exception {
                //刷新数据
                mPicList.remove(deleteEvent.getPosition());
                mAdapter.notifyDataSetChanged();
                Log.i("code", "接收到删除数据了: " + deleteEvent.getPosition());
            }
        });
        RxBus.getInstance().addSubscription(this,regist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_time_order_up:
                ToastUtils.show("点击了第一个");
                timeOrder(mFileList, true);
                item.setChecked(true);
                break;
            case R.id.menu_size_order_up:
                sizeOrder(mFileList);
                break;
           /* case R.id.menu_size_order_down:
                sizeOrder(mFileList, false);
                break;*/
            case R.id.menu_time_order_down:
                timeOrder(mFileList, false);
                break;
        }
        item.setChecked(true);
        return true;
    }




    private void sizeOrder(final List<File> fileList) {
        refreshLayout.autoRefresh();
        new Thread(() -> {
            try {
                File file1, file2, file;
                long size1, size2;
                for (int i = 0; i < fileList.size() - 1; i++) {
                    for (int j = 0; j < fileList.size() - 1 - i; j++) {
                        file1 = fileList.get(j);
                        file2 = fileList.get(j + 1);
                        size1 = getFileSize(file1);
                        size2 = getFileSize(file2);

                        if (size1 > size2) {
                            file = file1;
                            fileList.set(j, file2);
                            fileList.set(j + 1, file);
                        }
                    }
                }
                mPicList.clear();
                for (int i = 0; i < fileList.size(); i++) {
                    //Log.i("下载的图片路径", fileList.get(i).getAbsolutePath());
                    String path = "file://" + fileList.get(i).getAbsolutePath();
                    //String path = fileList.get(i).getAbsolutePath();
                    mPicList.add(path);
                }

                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void timeOrder(final List<File> fileList, final boolean isUp) {
        refreshLayout.autoRefresh();
        new Thread(() -> {
            try {
                File file1, file2, file;
                Date date1, date2;
                for (int i = 0; i < fileList.size() - 1; i++) {
                    for (int j = 0; j < fileList.size() - 1 - i; j++) {
                        file1 = fileList.get(j);
                        file2 = fileList.get(j + 1);
                        date1 = StringUtils.longToDate(file1.lastModified(), "yyyy-MM-dd HH:mm:ss");
                        date2 = StringUtils.longToDate(file2.lastModified(), "yyyy-MM-dd HH:mm:ss");
                        if (date1.before(date2)) {
                            if (isUp) {
                                file = fileList.get(j + 1);
                                fileList.set(j, file1);
                                fileList.set(j + 1, file);
                            } else {
                                file = fileList.get(j);
                                fileList.set(j, file2);
                                fileList.set(j + 1, file);
                            }
                        }
                    }
                }
                mPicList.clear();
                for (int i = 0; i < fileList.size(); i++) {
                    //Log.i("下载的图片路径", fileList.get(i).getAbsolutePath());
                    String path = "file://" + fileList.get(i).getAbsolutePath();
                    //String path = fileList.get(i).getAbsolutePath();
                    mPicList.add(path);
                }

                UIUtils.runOnUIThread(() -> {
                    mAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }
        return size;
    }

    private List<File> getFiles(File file) {
        try {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if (f.isFile()) {
                    mFileList.add(f);
                } else {
                    getFiles(f);
                }
            }
            return mFileList;
        } catch (NullPointerException e) {
            //ToastUtils.show("出错了："+e.getMessage());
            Log.i("seedownloadimgact", "出错了");
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.see_local_img, menu);
        return true;
    }
}

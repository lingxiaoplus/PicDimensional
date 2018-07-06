package com.lingxiaosuse.picture.tudimension;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ActivityController;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.widget.SkinTabLayout;
import com.lingxiaosuse.picture.tudimension.activity.AboutActivity;
import com.lingxiaosuse.picture.tudimension.activity.MzituActivity;
import com.lingxiaosuse.picture.tudimension.activity.SearchActivity;
import com.lingxiaosuse.picture.tudimension.activity.SeeDownLoadImgActivity;
import com.lingxiaosuse.picture.tudimension.activity.SettingsActivity;
import com.lingxiaosuse.picture.tudimension.activity.VerticalActivity;
import com.lingxiaosuse.picture.tudimension.activity.WebActivity;
import com.lingxiaosuse.picture.tudimension.activity.sousiba.SousibaActivity;
import com.lingxiaosuse.picture.tudimension.fragment.FragmentFactory;
import com.lingxiaosuse.picture.tudimension.modle.FileUploadModle;
import com.lingxiaosuse.picture.tudimension.presenter.MainPresenter;
import com.lingxiaosuse.picture.tudimension.receiver.NetworkReceiver;
import com.lingxiaosuse.picture.tudimension.retrofit.FileUploadInterface;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.MainView;

import java.io.File;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements MainView{
    @BindView(R.id.tab_main)
    SkinTabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.dl_menu)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_main)
    NavigationView navigationView;

    private String[] mTabStr;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                //默认是自动更新的
                boolean isCheck = SpUtils
                        .getBoolean(UIUtils.getContext(), ContentValue.IS_CHECK, true);
                if (isCheck) {
                    checkUpdate();
                }
            }
        }
    };
    private NetworkReceiver mNetworkChangeListener;
    private ActionBarDrawerToggle mDrawerToggle;
    private FileUploadInterface fileUploadInterface;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private ProgressDialog uploadDialog;
    private TextView hitokoto;
    private SimpleDraweeView simpleDraweeView;
    private MainPresenter mPresenter = new MainPresenter(this,this);
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        initView();
        //初始化headlayout
        initHeadLayout();
        mNetworkChangeListener = new NetworkReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetworkChangeListener, filter);
    }

    private void initHeadLayout() {
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header);
        hitokoto = headerLayout.findViewById(R.id.tv_hitokoto);
        simpleDraweeView = headerLayout.findViewById(R.id.image_head_background);
        SimpleDraweeView headImage = headerLayout.findViewById(R.id.image_head);
        headImage.setVisibility(View.GONE);
        mPresenter.getHeadImg();
        mPresenter.getHeadText();
    }

    private void initView() {
        mTabStr = getResources().getStringArray(R.array.tab_string_array);
        for (int i = 0; i < mTabStr.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTabStr[i]));
        }
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //设置viewpager缓存页面个数
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听  侧滑菜单
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                startPropertyAnim(simpleDraweeView,1f,2f,1f,10000);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置默认选中为首页
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        StartActivity(MainActivity.class, false);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_vertical:
                        StartActivity(VerticalActivity.class, false);
                        break;
                    /*case R.id.nav_figure:
                        ToastUtils.show("斗图");
                        break;*/
                    case R.id.nav_find:
                        showSelectDia();
                        break;
                    case R.id.nav_reception:
                        goToMarket(MainActivity.this, getPackageName());
                        break;
                    case R.id.nav_exit:
                        ActivityController.finishAll();
                        break;
                    case R.id.nav_mzitu:
                        //mzitu 爬虫
                        StartActivity(MzituActivity.class,false);
                        break;
                    case R.id.nav_sousiba:

                        StartActivity(SousibaActivity.class,false);
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers(); //关闭菜单
                return true;
            }
        });
    }

    @Override
    public void onGetHeadBackGround(Uri uri) {
        simpleDraweeView.setImageURI(uri);
    }

    @Override
    public void onGetHeadText(String result) {
        hitokoto.setText(result);
    }

    private class MainPageAdapter extends FragmentPagerAdapter {
        public MainPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabStr.length;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabStr[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                StartActivity(SettingsActivity.class, false);
                break;
            case R.id.menu_about:
                StartActivity(AboutActivity.class, false);
                break;
            case R.id.menu_download:
                StartActivity(SeeDownLoadImgActivity.class, false);
                break;
            case R.id.menu_search:
                StartActivity(SearchActivity.class, false);
                break;
        }
        return true;
    }

    private long preTime = 0;
    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - preTime > 2000) {
            ToastUtils.show("再按一次退出软件");
            preTime = nowTime;
        } else {
            ActivityController.finishAll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除网络监听广播
        if (mNetworkChangeListener != null) {
            unregisterReceiver(mNetworkChangeListener);
        }
        mHandler.removeMessages(2);
    }
    /**
     * 对话框选择搜图接口
     */
    private void showSelectDia() {
        final String[] items = getResources().getStringArray(R.array.detect_array);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.detect_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext()
                        , WebActivity.class);
                if (i == 0) {
                    //调用相册
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("title",items[i]);
                    startActivityForResult(intent, IMAGE);
                } else if (i == 1) {
                    intent.putExtra("url", ContentValue.SOUGOU_URL);
                    intent.putExtra("title",items[i]);
                    startActivity(intent);
                } else {
                    intent.putExtra("url", ContentValue.GOOGLE_URL);
                    intent.putExtra("title",items[i]);
                    ToastUtils.show("请自备梯子");
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == MainActivity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showUploadDialog();
            uploadFile(imagePath);
            c.close();
        }
    }

    private void showUploadDialog() {
        uploadDialog = new ProgressDialog(this);
        uploadDialog.setTitle(getResources().getString(R.string.app_name));
        uploadDialog.setMessage("正在上传");
        uploadDialog.show();
    }

    private void uploadFile(String filePath) {
        String url = "http://shitu.baidu.com";
        if (fileUploadInterface == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            fileUploadInterface = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build().create(FileUploadInterface.class);
        }
        //构建要上传的文件
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);

        MultipartBody.Part body =
                MultipartBody
                        .Part
                        .createFormData("upload",
                                file.getName(), requestFile);

        String descriptionStr = "upload";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionStr);
        Call<FileUploadModle> call =
                fileUploadInterface.fileModle(description, body);
        call.enqueue(new Callback<FileUploadModle>() {
            @Override
            public void onResponse(Call<FileUploadModle> call, Response<FileUploadModle> response) {
                String url = ContentValue.BAIDU_URL + response.body().getUrl();
                String querySign = response.body().getQuerySign();
                uploadDialog.dismiss();
                Intent intent = new Intent(getApplicationContext()
                        , WebActivity.class);
                intent.putExtra("url", url + "&" + querySign);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<FileUploadModle> call, Throwable t) {

            }
        });
    }

    /**
     * @param packageName 目标应用的包名
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    // 动画实际执行
    private void startPropertyAnim(View view,float oldValue1,float nowValue,float oldValue2,long time) {
        AnimatorSet set = new AnimatorSet();
        // 将一个TextView沿垂直方向先从原大小（1f）放大到5倍大小（5f），然后再变回原大小。
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", oldValue1, nowValue,oldValue2);
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", oldValue1, nowValue,oldValue2);
        set.play(animX).with(animY);
        set.setDuration(time);
        set.start();
    }
}

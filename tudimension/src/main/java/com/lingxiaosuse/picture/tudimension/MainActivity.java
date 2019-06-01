package com.lingxiaosuse.picture.tudimension;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.camera.lingxiao.common.rxbus.RxBus;
import com.camera.lingxiao.common.app.ActivityController;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.PopwindowUtil;
import com.camera.lingxiao.common.utills.SpUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.activity.LoginActivity;
import com.lingxiaosuse.picture.tudimension.activity.UserInfoActivity;
import com.lingxiaosuse.picture.tudimension.activity.cosplay.CosplayLaActivity;
import com.lingxiaosuse.picture.tudimension.activity.tuwan.TuWanActivity;
import com.lingxiaosuse.picture.tudimension.db.UserModel;
import com.lingxiaosuse.picture.tudimension.rxbusevent.DrawerChangeEvent;
import com.lingxiaosuse.picture.tudimension.service.DownloadService;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
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
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.MainView;
import com.liuguangqiang.cookie.CookieBar;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
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
            }else if (msg.what == 3){
                int color = SpUtils.getInt(UIUtils.getContext(),
                        ContentValue.SKIN_ID,R.color.colorPrimary);
                new CookieBar.Builder(MainActivity.this)
                        .setTitle("注意")
                        .setMessage("当前是移动网络！")
                        .setBackgroundColor(color)
                        .show();
            }
        }
    };
    private Runnable mLoadingRunnable = () -> {
        //初始化leancloud的sdk  只能在主线程初始化
        // 初始化参数依次为 this, AppId, AppKey
        // 初始化参数依次为 this, AppId, AppKey


        AVOSCloud.initialize(this,
                "LIAOQVeY4ChL2XLqKkRRNIHC-gzGzoHsz",
                "hYedbdnzfufqTy2iCz0uECkc");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可     开启调试日志
        //AVOSCloud.setDebugLogEnabled(true);

    };
    private Handler mInitHandler = new Handler();
    private NetworkReceiver mNetworkChangeListener;
    private ActionBarDrawerToggle mDrawerToggle;
    private FileUploadInterface fileUploadInterface;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private ProgressDialog uploadDialog;
    private TextView hitokoto;
    private SimpleDraweeView simpleDraweeView;
    private ServiceConnection mConnect;
    public DownloadService mDownloadService;
    private String mHeadImageUrl = "";
    private MainPresenter mPresenter = new MainPresenter(this,this);
    private String[] mPermessions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

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
        bindDownloadService();
        //权限检测
        if (!EasyPermissions.hasPermissions(this,mPermessions)){
            //没有权限就申请
            EasyPermissions.requestPermissions(this, getString(R.string.permession_title),
                    ContentValue.PERMESSION_REQUEST_CODE, mPermessions);
        }

        //延迟加载 提升速度
        getWindow().getDecorView().post(() -> mInitHandler.post(mLoadingRunnable));
    }

    private void initHeadLayout() {
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header);
        hitokoto = headerLayout.findViewById(R.id.tv_hitokoto);
        simpleDraweeView = headerLayout.findViewById(R.id.image_head_background);
        ImageView headImage = headerLayout.findViewById(R.id.image_head);
        TextView userName = headerLayout.findViewById(R.id.tv_username);

        headImage.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        final UserModel model = SQLite.select()
                .from(UserModel.class)
                .querySingle();
        if (model != null){
            userName.setText(model.getUsername());
        }
        //headImage.setVisibility(View.GONE);
        headImage.setOnClickListener(view -> {
            if (model != null) {
                // 跳转到首页
                StartActivity(UserInfoActivity.class,false);
            } else {
                //缓存用户对象为空时，可打开用户注册界面…
                StartActivity(LoginActivity.class,false);
            }
        });
        mPresenter.getHeadImg();
        mPresenter.getHeadText();

        simpleDraweeView.setOnLongClickListener(v -> {
            final PopwindowUtil popwindowUtil = new PopwindowUtil
                    .PopupWindowBuilder(getApplicationContext())
                    .setView(R.layout.pop_long_click)
                    .setFocusable(true)
                    .setTouchable(true)
                    .setOutsideTouchable(true)
                    .create();
            //popwindowUtil.showAtLocation(simpleDraweeView);
            popwindowUtil.showAsDropDown(simpleDraweeView,0,
                    -simpleDraweeView.getHeight()/2);
            popwindowUtil.getView(R.id.pop_download).setOnClickListener(v12 -> {
                if (mDownloadService != null && mHeadImageUrl != null){
                    ToastUtils.show("正在下载");
                    mDownloadService.startDownload(mHeadImageUrl);
                }
                popwindowUtil.dissmiss();
            });
            popwindowUtil.getView(R.id.pop_cancel).setOnClickListener(v1 -> popwindowUtil.dissmiss());
            return true;
        });
    }

    private void initView() {
        initSubscription();
        mTabStr = getResources().getStringArray(R.array.tab_string_array);
        for (int i = 0; i < mTabStr.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTabStr[i]));
        }
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //设置viewpager缓存页面个数
        viewPager.setOffscreenPageLimit(4);
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

        navigationView.setNavigationItemSelectedListener(item -> {
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
                case R.id.nav_cosplayla:
                    StartActivity(CosplayLaActivity.class,false);
                    break;
                case R.id.nav_tuwan:
                    Intent tuwanIt = new Intent(MainActivity.this,TuWanActivity.class);
                    tuwanIt.putExtra("table_name","tuwan_main");
                    startActivity(tuwanIt);
                    break;
                case R.id.nav_leshe:
                    Intent lesheIt = new Intent(MainActivity.this,TuWanActivity.class);
                    lesheIt.putExtra("table_name","leshe_cover");
                    startActivity(lesheIt);
                    break;
                default:
                    break;
            }
            mDrawerLayout.closeDrawers(); //关闭菜单
            return true;
        });
    }

    private void setVisibility(int pos,boolean vis){
        Menu menu = navigationView.getMenu();
        switch (pos){
            case 0:
                menu.findItem(R.id.nav_home).setVisible(vis);
                break;
            case 1:
                menu.findItem(R.id.nav_vertical).setVisible(vis);
                break;
            case 2:
                menu.findItem(R.id.nav_mzitu).setVisible(vis);
                break;
            case 3:
                menu.findItem(R.id.nav_leshe).setVisible(vis);
                break;
            case 4:
                menu.findItem(R.id.nav_tuwan).setVisible(vis);
                break;
            case 5:
                menu.findItem(R.id.nav_cosplayla).setVisible(vis);
                break;
            case 6:
                menu.findItem(R.id.nav_find).setVisible(vis);
                break;
            case 7:
                menu.findItem(R.id.nav_reception).setVisible(vis);
                break;
            case 8:
                menu.findItem(R.id.nav_exit).setVisible(vis);
                break;
        }
    }
    @Override
    public void onGetHeadBackGround(Uri uri) {
        simpleDraweeView.setImageURI(uri);
        mHeadImageUrl = uri.toString();
    }

    @Override
    public void onGetHeadText(String result) {
        hitokoto.setText(result);
    }


    private void bindDownloadService() {
        mConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                mDownloadService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mDownloadService = null;
            }
        };
        Intent intent = new Intent(this, DownloadService.class);
        //最后一个参数，是否自动创建service 这个是自动创建
        bindService(intent, mConnect, Service.BIND_AUTO_CREATE);
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
            case R.id.menu_feedback:
                FeedbackAgent agent = new FeedbackAgent(getApplicationContext());
                agent.startDefaultThreadActivity();
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
        if (mConnect != null){
            unbindService(mConnect);
        }
        mHandler.removeMessages(2);
        RxBus.getInstance().unSubscribe(this);
        FragmentFactory.clearFragments();
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
    /**
     * 订阅侧滑菜单修改消息
     */
    private void initSubscription() {
        Disposable regist = RxBus.getInstance().register(DrawerChangeEvent.class, drawerChangeEvent -> {

            for (int j = 0; j < getResources().getStringArray(R.array.drawer_string).length; j++) {
                setVisibility(j,true);
            }
            String val = drawerChangeEvent.getPositions();
            String[] posStr = val.split(",");
            for (int i = 0; i < posStr.length; i++) {
                if (StringUtils.isNumeric(posStr[i]) && !posStr[i].isEmpty()){
                    int pos = Integer.valueOf(posStr[i]);
                    setVisibility(pos,false);
                }
            }
        });
        RxBus.getInstance().addSubscription(this,regist);
    }

}

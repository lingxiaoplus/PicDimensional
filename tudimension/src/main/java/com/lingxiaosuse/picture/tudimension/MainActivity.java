package com.lingxiaosuse.picture.tudimension;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.lingxiaosuse.picture.tudimension.activity.AboutActivity;
import com.lingxiaosuse.picture.tudimension.activity.ActivityController;
import com.lingxiaosuse.picture.tudimension.activity.BaseActivity;
import com.lingxiaosuse.picture.tudimension.activity.SearchActivity;
import com.lingxiaosuse.picture.tudimension.activity.SeeDownLoadImgActivity;
import com.lingxiaosuse.picture.tudimension.activity.SettingsActivity;
import com.lingxiaosuse.picture.tudimension.activity.VerticalActivity;
import com.lingxiaosuse.picture.tudimension.activity.WebActivity;
import com.lingxiaosuse.picture.tudimension.fragment.BaseFragment;
import com.lingxiaosuse.picture.tudimension.fragment.FragmentFactory;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.FileUploadModle;
import com.lingxiaosuse.picture.tudimension.receiver.NetworkReceiver;
import com.lingxiaosuse.picture.tudimension.retrofit.FileUploadInterface;
import com.lingxiaosuse.picture.tudimension.utils.PremessionUtils;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.WaveLoading;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.fab_main)
    FloatingActionButton faButton;
    @BindView(R.id.dl_menu)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_main)
    NavigationView navigationView;
    @BindView(R.id.pb_menu)
    WaveLoading pbMenu;
    private String[] tabStr = new String[]{"推荐", "分类", "最新", "专辑"};
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
    private View dialogView;
    private ActionBarDrawerToggle mDrawerToggle;
    private FileUploadInterface fileUploadInterface;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private ProgressDialog uploadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //tabLayout = (TabLayout) findViewById(R.id.tab_main);
        initView();
        PremessionUtils.getPremession(this, getString(R.string.permession_title),
                getString(R.string.permession_message),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }
                , new PremessionUtils.onPermissinoListener() {
                    @Override
                    public void onPermissionGet() {
                        ToastUtils.show("已获取权限");
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastUtils.show("获取权限失败");
                    }
                });

        mNetworkChangeListener = new NetworkReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetworkChangeListener, filter);
    }

    private void initView() {
        for (int i = 0; i < tabStr.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabStr[i]));
        }
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //设置viewpager缓存页面个数
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ultimateBar.setColorBarForDrawer
                        (ContextCompat.getColor(UIUtils.getContext(), R.color.colorPrimary),
                                100);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ultimateBar.setColorBar(ContextCompat.getColor(UIUtils.getContext(),
                        R.color.colorPrimary),
                        100);
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
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers(); //关闭菜单
                return true;
            }
        });
    }

    class MainPageAdapter extends FragmentPagerAdapter {
        public MainPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabStr.length;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabStr[position];
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PremessionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_support:
                showDialog();
                break;
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

    private void showDialog() {
        String[] items = {"支付宝", "微信"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("感谢各位大佬的捐赠~");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    ToastUtils.show("支付宝");
                    donateAlipay("FKX014647ZUX0IO5DJW109");
                } else {
                    ToastUtils.show("请从相册中选择第一张二维码");
                    donateWeixin();
                }
            }
        });
        builder.show();
    }

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *                payCode = stx00187oxldjvyo3ofaw60
     *                注：不区分大小写
     *                FKX014647ZUX0IO5DJW109
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
        }
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.ic_wechat_pay);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "lingxiao_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkChangeListener != null) {
            unregisterReceiver(mNetworkChangeListener);
        }
    }

    long preTime = 0;

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

    /**
     * 对话框选择搜图接口
     */
    private void showSelectDia() {
        String[] items = {"百度识图", "搜狗识图", "谷歌识图"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择搜图接口");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext()
                        , WebActivity.class);
                if (i == 0) {
                    //调用相册
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("title","百度识图");
                    startActivityForResult(intent, IMAGE);
                } else if (i == 1) {
                    intent.putExtra("url", "http://pic.sogou.com/");
                    intent.putExtra("title","搜狗识图");
                    startActivity(intent);
                } else {
                    intent.putExtra("url", "https://images.google.com/imghp?hl=zh-CN&gws_rd=ssl");
                    intent.putExtra("title","谷歌识图");
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
}

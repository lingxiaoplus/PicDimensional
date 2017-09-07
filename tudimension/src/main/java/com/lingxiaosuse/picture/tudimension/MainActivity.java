package com.lingxiaosuse.picture.tudimension;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lingxiaosuse.picture.tudimension.activity.AboutActivity;
import com.lingxiaosuse.picture.tudimension.activity.BaseActivity;
import com.lingxiaosuse.picture.tudimension.activity.SettingsActivity;
import com.lingxiaosuse.picture.tudimension.fragment.BaseFragment;
import com.lingxiaosuse.picture.tudimension.fragment.FragmentFactory;
import com.lingxiaosuse.picture.tudimension.utils.PremessionUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    private String[] tabStr = new String[]{"推荐","分类","最新","专辑"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //tabLayout = (TabLayout) findViewById(R.id.tab_main);
        initView();
        PremessionUtils.getPremession(this,getString(R.string.permession_title),
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
    }

    class MainPageAdapter extends FragmentPagerAdapter{
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
        PremessionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_support:
                showDialog();
                break;
            case R.id.menu_setting:
                StartActivity(SettingsActivity.class,false);
                break;
            case R.id.menu_about:
                StartActivity(AboutActivity.class,false);
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
                if (i == 0){
                    ToastUtils.show("支付宝");
                    donateAlipay("FKX014647ZUX0IO5DJW109");
                }else {
                    ToastUtils.show("请从相册中选择第一张二维码");
                    donateWeixin();
                }
            }
        });
        builder.show();
    }

    /**
     * 支付宝支付
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
}

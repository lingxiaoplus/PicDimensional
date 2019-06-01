package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.PopwindowUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;

import com.lingxiaosuse.picture.tudimension.adapter.CategoryAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.presenter.CategoryPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.CategoryView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryFragment extends BaseFragment implements CategoryView{
    @BindView(R.id.rv_category)
    RecyclerView recyclerView;
    @BindView(R.id.swip_category)
    SmartSkinRefreshLayout refreshLayout;

    private CategoryAdapter mCateAdapter;
    private GridLayoutManager mLayoutManager;
    private CategoryPresenter mCategoryPresenter;
    private List<CategoryModle.CategoryBean> categoryList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        refreshLayout.setOnRefreshListener((refreshLayout)->{
            categoryList.clear();
            mCategoryPresenter.getCategor();
        });

        mCateAdapter = new CategoryAdapter(R.layout.category_item,categoryList,false);
        recyclerView.setAdapter(mCateAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);

        mCateAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mCateAdapter.setOnItemClickListener((adapter, view, position) -> {
            try {
                List<CategoryModle.CategoryBean> categoryList = adapter.getData();
                Intent intent = new Intent(UIUtils.getContext(),
                        BannerDetailActivity.class);
                intent.putExtra("url",categoryList.get(position).getCover());
                intent.putExtra("desc",categoryList.get(position).getName());
                intent.putExtra("id",categoryList.get(position).getId());
                intent.putExtra("title",categoryList.get(position).getName());
                intent.putExtra("type", ContentValue.TYPE_CATEGORY);  //说明类型是轮播图
                startActivity(intent);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        });
        mCateAdapter.setOnItemLongClickListener((adapter, view, position) -> {

            final PopwindowUtil popwindowUtil = new PopwindowUtil
                    .PopupWindowBuilder(getActivity())
                    .setView(R.layout.pop_long_click)
                    .setFocusable(true)
                    .setTouchable(true)
                    .setOutsideTouchable(true)
                    .create();
            popwindowUtil.showAsDropDown(view,0,-view.getHeight());
            //popwindowUtil.showAtLocation(view);

            popwindowUtil.getView(R.id.pop_download).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity.mDownloadService != null){
                        ToastUtils.show("正在下载");
                        activity.mDownloadService.startDownload(categoryList.get(position).getCover());
                    }
                    popwindowUtil.dissmiss();
                }
            });
            popwindowUtil.getView(R.id.pop_cancel).setOnClickListener(v -> popwindowUtil.dissmiss());
            return true;
        });

    }

    @Override
    protected void onFirstVisiblity() {
        super.onFirstVisiblity();
        mCategoryPresenter = new CategoryPresenter(this,this);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onGetCategoryResult(CategoryModle modle) {
        mCateAdapter.addData(modle.getCategory());
        refreshLayout.finishRefresh();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {
        ToastUtils.show(text);
    }
}

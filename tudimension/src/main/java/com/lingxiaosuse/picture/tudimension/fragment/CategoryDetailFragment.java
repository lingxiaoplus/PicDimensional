package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryDetailModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryDetailInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-13.
 */

public class CategoryDetailFragment extends BaseFragment{

    @BindView(R.id.swip_vertical_item)
    SmartSkinRefreshLayout refreshLayout;
    @BindView(R.id.recycle_vertical_item)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_vertical_fragment)
    FloatingActionButton floatingActionButton;

    private int skip = 0;
    private FloatingActionButton faButton;
    private StaggeredGridLayoutManager manager;
    private String id;
    private List<CategoryDetailModle.ResBean.VerticalBean> mPicList = new ArrayList<>();
    private ArrayList<String> picIdList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private VerticalAdapter mAdapter;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_pager;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        manager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        refreshLayout.autoRefresh();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    floatingActionButton.hide();
                }else {
                    floatingActionButton.show();
                }
            }
        });
    }

    @Override
    protected void initData() {
        try {
            Bundle bundle = getArguments();
            final String order = bundle.getString("order");
            id = bundle.getString("id");
            mAdapter = new VerticalAdapter(R.layout.list_vertical,mPicList);
            mRecyclerView.setAdapter(mAdapter);
            refreshLayout.setOnRefreshListener(refreshLayout -> {
                getDataFromServer(id,30,0,order);
            });
            refreshLayout.setOnLoadMoreListener(refreshLayout -> {
                skip+=30;
                getDataFromServer(id,30,skip,order);
            });

            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                try {
                    Intent intent = new Intent(UIUtils.getContext(),
                            ImageLoadingActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("itemCount",mAdapter.getItemCount());
                    intent.putExtra("id",mPicList.get(position).getId());
                    intent.putExtra("isVertical",true);
                    intent.putStringArrayListExtra("picList",picUrlList);
                    intent.putStringArrayListExtra("picIdList",picIdList);
                    startActivity(intent);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            });
        }catch (NullPointerException e){
            Log.i("verticalFragment", "获取bundle数据失败");
        }
    }

    private void getDataFromServer(String id,int limit,int skip,String order){
        RetrofitHelper
                .getInstance(UIUtils.getContext())
                .getInterface(CategoryDetailInterface.class)
                .cateModle(id,limit,skip,order)
                .enqueue(new Callback<CategoryDetailModle>() {
                    @Override
                    public void onResponse(Call<CategoryDetailModle> call, Response<CategoryDetailModle> response) {
                        mAdapter.addData(response.body().getRes().getVertical());
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        picUrlList.clear();
                        picIdList.clear();
                        for (int i = 0; i < mPicList.size(); i++) {
                            picIdList.add(mPicList.get(i).getId());
                            //修复图片名字识别失败
                            picUrlList.add(mPicList.get(i).getWp());
                        }


                    }

                    @Override
                    public void onFailure(Call<CategoryDetailModle> call, Throwable t) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                });
    }


    public class VerticalAdapter extends BaseQuickAdapter<CategoryDetailModle.ResBean.VerticalBean,BaseViewHolder> {

        private SimpleDraweeView draweeView;
        private List<CategoryDetailModle.ResBean.VerticalBean> cateBeanList;//分类
        public VerticalAdapter(int layoutResId, @Nullable List<CategoryDetailModle.ResBean.VerticalBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, CategoryDetailModle.ResBean.VerticalBean item) {
            Uri uri = Uri.parse(item.getThumb());
            draweeView = (SimpleDraweeView) holder.getView(R.id.img_vertical_item);
            draweeView.setImageURI(uri);
        }
    }
}

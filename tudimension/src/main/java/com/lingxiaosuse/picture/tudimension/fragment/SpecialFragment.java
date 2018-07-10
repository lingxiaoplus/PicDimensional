package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.SpecialRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.presenter.SpecialPresneter;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.SpecialInterface;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SpecialView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class SpecialFragment extends BaseFragment implements SpecialView{
    @BindView(R.id.swip_special)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_special)
    RecyclerView recyclerView;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;

    private List<SpecialModle.AlbumBean> albumBeanList = new ArrayList<>();
    private List<SpecialModle.AlbumBean> moreList = new ArrayList<>();;
    private SpecialRecycleAdapter mAdapter;
    private LinearLayoutManager manager;
    private int skip = 0;
    private SpecialPresneter mPresenter;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_special;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                albumBeanList.clear();
                mPresenter.getSpecialResult(ContentValue.limit,0);
            }
        });
        floatingBtnToogle(recyclerView,fab);
        mAdapter = new SpecialRecycleAdapter(albumBeanList,0,1);
        manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setRefreshListener(new SpecialRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                mPresenter.getSpecialResult(ContentValue.limit,skip);
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        BannerDetailActivity.class);
                intent.putExtra("url",albumBeanList.get(position).getLcover());
                intent.putExtra("desc",albumBeanList.get(position).getName());
                intent.putExtra("id",albumBeanList.get(position).getId());
                intent.putExtra("title",albumBeanList.get(position).getName());
                intent.putExtra("type","album");  //说明类型位album
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {
        albumBeanList.clear();
        mPresenter = new SpecialPresneter(this,this);
        mPresenter.getSpecialResult(ContentValue.limit,0);
    }

    @OnClick(R.id.fab_fragment)
    public void setTopView(){
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onGetSpecialData(SpecialModle modle) {
        if (modle.getAlbum().size() < 30){
            mAdapter.isFinish(true);
        }
        albumBeanList.addAll(modle.getAlbum());
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
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

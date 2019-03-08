package com.lingxiaosuse.picture.tudimension.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.SpecialRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.presenter.SpecialPresneter;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SpecialView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

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
    SmartSkinRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_special)
    RecyclerView recyclerView;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;

    private List<SpecialModle.AlbumBean> albumBeanList = new ArrayList<>();
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
        swipeRefreshLayout.autoRefresh();
        swipeRefreshLayout.setOnRefreshListener((refreshLayout)-> {
            albumBeanList.clear();
            mPresenter.getSpecialResult(ContentValue.limit,0);
        });
        floatingBtnToogle(recyclerView,fab);
        mAdapter = new SpecialRecycleAdapter(R.layout.list_special,albumBeanList);
        manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            skip+=30;
            mPresenter.getSpecialResult(ContentValue.limit,skip);
        });
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(),
                    BannerDetailActivity.class);
            intent.putExtra("url",albumBeanList.get(position).getLcover());
            intent.putExtra("desc",albumBeanList.get(position).getName());
            intent.putExtra("id",albumBeanList.get(position).getId());
            intent.putExtra("title",albumBeanList.get(position).getName());
            intent.putExtra("type","album");  //说明类型位album
            startActivity(intent);
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
            mAdapter.loadMoreEnd();
        }
        mAdapter.addData(modle.getAlbum());
        swipeRefreshLayout.finishRefresh();
        swipeRefreshLayout.finishLoadMore();
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

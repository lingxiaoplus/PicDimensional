package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.SearchRecyAdapter;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;
import com.lingxiaosuse.picture.tudimension.presenter.SearchPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class SearchActivity extends BaseActivity implements com.lingxiaosuse.picture.tudimension.view.SearchView{
    /*@BindView(R.id.searchview)
    SearchView searchView;*/
    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindViews({R.id.tv_search1,R.id.tv_search2,
            R.id.tv_search3,R.id.tv_search4,
            R.id.tv_search5,R.id.tv_search6,
            R.id.tv_search7,R.id.tv_search8})
    List<TextView> textViewList;
    @BindView(R.id.recycle_search)
    RecyclerView recyclerView;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.swip_comment)
    SmartSkinRefreshLayout refreshLayout;
    private List<String> keyWords = new ArrayList<>();
    private String keyword;

    //记录是第几次搜索
    private int index = 0;
    //记录页数
    private int page = 0;
    //保存返回结果
    private List<SearchResultModle.WallPaper> wallPaperList = new ArrayList<>();
    private List<SearchResultModle.SearchBean> SearchList = new ArrayList<>();
    private GridLayoutManager mLayoutManager;

    private ArrayList<String> picUrlList = new ArrayList<>();
    private ArrayList<String> IdList = new ArrayList<>();
    private SearchRecyAdapter mAdapter;
    private SearchPresenter mPresenter = new SearchPresenter(this,this);
    private SearchView mSearchView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        refreshLayout.autoRefresh();
        setToolbarBack(toolbar);

        // show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //从服务器上获取关键字
        mPresenter.getSearchKey();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (cardView.getVisibility() == View.VISIBLE){
                mPresenter.getSearchKey();
            }else {
                wallPaperList.clear();
                mPresenter.getSearchWallResult(keyword,0);
            }
        });

    }

    /**
     * 初始化searchview
     */
    private void initSearchView(SearchView searchView) {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //隐藏软键盘
                toggleSoftInput(toolbar,0,false);
                //cardView.setVisibility(View.GONE);
                startPropertyAnim(cardView,1f,0f,500);
                keyword = query;
                wallPaperList.clear();
                refreshLayout.autoRefresh();
                mPresenter.getSearchWallResult(keyword,0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    ToastUtils.show(newText);
                    keyword = newText;

                }else {

                }
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (null != recyclerView){
            recyclerView.setFocusable(true);
            recyclerView.setFocusableInTouchMode(true);
            recyclerView.requestFocus();

        }
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < textViewList.size(); i++) {
            final int finalI = i;
            textViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    keyword = textViewList.get(finalI).getText().toString();
                    mPresenter.getSearchWallResult(keyword,0);
                    //cardView.setVisibility(View.GONE);
                    startPropertyAnim(cardView,1f,0f,500);
                    //隐藏软键盘
                    toggleSoftInput(toolbar,0,false);
                }
            });
        }

        mAdapter = new SearchRecyAdapter(wallPaperList,0,1);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                page+=30;
                mPresenter.getSearchWallResult(keyword,page);
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                if (null == picUrlList){
                    return;
                }
                picUrlList.clear();
                IdList.clear();
                for (int i = 0; i < wallPaperList.size(); i++) {
                    if (picUrlList != null){
                        picUrlList.add(wallPaperList.get(i).getImg());
                    }
                    IdList.add(wallPaperList.get(i).getId());
                }
                try {
                    Intent intent = new Intent(UIUtils.getContext(),
                            ImageLoadingActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("itemCount",mAdapter.getItemCount());
                    intent.putExtra("id",wallPaperList.get(position).getId());
                    intent.putStringArrayListExtra("picList",picUrlList);
                    intent.putStringArrayListExtra("picIdList",IdList);
                    intent.putExtra("isHot",false); // 判断是否为最新界面传递过来的
                    startActivity(intent);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    /**
     * @param windowToken View，一般为edittext  getWindowToken()
     * @param flag 软键盘隐藏时的控制参数  一般为0即可
     * @param isShow 显示还是隐藏
     */
    public void toggleSoftInput(View windowToken, int flag, boolean isShow){
        //隐藏软键盘
        InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != input){
            if (isShow){
                windowToken.requestFocus();
                input.showSoftInput(windowToken,0);
            }else {
                input.hideSoftInputFromWindow(windowToken.getWindowToken(),flag);
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);
        MenuItem item = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        initSearchView(mSearchView);
        return true;
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

    @Override
    public void onGetSearchResult(SearchResultModle modle) {
        if (modle.getSearch().size() < 5){
            mAdapter.isFinish(true);
            ToastUtils.show("共有"+modle.getSearch().size());
        }
        //SearchList.addAll(modle.getSearch());
        for (int i = 0; i < modle.getSearch().size(); i++) {
            wallPaperList.addAll(modle.getSearch().get(i).getItems());
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onGetSearchKeyWord(SearchKeyword keyword) {
        try{
            keyWords = keyword
                    .getKeyword()
                    .get(0)
                    .getItems();
            for (int i = 0; i < textViewList.size(); i++) {
                textViewList.get(i).setText(keyWords.get(i));
            }
            refreshLayout.finishRefresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // 动画实际执行
    private void startPropertyAnim(View view,float oldValue,float nowValue,long time) {
        AnimatorSet set = new AnimatorSet();
        // 将一个TextView沿垂直方向先从原大小（1f）放大到5倍大小（5f），然后再变回原大小。
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", oldValue, nowValue);
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", oldValue, nowValue);
        set.play(animX).with(animY);
        set.setDuration(time);
        set.start();
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

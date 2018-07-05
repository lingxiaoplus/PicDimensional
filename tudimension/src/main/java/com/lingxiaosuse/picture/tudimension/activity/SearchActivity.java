package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;
import com.lingxiaosuse.picture.tudimension.presenter.SearchPresenter;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.SearchKeyInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.SearchKeyResultInterface;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.WaveLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    SwipeRefreshLayout refreshLayout;
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
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        refreshLayout.setRefreshing(true);
        setSwipeColor(refreshLayout);
        setToolbarBack(toolbar);

        // show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //从服务器上获取关键字
        mPresenter.getSearchKey();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cardView.getVisibility() == View.VISIBLE){
                    mPresenter.getSearchKey();
                }else {
                    wallPaperList.clear();
                    mPresenter.getSearchWallResult(keyword,0);
                }
            }
        });
    }

    /**
     * 初始化searchview
     */
    private void initSearchView(SearchView searchView) {
        searchView.clearFocus();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //隐藏软键盘
                toggleSoftInput(toolbar,0,false);
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
    protected void initData() {
        super.initData();
        for (int i = 0; i < textViewList.size(); i++) {
            final int finalI = i;
            textViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    keyword = textViewList.get(finalI).getText().toString();
                    mPresenter.getSearchWallResult(keyword,0);
                    cardView.setVisibility(View.GONE);
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
        //recycle的监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView
                        .getLayoutManager()
                        .getChildAt(recyclerView
                                .getLayoutManager()
                                .getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView
                        .getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView
                        .getLayoutManager()
                        .getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom &&
                        lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    page+=30;
                    mPresenter.getSearchWallResult(keyword,page);
                }
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
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mAdapter.getItemCount());
                intent.putExtra("id",wallPaperList.get(position).getId());
                intent.putStringArrayListExtra("picList",picUrlList);
                intent.putStringArrayListExtra("picIdList",IdList);
                intent.putExtra("isHot",false); // 判断是否为最新界面传递过来的
                startActivity(intent);
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
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
        refreshLayout.setRefreshing(false);
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
            refreshLayout.setRefreshing(false);
        }catch (Exception e){
            e.printStackTrace();
        }
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

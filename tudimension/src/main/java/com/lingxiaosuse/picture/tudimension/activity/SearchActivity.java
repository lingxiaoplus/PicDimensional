package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.SearchRecyAdapter;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.SearchKeyInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.SearchKeyResultInterface;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.WaveLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SearchActivity extends BaseActivity {
    @BindView(R.id.searchview)
    SearchView searchView;
    @BindView(R.id.toolbar_search)
    Toolbar toolbar;
    @BindView(R.id.image_search)
    ImageView imageSearch;
    @BindViews({R.id.tv_search1,R.id.tv_search2,
            R.id.tv_search3,R.id.tv_search4,
            R.id.tv_search5,R.id.tv_search6,
            R.id.tv_search7,R.id.tv_search8})
    List<TextView> textViewList;
    @BindView(R.id.recycle_search)
    RecyclerView recyclerView;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.wave_search)
    WaveLoading waveLoading;
    private List<String> keyWords = new ArrayList<>();
    private String keyword;

    //记录是第几次搜索
    private int index = 0;
    //记录页数
    private int page = 0;
    //保存返回结果
    private List<SearchResultModle.ResBean.WallPaper> wallPaperList = new ArrayList<>();
    private List<SearchResultModle.ResBean.WallPaper> oldList;
    private List<SearchResultModle.ResBean.SearchBean> SearchList = new ArrayList<>();
    private GridLayoutManager mLayoutManager;

    private ArrayList<String> picUrlList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waveLoading.setVisibility(View.GONE);
        }
    };
    private SearchRecyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        //从服务器上获取关键字
        getKeyFromServer();

        for (int i = 0; i < textViewList.size(); i++) {
            final int finalI = i;
            textViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    keyword = textViewList.get(finalI).getText().toString();
                    getKeyData(keyword,0);
                    cardView.setVisibility(View.GONE);
                    //隐藏软键盘
                    InputMethodManager input = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(SearchActivity.this
                            .getCurrentFocus().getWindowToken(),0);
                }
            });
        }

        mAdapter = new SearchRecyAdapter(wallPaperList);
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
                    page++;
                    if (page<55){
                        getKeyData(keyword,page);
                    }else {
                        ToastUtils.show("没有数据了");
                    }
                }
            }
        });
    }
    /**
     *根据关键字从服务器上获取信息
     * @param keyword 关键字
     * @param page 页数
     */
    private void getKeyData(String keyword, final int page) {
        waveLoading.setVisibility(View.VISIBLE);
        final long preTime = System.currentTimeMillis();
        RetrofitHelper
                .getInstance(this)
                .getInterface(SearchKeyResultInterface.class)
                .searchResult(ContentValue.SEARCH_URL+"/v1/search/all/resource/"+keyword+"?version=181&channel=huawei&skip="+page+"&adult=false")
                .enqueue(new Callback<SearchResultModle>() {
                    @Override
                    public void onResponse(Call<SearchResultModle> call, Response<SearchResultModle> response) {
                        SearchList = response.body().getRes().getSearch();
                        try {
                            //if (index>0){
                                //oldList.clear();
                            //}
                            for (int i = 0; i < SearchList.size(); i++) {
                                oldList = SearchList.get(i).getItems();
                                wallPaperList.addAll(SearchList.get(i).getItems());

                            }
                            for (int i = 0; i < wallPaperList.size(); i++) {
                                Log.i("SearchActivity", "图片地址: "
                                        +wallPaperList.get(i).getImg());
                            }
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                            Log.i("SearchActivity", "wallPaperList: "+wallPaperList.size()
                                    +"oldList"+oldList.size()
                                    +"SearchList"+SearchList.size()
                                    +"页数："+page);
                            mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View View, int position) {
                                    if (null == picUrlList){
                                        return;
                                    }
                                    picUrlList.clear();
                                    for (int i = 0; i < wallPaperList.size(); i++) {
                                        if (picUrlList != null){
                                            picUrlList.add(wallPaperList.get(i).getImg());
                                        }
                                    }
                                    Intent intent = new Intent(UIUtils.getContext(),
                                            ImageLoadingActivity.class);
                                    intent.putExtra("position",position);
                                    intent.putExtra("itemCount",mAdapter.getItemCount());
                                    intent.putExtra("id",wallPaperList.get(position).getId());
                                    intent.putStringArrayListExtra("picList",picUrlList);
                                    intent.putExtra("isHot",false); // 判断是否为最新界面传递过来的
                                    startActivity(intent);
                                }
                            });
                            index++;
                            final long nowTime = System.currentTimeMillis();
                            if ((nowTime - preTime) < 2000){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000-(nowTime - preTime));
                                            mHandler.sendEmptyMessage(0);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }else {
                                waveLoading.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResultModle> call, Throwable t) {

                    }
                });
    }

    private void getKeyFromServer() {
        RetrofitHelper.getInstance(this)
                .getInterface(SearchKeyInterface.class)
                .searchModle()
                .enqueue(new Callback<SearchKeyword>() {
                    @Override
                    public void onResponse(Call<SearchKeyword> call, Response<SearchKeyword> response) {
                        try{
                            keyWords = response.body()
                                    .getRes()
                                    .getKeyword()
                                    .get(0)
                                    .getItems();
                            for (int i = 0; i < textViewList.size(); i++) {
                                textViewList.get(i).setText(keyWords.get(i));
                            }
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchKeyword> call, Throwable t) {

                    }
                });
    }

    private void initView() {
        searchView.setIconifiedByDefault(false);
        setToolbarBack(toolbar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //隐藏软键盘
                InputMethodManager input = (InputMethodManager)
                        getSystemService(INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(SearchActivity.this
                        .getCurrentFocus().getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    ToastUtils.show(newText);
                    keyword = newText;
                    imageSearch.setVisibility(View.VISIBLE);
                }else {
                    imageSearch.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        // show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
    @OnClick(R.id.image_search)
    public void onSearchClick(){
        //隐藏软键盘
        InputMethodManager input = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(SearchActivity.this
                .getCurrentFocus().getWindowToken(),0);
        getKeyData(keyword,0);
        cardView.setVisibility(View.GONE);
    }
}

package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.SearchKeyInterface;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

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
            R.id.tv_search7,R.id.tv_search8,})
    List<TextView> textViewList;
    private List<String> keyWords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        //从服务器上获取关键字
        getKeyFromServer();
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
                ToastUtils.show("查询");

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
    }
}

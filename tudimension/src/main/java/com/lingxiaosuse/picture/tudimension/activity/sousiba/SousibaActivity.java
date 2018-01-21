package com.lingxiaosuse.picture.tudimension.activity.sousiba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BaseActivity;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SousibaActivity extends BaseActivity {
    private List<String> mImgList = new ArrayList<>();  //存放图片地址
    private List<String> mTitleList = new ArrayList<>();  //存放标题
    private List<String> mUrlList = new ArrayList<>();  //存放跳转链接
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sousiba);

        getDataFromJsoup();
    }

    private void getDataFromJsoup(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(ContentValue.SOUSIBA_URL+"/guochantaotu/TuiGirl/index.html")
                        .timeout(10000);
                Document doc = null;
                try {
                    Connection.Response response = connection.execute();
                    response.cookies();
                    doc = connection.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取首页详细数据
                try {
                    Element elementHome = doc.getElementById("lm_downlist_box");
                    Elements elementImgs = elementHome.getElementsByClass("pic");
                    Elements elementTitle = elementHome.getElementsByClass("geme_dl_info");
                    for (Element elementImg:elementImgs) {

                        //跳转链接
                        final String imgUrl = elementImg.getElementsByTag("a").attr("href");
                        //专辑图片
                        final String imgSrc = elementImg.select("img").attr("src");
                        /*mImgList.add(imgSrc);
                        mTitleList.add(imgAlt);
                        mImgDetailList.add(imgUrl);*/
                        mImgList.add(imgSrc);
                        mUrlList.add(imgUrl);
                        Log.i("sousibaActivity", "跳转链接: "+imgUrl+"  专辑图片："+imgSrc);

                    }

                    for (Element element:elementTitle) {
                        //专辑名字
                        final String imgAlt = element.select("a").first().text();
                        Log.i("sousibaActivity","专辑名字："+imgAlt);
                        mTitleList.add(imgAlt);

                    }
                }catch (Exception e){

                }finally {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

            }
        }).start();
    }
}

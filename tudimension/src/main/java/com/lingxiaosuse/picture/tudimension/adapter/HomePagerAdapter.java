package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by lingxiao on 2017/8/4.
 */

public class HomePagerAdapter extends PagerAdapter{
    private ArrayList<HomePageModle.slidePic> slideList;

    private SimpleDraweeView iv_slideshow;
    private TextView tv_slideshow;

    public HomePagerAdapter(ArrayList<HomePageModle.slidePic> slideList){
        this.slideList = slideList;
    }
    @Override
    public int getCount() {
        return slideList==null?0:slideList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = UIUtils.inflate(R.layout.item_home);
        iv_slideshow = view.findViewById(R.id.iv_slideshow);
        tv_slideshow = view.findViewById(R.id.tv_slideshow);
        if (null != slideList.get(position).cover){
            Uri uri = Uri.parse(slideList.get(position).cover);
            iv_slideshow.setImageURI(uri);
        }

        if (null != slideList.get(position).desc){
            tv_slideshow.setText(slideList.get(position).desc);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

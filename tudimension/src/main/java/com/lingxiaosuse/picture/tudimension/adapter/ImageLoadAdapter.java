package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by lingxiao on 2017/8/29.
 */

public class ImageLoadAdapter extends PagerAdapter{
    private ArrayList<String> urlList;
    private SimpleDraweeView image;
    private boolean isHot;
    private String imgRule ="?imageView2/3/h/1080";
    public ImageLoadAdapter(ArrayList<String> urlList,boolean isHot){
        this.urlList = urlList;
        this.isHot = isHot;
    }
    @Override
    public int getCount() {
        return urlList==null?0:urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view =null;
        try {
            if (isHot){
                view = UIUtils.inflate(R.layout.pager_load_hot);
                image = view.findViewById(R.id.simple_pager_load_hot);
                Uri uri = Uri.parse(urlList.get(position));
                Log.i("code", "instantiateItem: 图片的地址"+urlList.get(position));
                image.setImageURI(uri);
                container.addView(view);
            }else {
                view = UIUtils.inflate(R.layout.pager_load);
                image = view.findViewById(R.id.simple_pager_load);
                Uri uri = Uri.parse(urlList.get(position)+imgRule);
                Log.i("code", "instantiateItem: 图片的地址"+urlList.get(position));
                image.setImageURI(uri);
                container.addView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

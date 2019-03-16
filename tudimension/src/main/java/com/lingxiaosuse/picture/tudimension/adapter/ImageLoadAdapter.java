package com.lingxiaosuse.picture.tudimension.adapter;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.ProgressDrawable;
import com.lingxiaosuse.picture.tudimension.widget.ZoomableViewpager;

import java.util.ArrayList;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * Created by lingxiao on 2017/8/29.
 */

public class ImageLoadAdapter extends PagerAdapter{
    private ArrayList<String> urlList;
    private PhotoDraweeView image;
    private ZoomableViewpager viewpager;
    public ImageLoadAdapter(ArrayList<String> urlList){
        this.urlList = urlList;
    }

    public void setMoveListener(ZoomableViewpager viewpager){
        this.viewpager = viewpager;
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
            view = UIUtils.inflate(R.layout.pager_load_hot);
            image = view.findViewById(R.id.simple_pager_load_hot);
            Uri uri = Uri.parse(urlList.get(position));
            Log.i("code", "instantiateItem: 图片的地址" + urlList.get(position));
            //setControll(uri,image);
            image.setPhotoUri(uri);

            //image.setHierarchy(FrescoHelper.getHierarchy(container.getContext()));
            image.getHierarchy().setProgressBarImage(new ProgressDrawable());
            container.addView(view);
            setOnclick(image);
            setOnLongClick(image);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public OnItemClickListener listener;
    public void setOnItemclick(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void onClick();
    }

    public onItemLongClickListener longClickListener;
    public void setLongClickListener(onItemLongClickListener listener){
        this.longClickListener = listener;
    }
    public interface onItemLongClickListener{
        void onLongClick();
    }

    public interface onItemTouchListener{
        void onTouch(float x,float y,float destence);
    }
    private void setOnclick(PhotoDraweeView view){
       /* view.setOnClickListener(new ZoomableDrawwView.OnClickListener() {
            @Override
            public void onClick() {
                if (listener != null){
                    listener.onClick();
                }
            }
        });*/
        view.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null){
                    listener.onClick();
                }
            }
        });
    }
    private void setOnLongClick(PhotoDraweeView view){
        /*view.setOnLongClickListener(new ZoomableDrawwView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                if (longClickListener != null){
                    longClickListener.onLongClick();
                }
            }
        });*/

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null){
                    longClickListener.onLongClick();
                }
                return true;
            }
        });
    }

    private void setControll(Uri uri,final PhotoDraweeView mPhotoDraweeView){
        /*ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(image.getController())
                .build();*/

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(uri);
        controller.setAutoPlayAnimations(true);
        controller.setOldController(mPhotoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null) {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mPhotoDraweeView.setController(controller.build());
    }
}

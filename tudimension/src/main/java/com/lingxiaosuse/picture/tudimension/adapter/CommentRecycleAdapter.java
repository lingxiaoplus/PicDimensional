package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-6.
 */

public class CommentRecycleAdapter extends BaseQuickAdapter<CommentModle.CommentBean, BaseViewHolder> {
    private SimpleDraweeView headImg;
    private TextView username,time,number,content;

    public CommentRecycleAdapter(int layoutResId, @Nullable List<CommentModle.CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentModle.CommentBean item) {
        headImg = (SimpleDraweeView) holder.getView(R.id.img_comment_head);
        username = (TextView) holder.getView(R.id.tv_comment_username);
        time = (TextView) holder.getView(R.id.tv_comment_time);
        number = (TextView) holder.getView(R.id.tv_comment_num);
        content = (TextView) holder.getView(R.id.tv_comment_content);

        Uri uri = Uri.parse(item.getUser().getAvatar());
        headImg.setImageURI(uri);
        username.setText(item.getUser().getName());
        number.setText(item.getSize()+"");
        content.setText(item.getContent());
    }
}

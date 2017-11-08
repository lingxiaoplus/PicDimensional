package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-6.
 */

public class CommentRecycleAdapter extends BaseRecycleAdapter{
    private List<CommentModle.ResBean.CommentBean> commentList;
    private SimpleDraweeView headImg;
    private TextView username,time,number,content;

    public CommentRecycleAdapter(List mList,int headCont,int footCount) {
        super(mList,headCont,footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        commentList = mList;
        headImg = (SimpleDraweeView) holder.getView(R.id.img_comment_head);
        username = (TextView) holder.getView(R.id.tv_comment_username);
        time = (TextView) holder.getView(R.id.tv_comment_time);
        number = (TextView) holder.getView(R.id.tv_comment_num);
        content = (TextView) holder.getView(R.id.tv_comment_content);

        Uri uri = Uri.parse(commentList.get(position).getUser().getAvatar());
        headImg.setImageURI(uri);
        username.setText(commentList.get(position).getUser().getName());
        number.setText(commentList.get(position).getSize()+"");
        content.setText(commentList.get(position).getContent());
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_comment;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }

}

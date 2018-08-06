package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.lingxiao.common.widget.BaseHolder;
import com.camera.lingxiao.common.widget.BaseRecyclerViewAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;

import java.util.ArrayList;
import java.util.List;

public class SkinAdapter extends BaseRecyclerViewAdapter{

    private SimpleDraweeView imageView;
    private TextView textView;
    private Button button;
    private List<Integer> list;
    private String[] strings = {"默认绿","激情红","知乎蓝","颐缇蓝","基佬紫","少女粉","重力橘","水鸭青","亮蓝","天青"};
    private Context mContext;
    private String[] colors = {"#4CAF50","#E57373","#64B5F6","#7986CB","#9575CD","#F06292","#ffb74d","#4db6ac","#4fc3f7","#4dd0e1"};
    public SkinAdapter(List<Integer> colorList, AdapterListener listener) {
        super(colorList,listener);

    }

    @Override
    protected int getItemViewType(int position, Object o) {
        return R.layout.skip_card_view;
    }

    @Override
    protected BaseHolder onCreateViewHolder(View root, int viewType) {
        return new SkinHolder(root);
    }

    /*@Override
    public void bindData(BaseHolder holder, int position, List mList) {
        list = mList;
        imageView = holder.getView(R.id.img_skin);
        textView = (TextView) holder.getView(R.id.tv_skin);
        button = (Button) holder.getView(R.id.bt_skin_use);
        //imageView.setBackgroundResource(list.get(position));
        imageView.setImageDrawable(new ColorDrawable(Color.parseColor(colors[position])));
        textView.setTextColor(Color.parseColor(colors[position]));
        textView.setText(strings[position]);
    }*/

    private class SkinHolder extends BaseHolder<Integer>{

        public SkinHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Integer color,int position) {
            imageView = getView(R.id.img_skin);
            textView = getView(R.id.tv_skin);
            button = getView(R.id.bt_skin_use);
            //imageView.setBackgroundResource(list.get(position));
            imageView.setImageDrawable(new ColorDrawable(Color.parseColor(colors[position])));
            textView.setTextColor(Color.parseColor(colors[position]));
            textView.setText(strings[position]);
        }
    }

}

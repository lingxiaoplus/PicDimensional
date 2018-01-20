package com.lingxiaosuse.picture.tudimension.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;

import java.util.List;
import java.util.Random;

/**
 * Created by lingxiao on 2018/1/20.
 */

public class AllMzituAdapter extends BaseRecycleAdapter{

    private List<String> titleList;
    private CardView cardView;
    private int[] color = {R.color.colorPrimary,
            android.R.color.holo_blue_light,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_light};
    private Random random = new Random();
    public AllMzituAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    /*public void setTitleList(List<String> titleList){
        this.titleList = titleList;
    }*/
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        titleList = mList;
        TextView textView = (TextView) holder.getView(R.id.tv_card_desc);

        cardView = (CardView) holder.getView(R.id.card_mzitu_root);

        int colorNum = random.nextInt(5);
        //cardView.setCardBackgroundColor();
        cardView.setBackgroundResource(color[colorNum]);
        textView.setText(titleList.get(position));
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_mzitu_all;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}

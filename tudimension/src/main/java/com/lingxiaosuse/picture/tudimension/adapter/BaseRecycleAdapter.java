package com.lingxiaosuse.picture.tudimension.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.camera.lingxiao.common.widget.RecyclerAnimator;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingxiao on 2017/9/30.
 */

public abstract class BaseRecycleAdapter<Data> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder> {
    private List<Data> mList;
    private int headCount = 0; //头布局个数
    private int footCount = 1; //尾布局个数
    private static final int HEAD_TYPE = 1;
    private static final int BODY_TYPE = 2;
    private static final int FOOT_TYPE = 3;
    private boolean isFinish;   //是否加载完成
    private LayoutInflater mLayoutInflater;
    private int mLastPosition = -1;
    private View mItemView;


    public BaseRecycleAdapter(List<Data> mList, int headCount, int footCount) {
        this.mList = mList;
        this.headCount = headCount;
        this.footCount = footCount;
    }

    //获取总共条目数
    public int getBodySize() {
        return mList.size();
    }

    //判断头布局
    public boolean isHead(int position) {
        return headCount != 0 && position < headCount;
    }

    //判断尾布局
    public boolean isFoot(int position) {
        return footCount != 0 && (position >= getBodySize() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHead(position)) {
            return HEAD_TYPE;
        } else if (isFoot(position)) {
            return FOOT_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    @Override
    public BaseRecycleAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater
                .from(parent.getContext());
        View view = null;
        switch (viewType) {
            case HEAD_TYPE:
                view = mLayoutInflater.inflate(getHeadLayoutId(), parent, false);
                break;
            case FOOT_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_foot, parent, false);
                break;
            case BODY_TYPE:
                view = mLayoutInflater.inflate(getLayoutId(), parent, false);
                break;
            default:
                return null;
        }
        final BaseViewHolder holder = new BaseViewHolder(view);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int poisition = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(holder.itemView, poisition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    onItemClickListener.onLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        if (isFoot(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null) {
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams){
                    StaggeredGridLayoutManager.LayoutParams p =
                            (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                }
            }

            if (listener != null && !isFinish) {
                //上拉加载更多
                listener.onLoadMore();
            }

            if (isFinish) {
                holder.getView(R.id.ll_loading).setVisibility(View.GONE);
                holder.getView(R.id.ll_finish).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.ll_loading).setVisibility(View.VISIBLE);
                holder.getView(R.id.ll_finish).setVisibility(View.GONE);
            }
        } else if (isHead(position)) {
            //头布局
            bindHeaderData(holder,position,mList);
        } else {
            bindData(holder, position, mList);
            if (!isFinish) {
                //数据加载完成后不要动画，不然动画会重复
                boolean b = Integer.compare(position, mLastPosition) < 0 ? true : false;
                addInAnimation(mItemView, b);
                mLastPosition = position;
            }
        }
    }

    public abstract void bindData(BaseViewHolder holder, int position, List<Data> mList);

    //处理头布局数据
    protected void bindHeaderData(BaseViewHolder holder, int position, List<Data> mList){

    }
    /**
     * 获取布局文件
     */
    public abstract int getLayoutId();

    /**
     * 获取头布局文件
     */
    public abstract int getHeadLayoutId();

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private Map<Integer, View> mViewMap;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new HashMap<>();
            mItemView = itemView;
        }

        public View getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViewMap.put(id, view);
            }
            return view;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View View, int position);

        void onLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //加载更多
    private onLoadmoreListener listener;

    public interface onLoadmoreListener {
        void onLoadMore();
    }

    public void setRefreshListener(onLoadmoreListener listener) {
        this.listener = listener;
    }

    //数据加载完成
    public void isFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    /**
     * 刷新数据
     */
    public void onRefresh(List<Data> datas) {
        this.mList.clear();
        this.mList = datas;
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        this.mList.add(data);
        notifyItemInserted(this.mList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mList.size();
            Collections.addAll(mList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }
    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mList.size();
            mList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }
    /**
     * 移除一条数据
     */
    public void removeData(int... position) {
        if (position != null && position.length > 0) {
            int startPos = mList.size();
            mList.remove(position);
            notifyItemRangeRemoved(startPos,position.length);
        }
    }

    /**
     * 移除一堆数据
     */
    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * 将动画对象加入集合中  根据左右滑动加入不同
     */

    private void addInAnimation(View view, boolean buttom) {
        List<Animator> list = new ArrayList<>();
        /*if (buttom) {
            list.add(ObjectAnimator.ofFloat(view,
                    "translationY", -view.getMeasuredHeight() * 2, 0));
        } else {
            list.add(ObjectAnimator.ofFloat(view,
                    "translationY", view.getMeasuredHeight() * 2, 0));
        }*/
        list.add(ObjectAnimator.ofFloat(view, "translationY",
                view.getMeasuredHeight() / 2, 0));
        list.add(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
        list.add(ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f));
        list.add(ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f));
        startAnimation(list);
    }

    /**
     * 开启动画
     */
    private void startAnimation(List<Animator> list) {
        int checked = SpUtils.getInt(UIUtils.getContext(),
                ContentValue.ANIMATOR_TYPE,0);
        Interpolator interpolator = null;
        switch (checked){
            case 0:
                interpolator = new BounceInterpolator();
                break;
            case 1:
                interpolator = new DecelerateInterpolator();
                break;
            case 2:
                interpolator = new LinearInterpolator();
                break;
            case 3:
                interpolator = new OvershootInterpolator();
                break;
            case 4:
                interpolator = new AnticipateOvershootInterpolator();
                break;
            default:
                break;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(list);
        if (interpolator != null){
            animatorSet.setInterpolator(interpolator);
        }
        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    //GridLayoutManager
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHead(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else if (isFoot(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

}

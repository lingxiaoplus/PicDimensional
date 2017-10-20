package com.lingxiaosuse.picture.tudimension.rxbus;

/**
 * Created by lingxiao on 2017/10/20.
 * 删除图片后刷新数据源
 */

public class DeleteEvent {
    int position;
    public DeleteEvent(int position){
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}

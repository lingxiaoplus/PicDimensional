package com.camera.lingxiao.common.widget;

public interface AdapterCallback<Data> {
    void update(Data data, BaseHolder<Data> holder);
}

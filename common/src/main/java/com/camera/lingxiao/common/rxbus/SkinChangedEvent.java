package com.camera.lingxiao.common.rxbus;

public class SkinChangedEvent {
    private int color;
    public SkinChangedEvent(int color){
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}

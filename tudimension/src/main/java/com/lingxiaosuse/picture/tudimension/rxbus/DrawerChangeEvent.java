package com.lingxiaosuse.picture.tudimension.rxbus;

/**
 * @author lingxiao
 * 侧滑菜单
 */
public class DrawerChangeEvent {
    private String positions;
    public DrawerChangeEvent(String positions){
        this.positions = positions;
    }
    public String getPositions() {
        return positions;
    }
}

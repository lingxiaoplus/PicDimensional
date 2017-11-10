package com.lingxiaosuse.picture.tudimension.transformer;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by lingxiao on 2017/10/22.
 */

public class ScrollAwareFABBehaviorDefault extends FloatingActionButton.Behavior{

    private boolean visible = true; //是否可见
    public ScrollAwareFABBehaviorDefault(Context context, AttributeSet attr){
        super();
    }
    //列表（RecyclerView）刚开始滑动时候会回调该方法，需要在方法内设置滑动关联轴。这里只需要垂直方向上的滑动即可。
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes);
    }

    //onNestedScroll：滑动的时候不断的回调该方法，通过dyConsumed来判断是上滑还是下滑。
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout,
                child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && visible) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            //fab只隐藏，不显示，在hide方法里设置为GONE，所以不会再调用onNestedScroll方法
            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    fab.setVisibility(View.INVISIBLE);
                }
            });
            visible = false;
        } else if (dyConsumed  < 0) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show();
            visible = true;
        }
    }



}

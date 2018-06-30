package com.lingxiao.skinlibrary;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class SkinLib {
    public static void init(Application context){
        SkinCompatManager.withoutActivity(context)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .loadSkin();
    }

    /**
     * 后缀加载
     * @param skinName R.color.windowBackgroundColor, 添加对应资源R.color.windowBackgroundColor_night
     */
    public static void loadSkin(String skinName){
        SkinCompatManager.getInstance().loadSkin(skinName, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
    }

    /**
     *
     * @param skinName
     */
    public static void loadSkinBack(String skinName){
        SkinCompatManager.getInstance().loadSkin(skinName, SkinCompatManager.SKIN_LOADER_STRATEGY_PREFIX_BUILD_IN); // 前缀加载
    }

    public static void resetSkin(){
        // 恢复应用默认皮肤
        SkinCompatManager.getInstance().restoreDefaultTheme();
    }
}

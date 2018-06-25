package com.lingxiaosuse.picture.tudimension.fragment;

import java.util.HashMap;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class FragmentFactory {
    private static HashMap<Integer, com.camera.lingxiao.common.app.BaseFragment> mFragmentMap = new HashMap<>();
    public static com.camera.lingxiao.common.app.BaseFragment createFragment(int pos){
        com.camera.lingxiao.common.app.BaseFragment fragment = mFragmentMap.get(pos);
        if (fragment == null){
            switch (pos){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new CategoryFragment();
                    break;
                case 2:
                    fragment = new HotFragment();
                    break;
                case 3:
                    fragment = new SpecialFragment();
                    break;
                /*case 4:
                    fragment = new HomeFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;*/
            }
            mFragmentMap.put(pos,fragment);
        }
        return fragment;
    }
}

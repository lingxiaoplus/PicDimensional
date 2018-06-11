package com.camera.lingxiao.common.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface LifeCycleListener {
    /**
     * Activity相关生命周期回调
     */
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    /**
     * Fragment(特有)相关生命周期回调
     */

    void onAttach(Activity activity);

    void onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle);

    void onActivityCreated(Bundle bundle);

    void onDestroyView();

    void onDetach();
}

package com.reactnative.toast.example;

import android.os.Bundle;

import com.navigation.androidx.TabBarFragment;
import com.navigation.androidx.TabBarItem;
import com.reactnative.hybridnavigation.HybridFragment;
import com.reactnative.hybridnavigation.ReactAppCompatActivity;
import com.reactnative.hybridnavigation.ReactNavigationFragment;
import com.reactnative.hybridnavigation.ReactTabBarFragment;


public class MainActivity extends ReactAppCompatActivity {

    @Override
    protected void onCreateMainComponent() {

        Bundle options1 = new Bundle();
        Bundle titleItem1 = new Bundle();
        titleItem1.putString("title", "React");
        options1.putBundle("titleItem", titleItem1);
        HybridFragment f1 = getReactBridgeManager().createFragment("Tab1", null, options1);

        Bundle options2 = new Bundle();
        Bundle titleItem2 = new Bundle();
        titleItem2.putString("title", "Native");
        options2.putBundle("titleItem", titleItem2);
        HybridFragment f2 = getReactBridgeManager().createFragment("Tab2", null, options2);

        ReactNavigationFragment nav1 = new ReactNavigationFragment();
        nav1.setTabBarItem(new TabBarItem("React"));
        nav1.setRootFragment(f1);
        ReactNavigationFragment nav2 = new ReactNavigationFragment();
        nav2.setTabBarItem(new TabBarItem("Native"));
        nav2.setRootFragment(f2);
        TabBarFragment tabs = new ReactTabBarFragment();
        tabs.setChildFragments(nav1, nav2);
        setActivityRootFragment(tabs);
    }


}
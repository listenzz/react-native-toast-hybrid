package com.reactnative.hud.example;
  
import android.os.Bundle;

import com.navigation.androidx.NavigationFragment;
import com.navigation.androidx.TabBarFragment;
import com.navigation.androidx.TabBarItem;
import com.navigationhybrid.HybridFragment;
import com.navigationhybrid.ReactAppCompatActivity;

public class MainActivity extends ReactAppCompatActivity {

    @Override
    protected void onCreateMainComponent() {

        Bundle options1 = new Bundle();
        Bundle titleItem1 = new Bundle();
        titleItem1.putString("title", "Tab1");
        options1.putBundle("titleItem", titleItem1);
        HybridFragment f1 = getReactBridgeManager().createFragment("HudHybrid", null, options1);

        Bundle options2 = new Bundle();
        Bundle titleItem2 = new Bundle();
        titleItem2.putString("title", "Tab2");
        options2.putBundle("titleItem", titleItem2);
        HybridFragment f2 = getReactBridgeManager().createFragment("HudHybrid", null, options2);

        NavigationFragment nav1 = new NavigationFragment();
        nav1.setTabBarItem(new TabBarItem(null, "Tab1"));
        nav1.setRootFragment(f1);
        NavigationFragment nav2 = new NavigationFragment();
        nav2.setTabBarItem(new TabBarItem(null, "Tab2"));
        nav2.setRootFragment(f2);
        TabBarFragment tabs = new TabBarFragment();
        tabs.setChildFragments(nav1, nav2);
        setActivityRootFragment(tabs);
    }
}
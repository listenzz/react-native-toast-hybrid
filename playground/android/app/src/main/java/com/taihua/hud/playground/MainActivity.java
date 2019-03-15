package com.taihua.hud.playground;

import com.navigationhybrid.HybridFragment;
import com.navigationhybrid.ReactAppCompatActivity;

import me.listenzz.navigation.NavigationFragment;
import me.listenzz.navigation.TabBarFragment;
import me.listenzz.navigation.TabBarItem;

public class MainActivity extends ReactAppCompatActivity {

    @Override
    protected void onCreateMainComponent() {
        HybridFragment f1 = getReactBridgeManager().createFragment("playground");
        HybridFragment f2 = getReactBridgeManager().createFragment("playground");
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

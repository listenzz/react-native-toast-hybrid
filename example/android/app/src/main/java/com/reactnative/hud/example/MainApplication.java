package com.reactnative.hud.example;

import android.app.Application;
import android.content.Context;

import com.facebook.common.logging.FLog;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.navigationhybrid.ReactBridgeManager;
import com.reactnative.hud.HudHybridPackage;

public class MainApplication extends Application implements ReactApplication {

	private final ReactNativeHost mReactNativeHost =
		new ReactNativeHost(this) {
			@Override
			public boolean getUseDeveloperSupport() {
				return BuildConfig.DEBUG;
			}

			@Override
			protected List<ReactPackage> getPackages() {
				@SuppressWarnings("UnnecessaryLocalVariable")
				List<ReactPackage> packages = new PackageList(this).getPackages();
				packages.add(new HudHybridPackage());
				return packages;
			}

			@Override
			protected String getJSMainModuleName() {
				return "example/index";
			}
		};

	@Override
	public ReactNativeHost getReactNativeHost() {
		return mReactNativeHost;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SoLoader.init(this, /* native exopackage */ false);
		initializeFlipper(this); // Remove this line if you don't want Flipper enabled
		ReactBridgeManager bridgeManager = ReactBridgeManager.get();
		bridgeManager.install(getReactNativeHost());

		// 注册原生页面
		bridgeManager.registerNativeModule("Tab2", DemoFragment.class);

		FLog.setMinimumLoggingLevel(FLog.INFO);
	}

	/**
	 * Loads Flipper in React Native templates.
	 *
	 * @param context
	 */
	private static void initializeFlipper(Context context) {
		if (BuildConfig.DEBUG) {
			try {
				/*
				We use reflection here to pick up the class that initializes Flipper,
				since Flipper library is not available in release mode
				*/
				Class<?> aClass = Class.forName("com.facebook.flipper.ReactNativeFlipper");
				aClass.getMethod("initializeFlipper", Context.class).invoke(null, context);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
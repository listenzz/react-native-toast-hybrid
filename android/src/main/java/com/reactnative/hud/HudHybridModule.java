package com.reactnative.hud;

import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class HudHybridModule extends ReactContextBaseJavaModule {

    private SparseArray<Hud> hudSparseArray = new SparseArray<>();

    private static int hudKeyGenerator = 0;


    public HudHybridModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "HudHybrid";
    }

    @Override
    public void onCatalystInstanceDestroy() {
        final Activity activity = getCurrentActivity();
        if (activity != null && !activity.isFinishing()) {
            UiThreadUtil.runOnUiThread(() -> {
                if (!activity.isFinishing()) {
                    int size = hudSparseArray.size();
                    for (int i = size - 1; i > -1; i--) {
                        Hud hud = hudSparseArray.get(hudSparseArray.keyAt(i));
                        hud.hide();
                    }
                }
            });
        }
    }

    @ReactMethod
    public void create(final Promise promise) {
        UiThreadUtil.runOnUiThread(() -> {
            if (getCurrentActivity() != null) {
                Hud hud = new Hud(getCurrentActivity());
                final int hudKey = setupHud(hud);
                promise.resolve(hudKey);
            } else {
                promise.resolve(-1);
            }
        });
    }

    private int setupHud(Hud hud) {
        final int hudKey = hudKeyGenerator++;
        hud.setOnHudDismissListener(() -> {
            hudSparseArray.remove(hudKey);
            ReactContext reactContext = getReactApplicationContext();
            if (reactContext != null) {
                DeviceEventManagerModule.RCTDeviceEventEmitter emitter = reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
                WritableMap map = new WritableNativeMap();
                map.putInt("hudKey", hudKey);
                emitter.emit("ON_HUD_DISMISS", map);
            }
        });
        hudSparseArray.put(hudKey, hud);
        return hudKey;
    }

    @ReactMethod
    public void spinner(final int hudkey, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.spinner(text == null ? HudConfig.loadingText : text);
            }
        });
    }

    @ReactMethod
    public void hide(final int hudID) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudID);
            if (hud != null) {
                hud.hide();
            }
        });
    }

    @ReactMethod
    public void hideDelay(final int hudkey, final int delayMs) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.hideDelay(delayMs);
            }
        });
    }

    @ReactMethod
    public void hideDelayDefault(final int hudkey) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.hideDelayDefault();
            }
        });
    }

    @ReactMethod
    public void text(final int hudkey, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.text(text);
            }
        });
    }

    @ReactMethod
    public void info(final int hudkey, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.info(text);
            }
        });
    }

    @ReactMethod
    public void done(final int hudkey, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.done(text);
            }
        });
    }


    @ReactMethod
    public void error(final int hudkey, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Hud hud = hudSparseArray.get(hudkey);
            if (hud != null) {
                hud.error(text);
            }
        });
    }

    @ReactMethod
    public void config(final ReadableMap config) {
        UiThreadUtil.runOnUiThread(() -> {
            if (config.hasKey("backgroundColor")) {
                HudConfig.backgroundColor = Color.parseColor(config.getString("backgroundColor"));
            } else {
                HudConfig.backgroundColor = HudConfig.DEFAULT_BACKGROUND_COLOR;
            }

            if (config.hasKey("tintColor")) {
                HudConfig.tintColor = Color.parseColor(config.getString("tintColor"));
            } else {
                HudConfig.tintColor = HudConfig.DEFAULT_TINT_COLOR;
            }

            if (config.hasKey("cornerRadius")) {
                double cornerRadius = config.getDouble("cornerRadius");
                HudConfig.cornerRadius = (float) cornerRadius;
            } else {
                HudConfig.cornerRadius = HudConfig.DEFAULT_CORNER_RADIUS;
            }

            if (config.hasKey("duration")) {
                HudConfig.duration = config.getInt("duration");
            } else {
                HudConfig.duration = HudConfig.DEFAULT_DURATION;
            }

            if (config.hasKey("graceTime")) {
                HudConfig.graceTime = config.getInt("graceTime");
            } else {
                HudConfig.graceTime = HudConfig.DEFAULT_GRACE_TIME;
            }

            if (config.hasKey("minShowTime")) {
                HudConfig.minShowTime = config.getInt("minShowTime");
            } else {
                HudConfig.minShowTime = HudConfig.DEFAULT_MIN_SHOW_TIME;
            }

            if (config.hasKey("loadingText")) {
                HudConfig.loadingText = config.getString("loadingText");
            } else {
                HudConfig.loadingText = HudConfig.DEFAULT_LOADING_TEXT;
            }
        });
    }

}

package com.taihua.hud;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class HUDModule extends ReactContextBaseJavaModule {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private SparseArray<HUD> hudSparseArray = new SparseArray<>();

    private static int hudKeyGenerator = 0;

    public HUDModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {

            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {
                handler.removeCallbacksAndMessages(null);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = hudSparseArray.size() -1; i > -1; i --) {
                            HUD hud = hudSparseArray.valueAt(i);
                            hudSparseArray.removeAt(i);
                            hud.hide();
                        }
                    }
                });
            }
        });
    }

    @Override
    public String getName() {
        return "HUD";
    }

    @ReactMethod
    public void showLoading(final Promise promise) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    hud.show(HUDConfig.loadingText);
                    final int hudKey = setupHud(hud);
                    promise.resolve(hudKey);
                } else {
                    promise.reject("404", "host is missing");
                }
            }
        });
    }

    private int setupHud(HUD hud) {
        final int hudKey = hudKeyGenerator++;
        hud.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                hudSparseArray.remove(hudKey);
            }
        });
        hudSparseArray.put(hudKey, hud);
        return hudKey;
    }

    @ReactMethod
    public void hideLoading(final int hudID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudID);
                if (hud != null) {
                    hud.hide();
                }
            }
        });
    }

    @ReactMethod
    public void text(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    hud.text(text);
                    setupHud(hud);
                    hud.hideDefaultDelay();
                }
            }
        });
    }

    @ReactMethod
    public void info(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    hud.info(text);
                    setupHud(hud);
                    hud.hideDefaultDelay();
                }
            }
        });
    }

    @ReactMethod
    public void done(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    hud.done(text);
                    setupHud(hud);
                    hud.hideDefaultDelay();
                }
            }
        });
    }


    @ReactMethod
    public void error(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    hud.error(text);
                    setupHud(hud);
                    hud.hideDefaultDelay();
                }
            }
        });
    }

    @ReactMethod
    public void config(ReadableMap config) {
        if (config.hasKey("backgroundColor")) {
            HUDConfig.backgroundColor = Color.parseColor(config.getString("backgroundColor"));
        } else {
            HUDConfig.backgroundColor = HUDConfig.DEFAULT_BACKGROUND_COLOR;
        }

        if (config.hasKey("tintColor")) {
            HUDConfig.tintColor = Color.parseColor(config.getString("tintColor"));
        } else {
            HUDConfig.tintColor = HUDConfig.DEFAULT_TINT_COLOR;
        }

        if (config.hasKey("cornerRadius")) {
            double cornerRadius = config.getDouble("cornerRadius");
            HUDConfig.cornerRadius = (float) cornerRadius;
        } else {
            HUDConfig.cornerRadius = HUDConfig.DEFAULT_CORNER_RADIUS;
        }

        if (config.hasKey("duration")) {
            HUDConfig.duration = config.getInt("duration");
        } else {
            HUDConfig.duration = HUDConfig.DEFAULT_DURATION;
        }

        if (config.hasKey("graceTime")) {
            HUDConfig.graceTime = config.getInt("graceTime");
        } else {
            HUDConfig.graceTime = HUDConfig.DEFAULT_GRACE_TIME;
        }

        if (config.hasKey("minShowTime")) {
            HUDConfig.minShowTime = config.getInt("minShowTime");
        } else {
            HUDConfig.minShowTime = HUDConfig.DEFAULT_MIN_SHOW_TIME;
        }

        if (config.hasKey("loadingText")) {
            HUDConfig.loadingText = config.getString("loadingText");
        } else {
            HUDConfig.loadingText = HUDConfig.DEFAULT_LOADING_TEXT;
        }

        if (config.hasKey("dimAmount")) {
            HUDConfig.dimAmount = (float) config.getDouble("dimAmount");
        } else {
            HUDConfig.dimAmount = HUDConfig.DEFAULT_DIM_AMOUNT;
        }
    }

}

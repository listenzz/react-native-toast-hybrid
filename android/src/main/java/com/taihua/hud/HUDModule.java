package com.taihua.hud;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class HUDModule extends ReactContextBaseJavaModule {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private SparseArray<HUD> hudSparseArray = new SparseArray<>();

    private static int hudKeyGenerator = 0;

    public HUDModule(final ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "HUD";
    }


    @ReactMethod
    public void create(final Promise promise) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    HUD hud = new HUD(getCurrentActivity());
                    final int hudKey = setupHud(hud);
                    promise.resolve(hudKey);
                } else {
                    promise.resolve(-1);
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
                ReactContext reactContext = getReactApplicationContext();
                if (reactContext != null) {
                    DeviceEventManagerModule.RCTDeviceEventEmitter emitter = reactContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
                    WritableMap map = new WritableNativeMap();
                    map.putInt("hudKey", hudKey);
                    emitter.emit("ON_HUD_DISMISS", map);
                }
            }
        });
        hudSparseArray.put(hudKey, hud);
        return hudKey;
    }

    @ReactMethod
    public void spinner(final int hudkey, final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.spinner(text == null ? HUDConfig.loadingText : text);
                }
            }
        });
    }

    @ReactMethod
    public void hide(final int hudID) {
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
    public void hideDelay(final int hudkey, final int delayMs) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.hideDelay(delayMs);
                }
            }
        });
    }

    @ReactMethod
    public void hideDelayDefault(final int hudkey) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.hideDelayDefault();
                }
            }
        });
    }

    @ReactMethod
    public void text(final int hudkey, final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.text(text);
                }
            }
        });
    }

    @ReactMethod
    public void info(final int hudkey, final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.info(text);
                }
            }
        });
    }

    @ReactMethod
    public void done(final int hudkey, final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.done(text);
                }
            }
        });
    }


    @ReactMethod
    public void error(final int hudkey, final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HUD hud = hudSparseArray.get(hudkey);
                if (hud != null) {
                    hud.error(text);
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
    }

}

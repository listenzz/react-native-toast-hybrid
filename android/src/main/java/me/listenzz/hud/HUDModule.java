package me.listenzz.hud;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class HUDModule extends ReactContextBaseJavaModule {

    private final Handler handler = new Handler(Looper.getMainLooper());
    HUD hud;

    public HUDModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "HUD";
    }

    @ReactMethod
    public void show() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null) {
                    if (hud == null) {
                        hud = new HUD(getCurrentActivity());
                    }

                    if (hud.context != getCurrentActivity()) {
                        hud.forceHide();
                        hud = new HUD(getCurrentActivity());
                    }
                    hud.show(HUDConfig.loadingText);
                }
            }
        });
    }

    @ReactMethod
    public void hide() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (hud != null) {
                    int result = hud.hide();
                    if (result == 0) {
                        hud = null;
                    }
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
                    new HUD(getCurrentActivity()).text(text);
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
                    new HUD(getCurrentActivity()).info(text);
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
                    new HUD(getCurrentActivity()).done(text);
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
                    new HUD(getCurrentActivity()).error(text);
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

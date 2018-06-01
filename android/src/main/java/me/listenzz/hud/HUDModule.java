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
                    hud.show(null);
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
        }

        if (config.hasKey("cornerRadius")) {
            double cornerRadius = config.getDouble("cornerRadius");
            HUDConfig.cornerRadius = (float) cornerRadius;
        }

        if (config.hasKey("duration")) {
            HUDConfig.duration = config.getInt("duration");
        }
    }

}

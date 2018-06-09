package me.listenzz.hud;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HUDModule extends ReactContextBaseJavaModule {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private HUD loadingHUD;

    private List<HUD> huds = new LinkedList<>();

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
                if (loadingHUD != null) {
                    loadingHUD.hide();
                    loadingHUD = null;
                }

                Iterator<HUD> it = huds.iterator();
                while (it.hasNext()) {
                    HUD hud = it.next();
                    it.remove();
                    hud.hide();
                }
            }
        });
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
                    if (loadingHUD == null) {
                        loadingHUD = new HUD(getCurrentActivity());
                    }
                    loadingHUD.show(HUDConfig.loadingText);
                }
            }
        });
    }

    @ReactMethod
    public void hide() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadingHUD != null) {
                    int result = loadingHUD.hide();
                    if (result == 0) {
                        loadingHUD = null;
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
                    HUD hud = new HUD(getCurrentActivity());
                    configureHud(hud);
                    hud.text(text);
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
                    configureHud(hud);
                    hud.info(text);
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
                    configureHud(hud);
                    hud.done(text);
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
                    configureHud(hud);
                    hud.error(text);
                }
            }
        });
    }

    private void configureHud(final HUD hud) {
        huds.add(hud);
        hud.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                huds.remove(hud);
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

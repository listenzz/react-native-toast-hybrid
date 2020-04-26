package com.reactnative.toast;

import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;

public class ToastHybridModule extends ReactContextBaseJavaModule {

    private SparseArray<Toast> toastSparseArray = new SparseArray<>();

    private static int toastKeyGenerator = 0;


    public ToastHybridModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "ToastHybrid";
    }

    @Override
    public void onCatalystInstanceDestroy() {
        final Activity activity = getCurrentActivity();
        if (activity != null && !activity.isFinishing()) {
            UiThreadUtil.runOnUiThread(() -> {
                if (!activity.isFinishing()) {
                    int size = toastSparseArray.size();
                    for (int i = size - 1; i > -1; i--) {
                        Toast toast = toastSparseArray.get(toastSparseArray.keyAt(i));
                        toast.hide();
                    }
                }
            });
        }
    }

    @ReactMethod
    public void create(final Promise promise) {
        UiThreadUtil.runOnUiThread(() -> {
            if (getCurrentActivity() != null) {
                Toast toast = new Toast(getCurrentActivity());
                final int key = setupToast(toast);
                promise.resolve(key);
            } else {
                promise.resolve(-1);
            }
        });
    }

    @ReactMethod
    public void ensure(final int key, final Promise promise) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                promise.resolve(key);
            } else {
                create(promise);
            }
        });
    }

    private int setupToast(Toast toast) {
        final int key = toastKeyGenerator++;
        toast.setOnDismissListener(() -> toastSparseArray.remove(key));
        toastSparseArray.put(key, toast);
        return key;
    }

    @ReactMethod
    public void loading(final int key, final String text, double graceTime) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.loading(text == null ? ToastConfig.loadingText : text, (int) graceTime);
            }
        });
    }

    @ReactMethod
    public void hide(final int key) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.hide();
            }
        });
    }

    @ReactMethod
    public void text(final int key, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.text(text);
            }
        });
    }

    @ReactMethod
    public void info(final int key, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.info(text);
            }
        });
    }

    @ReactMethod
    public void done(final int key, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.done(text);
            }
        });
    }


    @ReactMethod
    public void error(final int key, final String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get(key);
            if (toast != null) {
                toast.error(text);
            }
        });
    }

    @ReactMethod
    public void config(final ReadableMap config) {
        UiThreadUtil.runOnUiThread(() -> {
            if (config.hasKey("backgroundColor")) {
                ToastConfig.backgroundColor = Color.parseColor(config.getString("backgroundColor"));
            } else {
                ToastConfig.backgroundColor = ToastConfig.DEFAULT_BACKGROUND_COLOR;
            }

            if (config.hasKey("tintColor")) {
                ToastConfig.tintColor = Color.parseColor(config.getString("tintColor"));
            } else {
                ToastConfig.tintColor = ToastConfig.DEFAULT_TINT_COLOR;
            }

            if (config.hasKey("cornerRadius")) {
                double cornerRadius = config.getDouble("cornerRadius");
                ToastConfig.cornerRadius = (float) cornerRadius;
            } else {
                ToastConfig.cornerRadius = ToastConfig.DEFAULT_CORNER_RADIUS;
            }

            if (config.hasKey("duration")) {
                ToastConfig.duration = config.getInt("duration");
            } else {
                ToastConfig.duration = ToastConfig.DEFAULT_DURATION;
            }

            if (config.hasKey("graceTime")) {
                ToastConfig.graceTime = config.getInt("graceTime");
            } else {
                ToastConfig.graceTime = ToastConfig.DEFAULT_GRACE_TIME;
            }

            if (config.hasKey("minShowTime")) {
                ToastConfig.minShowTime = config.getInt("minShowTime");
            } else {
                ToastConfig.minShowTime = ToastConfig.DEFAULT_MIN_SHOW_TIME;
            }

            if (config.hasKey("loadingText")) {
                ToastConfig.loadingText = config.getString("loadingText");
            } else {
                ToastConfig.loadingText = ToastConfig.DEFAULT_LOADING_TEXT;
            }
        });
    }

}

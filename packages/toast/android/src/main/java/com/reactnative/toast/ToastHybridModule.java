package com.reactnative.toast;

import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;

public class ToastHybridModule extends NativeToastSpec {
    private final SparseArray<Toast> toastSparseArray = new SparseArray<>();

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
    public void invalidate() {
        final Activity activity = getActiveActivity();
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


    @Override
    public void createToast(Promise promise) {
        UiThreadUtil.runOnUiThread(() -> {
            if (getActiveActivity() != null) {
                Toast toast = new Toast(getActiveActivity());
                final int key = setupToast(toast);
                promise.resolve(key);
            } else {
                promise.resolve(-1);
            }
        });
    }
    
    @Override
    public void hide(double key) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.hide();
            }
        });
    }

    @Override
    public void ensure(double key, Promise promise) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                promise.resolve(key);
            } else {
                createToast(promise);
            }
        });
    }

    @Override
    public void loading(double key, @Nullable String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.loading(text == null ? ToastConfig.loadingText : text);
            }
        });
    }

    @Override
    public void text(double key, String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.text(text);
            }
        });
    }

    @Override
    public void info(double key, String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.info(text);
            }
        });
    }

    @Override
    public void done(double key, String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.done(text);
            }
        });
    }

    @Override
    public void error(double key, String text) {
        UiThreadUtil.runOnUiThread(() -> {
            Toast toast = toastSparseArray.get((int) key);
            if (toast != null) {
                toast.error(text);
            }
        });
    }

    private int setupToast(Toast toast) {
        final int key = toastKeyGenerator++;
        toast.setOnDismissListener(() -> toastSparseArray.remove(key));
        toastSparseArray.put(key, toast);
        return key;
    }

    @Override
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

            if (config.hasKey("fontSize")) {
                ToastConfig.fontSizeSP = config.getInt("fontSize");
            } else {
                ToastConfig.fontSizeSP = ToastConfig.DEFAULT_FONT_SIZE_SP;
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
    
    @Nullable
    private Activity getActiveActivity() {
        ReactContext reactContext = getReactApplicationContext();
        if (!reactContext.hasActiveReactInstance()) {
            return null;
        }

        return reactContext.getCurrentActivity();
    }

}

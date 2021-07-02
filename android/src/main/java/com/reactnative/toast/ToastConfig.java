package com.reactnative.toast;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ToastConfig {

    static int DEFAULT_DURATION = 2000;
    static int DEFAULT_GRACE_TIME = 300;
    static int DEFAULT_MIN_SHOW_TIME = 500;
    static int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#b1000000");
    static float DEFAULT_CORNER_RADIUS = 5;
    @ColorInt
    static int DEFAULT_TINT_COLOR = Color.WHITE;
    static int DEFAULT_FONT_SIZE_SP = 16;
    static String DEFAULT_LOADING_TEXT = null;

    public static int duration = DEFAULT_DURATION;
    public static int graceTime = DEFAULT_GRACE_TIME;
    public static int minShowTime = DEFAULT_MIN_SHOW_TIME;
    public static int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    @ColorInt
    public static int tintColor = DEFAULT_TINT_COLOR;
    public static int fontSizeSP = DEFAULT_FONT_SIZE_SP;
    public static float cornerRadius = DEFAULT_CORNER_RADIUS;
    public static String loadingText = null;

}

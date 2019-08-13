package com.taihua.hud;

import android.graphics.Color;
import androidx.annotation.ColorInt;

public class HUDConfig {

    static int DEFAULT_DURATION = 2000;
    static int DEFAULT_GRACE_TIME = 300;
    static int DEFAULT_MIN_SHOW_TIME = 800;
    static int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#b1000000");
    static float DEFAULT_CORNER_RADIUS = 5;
    static @ColorInt
    int DEFAULT_TINT_COLOR = Color.WHITE;
    static String DEFAULT_LOADING_TEXT = null;

    public static int duration = DEFAULT_DURATION;
    public static int graceTime = DEFAULT_GRACE_TIME;
    public static int minShowTime = DEFAULT_MIN_SHOW_TIME;

    public static int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    public static @ColorInt
    int tintColor = DEFAULT_TINT_COLOR;
    public static float cornerRadius = DEFAULT_CORNER_RADIUS;
    public static String loadingText = null;

}

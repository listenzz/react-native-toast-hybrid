package com.taihua.hud;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

public class HUD {

    public HUD(Activity context) {
        this.context = context;
    }

    private Activity context;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private KProgressHUD kProgressHUD;

    private DialogInterface.OnDismissListener dismissListener;

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public void onDestroy() {
        if (kProgressHUD != null) {
            kProgressHUD.onDestroy();
            hide();
        }
    }

    public void hide() {
        if (kProgressHUD != null) {
            handler.removeCallbacksAndMessages(null);
            kProgressHUD.dismiss();
            kProgressHUD = null;
        }
    }

    public void hideDelay(int delayMs) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, delayMs);
    }

    public void hideDelayDefault() {
        hideDelay(HUDConfig.duration);
    }

    public HUD spinner(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context)
                    .setTintColor(HUDConfig.tintColor)
                    .setGraceTime(HUDConfig.graceTime)
                    .setMinShowTime(HUDConfig.minShowTime);
            configHUD(kProgressHUD);
            kProgressHUD.show();
        }
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (!TextUtils.isEmpty(text)) {
            kProgressHUD.setLabel(text, HUDConfig.tintColor);
        } else {
            kProgressHUD.setLabel(null);
        }
        return this;
    }

    public HUD text(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show();
        }
        TextView textView = new TextView(context);
        textView.setTextColor(HUDConfig.tintColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(text);
        kProgressHUD.setCustomView(textView);
        kProgressHUD.setLabel(null);
        return this;
    }

    public HUD info(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show();
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_info);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        return this;
    }

    public HUD done(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show();
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_done);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        return this;
    }

    public HUD error(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show();
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_error);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        return this;
    }

    private void configHUD(KProgressHUD hud) {
        hud.setCornerRadius(HUDConfig.cornerRadius);
        hud.setBackgroundColor(HUDConfig.backgroundColor);
        hud.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dismissListener != null) {
                    dismissListener.onDismiss(dialogInterface);
                }
                hide();
            }
        });
    }

}

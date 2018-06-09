package me.listenzz.hud;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

public class HUD {

    public HUD(Activity context) {
        this.context = context;
    }

    private Activity context;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private KProgressHUD kProgressHUD;

    private int loadingCount;

    private DialogInterface.OnDismissListener dismissListener;

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public void show(String text) {
        if (loadingCount == 0) {
            kProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setTintColor(HUDConfig.tintColor)
                    .setGraceTime(HUDConfig.graceTime)
                    .setMinShowTime(HUDConfig.minShowTime);

            configHUD(kProgressHUD);
            if (text != null) {
                kProgressHUD.setLabel(text, HUDConfig.tintColor);
            }
            kProgressHUD.show();
        }
        loadingCount++;
    }

    public int hide() {
        loadingCount--;
        if (loadingCount <= 0) {
            hideInternal();
        }
        return loadingCount;
    }

    private void hideInternal() {
        if (kProgressHUD != null) {
            handler.removeCallbacksAndMessages(null);
            kProgressHUD.dismiss();
            kProgressHUD = null;
        }
        loadingCount = 0;
    }

    public void text(String text) {
        kProgressHUD = KProgressHUD.create(context);
        configHUD(kProgressHUD);
        TextView textView = new TextView(context);
        textView.setTextColor(HUDConfig.tintColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        kProgressHUD.setCustomView(textView);
        kProgressHUD.show();
        scheduleDismiss();
    }

    public void info(String text) {
        kProgressHUD = KProgressHUD.create(context);
        configHUD(kProgressHUD);
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_info);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        kProgressHUD.show();
        scheduleDismiss();
    }


    public void done(String text) {
        kProgressHUD = KProgressHUD.create(context);
        configHUD(kProgressHUD);
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_done);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        kProgressHUD.show();
        scheduleDismiss();
    }


    public void error(String text) {
        kProgressHUD = KProgressHUD.create(context);
        configHUD(kProgressHUD);
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_error);
        DrawableCompat.setTint(drawable, HUDConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HUDConfig.tintColor);
        kProgressHUD.show();
        scheduleDismiss();
    }

    private void configHUD(KProgressHUD hud) {
        hud.setCornerRadius(HUDConfig.cornerRadius);
        hud.setBackgroundColor(HUDConfig.backgroundColor);
        hud.setDimAmount(HUDConfig.dimAmount);
        hud.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dismissListener != null) {
                    dismissListener.onDismiss(dialogInterface);
                }
                hideInternal();
            }
        });
    }

    private void scheduleDismiss() {
        if (handler.getLooper() == Looper.getMainLooper()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideInternal();
                }
            }, HUDConfig.duration);
        }
    }

}

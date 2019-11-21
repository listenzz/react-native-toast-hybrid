package com.reactnative.hud;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Hud {

    public Hud(Activity context) {
        this.context = context;
    }

    private Activity context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private KProgressHUD kProgressHUD;

    private OnHudDismissListener mOnHudDismissListener;

    public void setOnHudDismissListener(OnHudDismissListener listener) {
        mOnHudDismissListener = listener;
    }

    public void hide() {
        if (kProgressHUD != null) {
            handler.removeCallbacksAndMessages(null);
            kProgressHUD.dismiss();
            kProgressHUD = null;
        }
    }

    public void hideDelay(int delayMs) {
        handler.postDelayed(() -> hide(), delayMs);
    }

    public void hideDelayDefault() {
        hideDelay(HudConfig.duration);
    }

    public Hud spinner(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context)
                    .setTintColor(HudConfig.tintColor)
                    .setGraceTime(HudConfig.graceTime)
                    .setMinShowTime(HudConfig.minShowTime);
            configHUD(kProgressHUD);
            kProgressHUD.show(getCurrentWindow(context));
        }
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (!TextUtils.isEmpty(text)) {
            kProgressHUD.setLabel(text, HudConfig.tintColor);
        } else {
            kProgressHUD.setLabel(null);
        }
        return this;
    }

    public Hud text(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show(getCurrentWindow(context));
        }
        TextView textView = new TextView(context);
        textView.setTextColor(HudConfig.tintColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(text);
        kProgressHUD.setCustomView(textView);
        kProgressHUD.setLabel(null);
        return this;
    }

    public Hud info(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show(getCurrentWindow(context));
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_info);
        DrawableCompat.setTint(drawable, HudConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HudConfig.tintColor);
        return this;
    }

    public Hud done(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show(getCurrentWindow(context));
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_done);
        DrawableCompat.setTint(drawable, HudConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HudConfig.tintColor);
        return this;
    }

    public Hud error(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
            configHUD(kProgressHUD);
            kProgressHUD.show(getCurrentWindow(context));
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_error);
        DrawableCompat.setTint(drawable, HudConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.setLabel(text, HudConfig.tintColor);
        return this;
    }

    private void configHUD(KProgressHUD hud) {
        hud.setCornerRadius(HudConfig.cornerRadius);
        hud.setBackgroundColor(HudConfig.backgroundColor);
        hud.setOnHudDismissListener(mOnHudDismissListener);
    }

    public Window getCurrentWindow(Activity activity) {
        DialogFragment dialogFragment = null;
        if (activity instanceof FragmentActivity) {
            dialogFragment = getDialogFragment(((FragmentActivity) activity).getSupportFragmentManager());
        }

        if (dialogFragment != null && dialogFragment.isAdded()) {
            return dialogFragment.getDialog().getWindow();
        } else {
            return activity.getWindow();
        }
    }

    @Nullable
    public static DialogFragment getDialogFragment(@NonNull FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
        if (fragment instanceof DialogFragment) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            if (dialogFragment.getShowsDialog()) {
                return dialogFragment;
            }
        }

        if (fragment != null && fragment.isAdded()) {
            return getDialogFragment(fragment.getChildFragmentManager());
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        int count = fragments.size();
        if (count > 0) {
            fragment = fragments.get(count -1);

            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                if (dialogFragment.getShowsDialog()) {
                    return dialogFragment;
                }
            }

            if (fragment != null && fragment.isAdded()) {
                return getDialogFragment(fragment.getChildFragmentManager());
            }
        }

        return null;
    }

}

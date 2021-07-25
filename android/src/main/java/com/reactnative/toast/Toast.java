package com.reactnative.toast;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class Toast {

    public static void text(@NonNull Activity activity, @NonNull String text) {
        Toast toast = new Toast(activity);
        toast.text(text);
        toast.hideDelayDefault();
    }

    public static void info(@NonNull Activity activity, @NonNull String text) {
        Toast toast = new Toast(activity);
        toast.info(text);
        toast.hideDelayDefault();
    }

    public static void done(@NonNull Activity activity, @NonNull String text) {
        Toast toast = new Toast(activity);
        toast.done(text);
        toast.hideDelayDefault();
    }

    public static void error(@NonNull Activity activity, @NonNull String text) {
        Toast toast = new Toast(activity);
        toast.error(text);
        toast.hideDelayDefault();
    }

    public static Toast loading(@NonNull Activity activity, @Nullable String text) {
        Toast toast = new Toast(activity);
        toast.loading(text);
        return toast;
    }

    public Toast(Activity context) {
        this.context = context;
    }

    private Activity context;
    private KProgressHUD kProgressHUD;

    private OnDismissListener mOnDismissListener;

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void hide() {
        if (kProgressHUD != null) {
            kProgressHUD.hide();
        }
    }

    public void hideDelay(int delayMs) {
        if (kProgressHUD != null) {
            kProgressHUD.hideDelay(delayMs);
        }
    }

    public void hideDelayDefault() {
        hideDelay(ToastConfig.duration);
    }

    public Toast loading(@Nullable String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context)
                    .setGraceTime(ToastConfig.graceTime)
                    .setMinShowTime(ToastConfig.minShowTime);
            configHUD(kProgressHUD);
        }
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (!TextUtils.isEmpty(text)) {
            kProgressHUD.setLabel(text);
        } else {
            kProgressHUD.setLabel(null);
        }
        kProgressHUD.show(getCurrentWindow(context));
        return this;
    }

    public Toast text(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context).setGraceTime(0);
            configHUD(kProgressHUD);
        }
        TextView textView = new TextView(context);
        textView.setTextColor(ToastConfig.tintColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ToastConfig.fontSizeSP);
        textView.setText(text);
        kProgressHUD.setCustomView(textView, false);
        kProgressHUD.setLabel(null);
        kProgressHUD.show(getCurrentWindow(context));
        return this;
    }

    public Toast info(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context).setGraceTime(0);
            configHUD(kProgressHUD);
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_info);
        DrawableCompat.setTint(drawable, ToastConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView, false);
        kProgressHUD.setLabel(text);
        kProgressHUD.show(getCurrentWindow(context));
        return this;
    }

    public Toast done(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context).setGraceTime(0);
            configHUD(kProgressHUD);
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_done);
        DrawableCompat.setTint(drawable, ToastConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView, false);
        kProgressHUD.setLabel(text);
        kProgressHUD.show(getCurrentWindow(context));
        return this;
    }

    public Toast error(String text) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context).setGraceTime(0);
            configHUD(kProgressHUD);
        }
        ImageView imageView = new ImageView(context);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hud_error);
        DrawableCompat.setTint(drawable, ToastConfig.tintColor);
        imageView.setBackground(drawable);
        kProgressHUD.setCustomView(imageView, false);
        kProgressHUD.setLabel(text);
        kProgressHUD.show(getCurrentWindow(context));
        return this;
    }

    private void configHUD(KProgressHUD hud) {
        hud.setCornerRadius(ToastConfig.cornerRadius);
        hud.setBackgroundColor(ToastConfig.backgroundColor);
        hud.setTextSize(ToastConfig.fontSizeSP);
        hud.setTintColor(ToastConfig.tintColor);
        hud.setOnHudDismissListener(() -> {
            kProgressHUD = null;
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        });
    }

    public Window getCurrentWindow(Activity activity) {
        DialogFragment dialogFragment = null;
        if (activity instanceof FragmentActivity) {
            dialogFragment = getDialogFragment(((FragmentActivity) activity).getSupportFragmentManager());
        }

        if (dialogFragment != null) {
            return dialogFragment.getDialog().getWindow();
        } else {
            return activity.getWindow();
        }
    }

    @Nullable
    public static DialogFragment getDialogFragment(@NonNull FragmentManager fragmentManager) {
        if (fragmentManager.isDestroyed()) {
            return null;
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        int count = fragments.size();

        DialogFragment dialog = null;

        for (int i = count - 1; i > -1; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isAdded()) {
                if (fragment instanceof DialogFragment) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    if (dialogFragment.getShowsDialog()) {
                        dialog = dialogFragment;
                        break;
                    }
                }

                if (dialog == null) {
                    dialog = getDialogFragment(fragment.getChildFragmentManager());
                }
            }
        }

        return dialog;
    }

}

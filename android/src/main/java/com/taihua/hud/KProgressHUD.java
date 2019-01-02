/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.taihua.hud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;


public class KProgressHUD implements DialogInterface.OnDismissListener {

    public enum Style {
        SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
    }

    // To avoid redundant APIs, make the HUD as a wrapper class around a Dialog
    private final ProgressDialog mProgressDialog;
    private int mWindowColor;
    private int mTintColor;
    private float mCornerRadius;
    private Activity mActivity;

    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;

    private int mGraceTimeMs;
    private int mMinShowTimeMs;
    private Handler mGraceTimer;
    private Handler mMinShowTimer;
    private Date mShowStarted;
    private boolean mFinished;

    private DialogInterface.OnDismissListener mDismissListener;

    public KProgressHUD(Activity activity) {
        mActivity = activity;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setOnDismissListener(this);
        //noinspection deprecation
        mWindowColor = activity.getResources().getColor(R.color.kprogresshud_default_color);
        mTintColor = Color.WHITE;
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
        mGraceTimeMs = 0;
        mFinished = false;

        setStyle(Style.SPIN_INDETERMINATE);
    }

    /**
     * Create a new HUD. Have the same effect as the constructor.
     * For convenient only.
     *
     * @param activity Activity that the HUD bound to
     * @return An unique HUD instance
     */
    public static KProgressHUD create(Activity activity) {
        return new KProgressHUD(activity);
    }

    /**
     * Create a new HUD. specify the HUD style (if you use a custom view, you need {@code KProgressHUD.create(Context context)}).
     *
     * @param activity Activity that the HUD bound to
     * @param style    One of the KProgressHUD.Style values
     * @return An unique HUD instance
     */
    public static KProgressHUD create(Activity activity, Style style) {
        return new KProgressHUD(activity).setStyle(style);
    }

    /**
     * Specify the HUD style (not needed if you use a custom view)
     *
     * @param style One of the KProgressHUD.Style values
     * @return Current HUD
     */
    public KProgressHUD setStyle(Style style) {
        View view = null;
        switch (style) {
            case SPIN_INDETERMINATE:
                view = new LoadingView(mActivity);
                break;
            case PIE_DETERMINATE:
                view = new PieView(mActivity);
                break;
            case ANNULAR_DETERMINATE:
                view = new AnnularView(mActivity);
                break;
            case BAR_DETERMINATE:
                view = new BarView(mActivity);
                break;
            // No custom view style here, because view will be added later
        }

        if (isActivityValid()) {
            mProgressDialog.setView(view);
        }
        return this;
    }

    private boolean isActivityValid() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return mActivity != null && !mActivity.isDestroyed() && !mActivity.isFinishing();
        } else {
            return mActivity != null && !mActivity.isFinishing();
        }
    }

    /**
     * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
     *
     * @param width  in dp
     * @param height in dp
     * @return Current HUD
     */
    public KProgressHUD setSize(int width, int height) {
        if (isActivityValid()) {
            mProgressDialog.setSize(width, height);
        }
        return this;
    }

    /**
     * @param color ARGB color
     * @return Current HUD
     * @deprecated As of release 1.1.0, replaced by {@link #setBackgroundColor(int)}
     */
    @Deprecated
    public KProgressHUD setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify the HUD background color
     *
     * @param color ARGB color
     * @return Current HUD
     */
    public KProgressHUD setBackgroundColor(int color) {
        mWindowColor = color;
        return this;
    }


    public KProgressHUD setTintColor(int color) {
        mTintColor = color;
        return this;
    }

    /**
     * Specify corner radius of the HUD (default is 10)
     *
     * @param radius Corner radius in dp
     * @return Current HUD
     */
    public KProgressHUD setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    /**
     * Change animation speed relative to default. Used with indeterminate style
     *
     * @param scale Default is 1. If you want double the speed, set the param at 2.
     * @return Current HUD
     */
    public KProgressHUD setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }

    /**
     * Optional label to be displayed.
     *
     * @return Current HUD
     */
    public KProgressHUD setLabel(String label) {
        if (isActivityValid()) {
            mProgressDialog.setLabel(label);
        }
        return this;
    }

    /**
     * Optional label to be displayed
     *
     * @return Current HUD
     */
    public KProgressHUD setLabel(String label, int color) {
        if (isActivityValid()) {
            mProgressDialog.setLabel(label, color);
        }
        return this;
    }

    /**
     * Optional detail description to be displayed on the HUD
     *
     * @return Current HUD
     */
    public KProgressHUD setDetailsLabel(String detailsLabel) {
        if (isActivityValid()) {
            mProgressDialog.setDetailsLabel(detailsLabel);
        }
        return this;
    }

    /**
     * Optional detail description to be displayed
     *
     * @return Current HUD
     */
    public KProgressHUD setDetailsLabel(String detailsLabel, int color) {
        if (isActivityValid()) {
            mProgressDialog.setDetailsLabel(detailsLabel, color);
        }
        return this;
    }

    /**
     * Max value for use in one of the determinate styles
     *
     * @return Current HUD
     */
    public KProgressHUD setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    /**
     * Set current progress. Only have effect when use with a determinate style, or a custom
     * view which implements Determinate interface.
     */
    public void setProgress(int progress) {
        if (isActivityValid()) {
            mProgressDialog.setProgress(progress);
        }
    }

    /**
     * Provide a custom view to be displayed.
     *
     * @param view Must not be null
     * @return Current HUD
     */
    public KProgressHUD setCustomView(View view) {
        if (view != null) {
            if (isActivityValid()) {
                mProgressDialog.setView(view);
            }
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    /**
     * Specify whether this HUD can be cancelled by using back button (default is false)
     * <p>
     * Setting a cancelable to true with this method will set a null callback,
     * clearing any callback previously set with
     * {@link #setCancellable(DialogInterface.OnCancelListener)}
     *
     * @return Current HUD
     */
    public KProgressHUD setCancellable(boolean isCancellable) {
        if (isActivityValid()) {
            mProgressDialog.setCancelable(isCancellable);
            mProgressDialog.setOnCancelListener(null);
        }
        return this;
    }

    public KProgressHUD setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDismissListener = listener;
        return this;
    }

    /**
     * Specify a callback to run when using the back button (default is null)
     *
     * @param listener The code that will run if the user presses the back
     *                 button. If you pass null, the dialog won't be cancellable, just like
     *                 if you had called {@link #setCancellable(boolean)} passing false.
     * @return Current HUD
     */
    public KProgressHUD setCancellable(DialogInterface.OnCancelListener listener) {
        if (isActivityValid()) {
            mProgressDialog.setCancelable(null != listener);
            mProgressDialog.setOnCancelListener(listener);
        }
        return this;
    }

    /**
     * Specify whether this HUD closes itself if progress reaches max. Default is true.
     *
     * @return Current HUD
     */
    public KProgressHUD setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    /**
     * Grace period is the time (in milliseconds) that the invoked method may be run without
     * showing the HUD. If the task finishes before the grace time runs out, the HUD will
     * not be shown at all.
     * This may be used to prevent HUD display for very short tasks.
     * Defaults to 0 (no grace time).
     *
     * @param graceTimeMs Grace time in milliseconds
     * @return Current HUD
     */
    public KProgressHUD setGraceTime(int graceTimeMs) {
        mGraceTimeMs = graceTimeMs;
        return this;
    }

    public KProgressHUD setMinShowTime(int minShowTimeMs) {
        mMinShowTimeMs = minShowTimeMs;
        return this;
    }

    public KProgressHUD show() {
        if (!isShowing()) {
            if (mMinShowTimer != null) {
                mMinShowTimer.removeCallbacksAndMessages(null);
                mMinShowTimer = null;
            }
            mFinished = false;
            if (mGraceTimeMs == 0) {
                showInternal();
            } else {
                mGraceTimer = new Handler(Looper.getMainLooper());
                mGraceTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mFinished) {
                            showInternal();
                        }
                    }
                }, mGraceTimeMs);
            }
        }
        return this;
    }

    private void showInternal() {
        if (!isActivityValid()) {
            return;
        }

        mShowStarted = new Date();
        mProgressDialog.show();
        Window window = mProgressDialog.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Helper.setStatusBarStyle(window, isDark());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Helper.setStatusBarTranslucent(window, isTranslucent());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Helper.setNavigationBarColor(window, Color.TRANSPARENT);
            Helper.setNavigationBarStyle(window, isNavigationBarDark());
        }
    }

    @TargetApi(23)
    private boolean isDark() {
        return (mActivity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
    }

    @TargetApi(26)
    private boolean isNavigationBarDark() {
        return (mActivity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0;
    }

    @TargetApi(19)
    private boolean isTranslucent() {
        return (mActivity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
    }

    public boolean isShowing() {
        return mShowStarted != null && mProgressDialog.isShowing();
    }

    public void dismiss() {
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
        mFinished = true;
        if (mMinShowTimeMs > 0.0 && mShowStarted != null) {
            Date now = new Date();
            int interval = (int) (now.getTime() - mShowStarted.getTime());
            if (interval < mMinShowTimeMs) {
                mMinShowTimer = new Handler(Looper.getMainLooper());
                mMinShowTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissInternal();
                    }
                }, mMinShowTimeMs - interval);
                return;
            }
        }
        dismissInternal();
    }

    private void dismissInternal() {
        if (isShowing() && isActivityValid()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mShowStarted = null;
        mFinished = true;
        if (mDismissListener != null) {
            mDismissListener.onDismiss(dialogInterface);
        }
    }

    public void onDestroy() {
        dismissInternal();
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
        if (mMinShowTimer != null) {
            mMinShowTimer.removeCallbacksAndMessages(null);
            mMinShowTimer = null;
        }
    }

    private class ProgressDialog extends Dialog {

        private Determinate mDeterminateView;
        private Indeterminate mIndeterminateView;
        private View mView;
        private TextView mLabelText;
        private TextView mDetailsText;
        private String mLabel;
        private String mDetailsLabel;
        private FrameLayout mCustomViewContainer;
        private BackgroundLayout mBackgroundLayout;
        private int mWidth, mHeight;
        private int mLabelColor = Color.WHITE;
        private int mDetailColor = Color.WHITE;

        public ProgressDialog(Context context) {
            super(context, R.style.Theme_HUD_FullScreenDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.kprogresshud_hud);
            setCanceledOnTouchOutside(false);
            initViews();
        }

        private void initViews() {
            mBackgroundLayout = findViewById(R.id.background);
            mBackgroundLayout.setBaseColor(mWindowColor);
            mBackgroundLayout.setCornerRadius(mCornerRadius);
            if (mWidth != 0) {
                updateBackgroundSize();
            }

            mCustomViewContainer = findViewById(R.id.container);
            addViewToFrame(mView);

            if (mDeterminateView != null) {
                mDeterminateView.setMax(mMaxProgress);
            }
            if (mIndeterminateView != null) {
                mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
            }

            mLabelText = findViewById(R.id.label);
            setLabel(mLabel, mLabelColor);
            mDetailsText = findViewById(R.id.details_label);
            setDetailsLabel(mDetailsLabel, mDetailColor);
        }

        private void addViewToFrame(View view) {
            if (view == null) return;
            if (view instanceof LoadingView) {
                LoadingView loadingView = (LoadingView) view;
                loadingView.setColor(mTintColor);
                loadingView.setSize(Helper.dpToPixel(32, getContext()));
            }
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view, params);
        }

        private void updateBackgroundSize() {
            ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
            params.width = Helper.dpToPixel(mWidth, getContext());
            params.height = Helper.dpToPixel(mHeight, getContext());
            mBackgroundLayout.setLayoutParams(params);
        }

        public void setProgress(int progress) {
            if (mDeterminateView != null) {
                mDeterminateView.setProgress(progress);
                if (mIsAutoDismiss && progress >= mMaxProgress) {
                    dismiss();
                }
            }
        }

        public void setView(View view) {
            if (view != null) {
                if (view instanceof Determinate) {
                    mDeterminateView = (Determinate) view;
                }
                if (view instanceof Indeterminate) {
                    mIndeterminateView = (Indeterminate) view;
                }
                mView = view;
                if (isShowing()) {
                    mCustomViewContainer.removeAllViews();
                    addViewToFrame(view);
                }
            }
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            mDetailsLabel = detailsLabel;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setLabel(String label, int color) {
            mLabel = label;
            mLabelColor = color;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setTextColor(color);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel, int color) {
            mDetailsLabel = detailsLabel;
            mDetailColor = color;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setTextColor(color);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackgroundLayout != null) {
                updateBackgroundSize();
            }
        }
    }
}

package com.reactnative.toast;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class KProgressHUD {

    public enum Style {
        SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
    }

    private Activity mActivity;
    private HudView mHudView;
    private View mView;

    private int mGraceTimeMs;
    private int mMinShowTimeMs;
    private Handler mGraceTimer;
    private Handler mMinShowTimer;
    private Date mShowStarted;
    private boolean mFinished;

    private String mLabel;
    private String mDetailsLabel;
    private int mLabelColor;
    private int mDetailLabelColor;

    private int mWindowColor;
    private int mTintColor;
    private float mCornerRadius;
    private int mTextSizeSP;

    private OnDismissListener mOnDismissListener;

    public KProgressHUD(Activity activity) {
        mActivity = activity;
        mWindowColor = activity.getResources().getColor(R.color.kprogresshud_default_color);
        mTintColor = Color.WHITE;
        mLabelColor = mTintColor;
        mDetailLabelColor = mTintColor;
        mCornerRadius = 10;
        mFinished = false;
        setStyle(Style.SPIN_INDETERMINATE);
    }

    public static KProgressHUD create(Activity activity) {
        return new KProgressHUD(activity);
    }

    public void setOnHudDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public KProgressHUD setBackgroundColor(int color) {
        mWindowColor = color;
        return this;
    }

    public KProgressHUD setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    public KProgressHUD setTintColor(int color) {
        mTintColor = color;
        mLabelColor = color;
        mDetailLabelColor = color;
        return this;
    }

    public KProgressHUD setTextSize(int sizeSP) {
        mTextSizeSP = sizeSP;
        return this;
    }

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
        }
        setCustomView(view, true);
        return this;
    }

    public KProgressHUD setCustomView(View view, boolean fromStyle) {
        if (!fromStyle) {
            cancelGraceTimer();
        }
        cancelMinShowTimer();
        mView = view;
        if (isShowing()) {
            mHudView.setView(view);
        }
        return this;
    }

    public KProgressHUD setLabel(String label) {
        mLabel = label;
        if (isShowing()) {
            mHudView.setLabel(label);
        }
        return this;
    }

    public KProgressHUD setDetailsLabel(String detailsLabel) {
        mDetailsLabel = detailsLabel;
        if (isShowing()) {
            mHudView.setDetailsLabel(detailsLabel);
        }
        return this;
    }

    public KProgressHUD setGraceTime(int graceTimeMs) {
        mGraceTimeMs = graceTimeMs;
        return this;
    }

    public KProgressHUD setMinShowTime(int minShowTimeMs) {
        mMinShowTimeMs = minShowTimeMs;
        return this;
    }

    public KProgressHUD show(final Window window) {
        if (!isShowing()) {
            mFinished = false;
            if (mGraceTimeMs == 0) {
                showInternal(window);
            } else {
                cancelGraceTimer();
                mGraceTimer = new Handler(Looper.getMainLooper());
                mGraceTimer.postDelayed(() -> {
                    if (!mFinished) {
                        showInternal(window);
                    }
                }, mGraceTimeMs);
            }
        }
        return this;
    }

    private void showInternal(@Nullable Window window) {
        mShowStarted = new Date();
        mHudView = new HudView(mActivity);
        mHudView.setView(mView);
        mHudView.setLabel(mLabel);
        mHudView.setDetailsLabel(mDetailsLabel);
        mHudView.show(window);
    }

    public void hide() {
        cancelGraceTimer();
        mFinished = true;
        if (mMinShowTimeMs > 0.0 && mShowStarted != null) {
            Date now = new Date();
            int interval = (int) (now.getTime() - mShowStarted.getTime());
            if (interval < mMinShowTimeMs) {
                cancelMinShowTimer();
                mMinShowTimer = new Handler(Looper.getMainLooper());
                mMinShowTimer.postDelayed(this::hideInternal, mMinShowTimeMs - interval);
                return;
            }
        }
        hideInternal();
    }

    public void hideDelay(int delayMs) {
        cancelMinShowTimer();
        mMinShowTimer = new Handler(Looper.getMainLooper());
        mMinShowTimer.postDelayed(this::hide, delayMs);
    }

    private void hideInternal() {
        cancelGraceTimer();
        cancelMinShowTimer();
        if (mHudView != null) {
            mHudView.hide();
            mHudView = null;
        }
    }

    private void cancelMinShowTimer() {
        if (mMinShowTimer != null) {
            mMinShowTimer.removeCallbacksAndMessages(null);
            mMinShowTimer = null;
        }
    }

    private void cancelGraceTimer() {
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
    }

    boolean isShowing() {
        return mHudView != null;
    }

    private class HudView extends FrameLayout {

        private TextView labelText;
        private TextView detailsText;
        private FrameLayout customViewContainer;
        private Dialog dialog;

        public HudView(@NonNull Context context) {
            super(context);
            init(context);
        }

        public HudView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public HudView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        @TargetApi(21)
        public HudView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(context);
        }

        void show(@Nullable Window window) {
            ViewParent parent = getParent();
            if (parent == null) {
                if (window != null) {
                    ViewGroup decorView = (ViewGroup) window.getDecorView();
                    decorView.addView(this, MATCH_PARENT, MATCH_PARENT);
                } else {
                    showAsDialog();
                }
            }
        }

        private void showAsDialog() {
            Dialog dialog = new Dialog(mActivity);
            this.dialog = dialog;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = 0;
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }

        void hide() {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                return;
            }
            ViewParent parent = getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        }

        private void init(Context context) {
            LayoutInflater.from(context).inflate(R.layout.kprogresshud_hud, this);
            BackgroundLayout backgroundLayout = findViewById(R.id.background);
            backgroundLayout.setBaseColor(mWindowColor);
            backgroundLayout.setCornerRadius(mCornerRadius);
            customViewContainer = findViewById(R.id.container);
            labelText = findViewById(R.id.label);
            detailsText = findViewById(R.id.details_label);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

        public void setView(View view) {
            customViewContainer.removeAllViews();
            if (view != null && view.getParent() == null) {
                addViewToFrame(view);
            }
        }

        private void addViewToFrame(View view) {
            if (view instanceof LoadingView) {
                LoadingView loadingView = (LoadingView) view;
                loadingView.setColor(mTintColor);
                loadingView.setSize(Helper.dpToPixel(32, getContext()));
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            customViewContainer.addView(view, params);
        }

        public void setLabel(String label) {
            if (!TextUtils.isEmpty(label)) {
                labelText.setText(label);
                labelText.setTextColor(mLabelColor);
                labelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSizeSP);
                labelText.setVisibility(View.VISIBLE);
            } else {
                labelText.setVisibility(View.GONE);
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            if (!TextUtils.isEmpty(detailsLabel)) {
                detailsText.setText(detailsLabel);
                detailsText.setTextColor(mDetailLabelColor);
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        }

    }
}

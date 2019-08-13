package com.taihua.hud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class KProgressHUD {

    public enum Style {
        SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
    }

    private Context mContext;
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

    private OnHudDismissListener mOnHudDismissListener;

    public KProgressHUD(Context context) {
        mContext = context;
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
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

    public void setOnHudDismissListener(OnHudDismissListener listener) {
        mOnHudDismissListener = listener;
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

    public KProgressHUD setStyle(Style style) {
        View view = null;
        switch (style) {
            case SPIN_INDETERMINATE:
                view = new LoadingView(mContext);
                break;
            case PIE_DETERMINATE:
                view = new PieView(mContext);
                break;
            case ANNULAR_DETERMINATE:
                view = new AnnularView(mContext);
                break;
            case BAR_DETERMINATE:
                view = new BarView(mContext);
                break;
        }
        setCustomView(view);
        return this;
    }

    public KProgressHUD setCustomView(View view) {
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

    public KProgressHUD setLabel(String label, int color) {
        mLabel = label;
        mLabelColor = color;
        if (isShowing()) {
            mHudView.setLabel(label, color);
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

    public KProgressHUD setDetailsLabel(String detailsLabel, int color) {
        mDetailsLabel = detailsLabel;
        mDetailLabelColor = color;
        if (isShowing()) {
            mHudView.setDetailsLabel(detailsLabel, color);
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
            if (mMinShowTimer != null) {
                mMinShowTimer.removeCallbacksAndMessages(null);
                mMinShowTimer = null;
            }
            mFinished = false;
            if (mGraceTimeMs == 0) {
                showInternal(window);
            } else {
                mGraceTimer = new Handler(Looper.getMainLooper());
                mGraceTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mFinished) {
                            showInternal(window);
                        }
                    }
                }, mGraceTimeMs);
            }
        }
        return this;
    }

    private void showInternal(Window window) {
        mShowStarted = new Date();
        mHudView = new HudView(window.getContext());
        mHudView.setView(mView);
        mHudView.setLabel(mLabel, mLabelColor);
        mHudView.setDetailsLabel(mDetailsLabel, mDetailLabelColor);
        mHudView.show(window);
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
        if (mHudView != null) {
            mHudView.hide();
            mHudView = null;
        }
    }

    boolean isShowing() {
        return mHudView != null;
    }

    private class HudView extends FrameLayout {

        private TextView labelText;
        private TextView detailsText;
        private FrameLayout customViewContainer;
        private BackgroundLayout backgroundLayout;

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

        void show(Window window) {
            ViewGroup parent = (ViewGroup) getParent();
            if (parent == null) {
                ViewGroup decorView = (ViewGroup) window.getDecorView();
                decorView.addView(this, MATCH_PARENT, MATCH_PARENT);
            }
        }

        void hide() {
            ViewGroup parent = (ViewGroup) getParent();
            if (parent != null) {
                parent.removeView(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnHudDismissListener != null) {
                mOnHudDismissListener.onDismiss();
            }
        }

        private void init(Context context) {
            LayoutInflater.from(context).inflate(R.layout.kprogresshud_hud, this);
            backgroundLayout = findViewById(R.id.background);
            backgroundLayout.setBaseColor(mWindowColor);
            backgroundLayout.setCornerRadius(mCornerRadius);
            customViewContainer = findViewById(R.id.container);
            labelText = findViewById(R.id.label);
            detailsText = findViewById(R.id.details_label);
            setFocusableInTouchMode(true);
            requestFocus();
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hide();
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

        public void setView(View view) {
            customViewContainer.removeAllViews();
            if (view != null) {
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
                labelText.setVisibility(View.VISIBLE);
            } else {
                labelText.setVisibility(View.GONE);
            }
        }

        public void setLabel(String label, int color) {
            if (!TextUtils.isEmpty(label)) {
                labelText.setText(label);
                labelText.setTextColor(color);
                labelText.setVisibility(View.VISIBLE);
            } else {
                labelText.setVisibility(View.GONE);
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            if (!TextUtils.isEmpty(detailsLabel)) {
                detailsText.setText(detailsLabel);
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        }

        public void setDetailsLabel(String detailsLabel, int color) {
            if (!TextUtils.isEmpty(detailsLabel)) {
                detailsText.setText(detailsLabel);
                detailsText.setTextColor(color);
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        }
    }
}

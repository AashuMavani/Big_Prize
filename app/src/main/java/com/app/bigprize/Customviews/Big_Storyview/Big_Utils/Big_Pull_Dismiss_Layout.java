package com.app.bigprize.Customviews.Big_Storyview.Big_Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.app.bigprize.Customviews.Big_Storyview.Big_callback.Big_Touch_Callbacks;


public class Big_Pull_Dismiss_Layout extends FrameLayout {
    private Listener listener;
    private ViewDragHelper dragHelper;
    private float minFlingVelocity;
    private float verticalTouchSlop;
    private Big_Touch_Callbacks mTouchCallbacks;
    private boolean animateAlpha;

    public Big_Pull_Dismiss_Layout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Big_Pull_Dismiss_Layout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Big_Pull_Dismiss_Layout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @TargetApi(21)
    public Big_Pull_Dismiss_Layout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle, int defResStyle) {
        super(context, attrs, defStyle, defResStyle);
        init(context);
    }

    private void init(@NonNull Context context) {
        if (!isInEditMode()) {
            ViewConfiguration vc = ViewConfiguration.get(context);
            minFlingVelocity = (float) vc.getScaledMinimumFlingVelocity();
            dragHelper = ViewDragHelper.create(this, new ViewDragCallback(this));
        }
    }

    public void computeScroll() {
        super.computeScroll();
        if (dragHelper != null && dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        boolean pullingDown = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                verticalTouchSlop = event.getY();
            case MotionEvent.ACTION_MOVE:
                final float dy = event.getY() - verticalTouchSlop;
                if (dy > dragHelper.getTouchSlop()) {
                    pullingDown = true;
                    mTouchCallbacks.touchPull();
                } else {
                    mTouchCallbacks.touchDown(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                verticalTouchSlop = 0.0f;
                mTouchCallbacks.touchUp();
                break;
        }

        if (!dragHelper.shouldInterceptTouchEvent(event) && pullingDown) {
            if (dragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE &&
                    dragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL)) {

                View child = getChildAt(0);
                if (child != null && !listener.onShouldInterceptTouchEvent()) {
                    dragHelper.captureChildView(child, event.getPointerId(0));
                    return dragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING;
                }
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return dragHelper.getCapturedView() != null;
    }

    public void setMinFlingVelocity(float velocity) {
        minFlingVelocity = velocity;
    }

    public void setAnimateAlpha(boolean b) {
        animateAlpha = b;
    }

    public void setListener(Listener l) {
        listener = l;
    }

    private static class ViewDragCallback extends ViewDragHelper.Callback {
        private Big_Pull_Dismiss_Layout pullDismissLayout;
        private int startTop;
        private float dragPercent;
        private View capturedView;
        private boolean dismissed;

        private ViewDragCallback(Big_Pull_Dismiss_Layout layout) {
            pullDismissLayout = layout;
            dragPercent = 0.0F;
            dismissed = false;
        }

        public boolean tryCaptureView(View view, int i) {
            return capturedView == null;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return top < 0 ? 0 : top;
        }

        public void onViewCaptured(View view, int activePointerId) {
            capturedView = view;
            startTop = view.getTop();
            dragPercent = 0.0F;
            dismissed = false;
        }

        @SuppressLint({"NewApi"})
        public void onViewPositionChanged(View view, int left, int top, int dx, int dy) {
            int range = pullDismissLayout.getHeight();
            int moved = Math.abs(top - startTop);
            if (range > 0) {
                dragPercent = (float) moved / (float) range;
            }
            if (pullDismissLayout.animateAlpha) {
                view.setAlpha(1.0F - dragPercent);
                pullDismissLayout.invalidate();
            }
        }

        public void onViewDragStateChanged(int state) {
            if (capturedView != null && dismissed && state == ViewDragHelper.STATE_IDLE) {
                pullDismissLayout.removeView(capturedView);
                if (pullDismissLayout.listener != null) {
                    pullDismissLayout.listener.onDismissed();
                }
            }
        }

        public void onViewReleased(View view, float xv, float yv) {
            dismissed = dragPercent >= 0.50F ||
                    (Math.abs(xv) > pullDismissLayout.minFlingVelocity && dragPercent > 0.20F);
            int finalTop = dismissed ? pullDismissLayout.getHeight() : startTop;
            if (!dismissed) {
                pullDismissLayout.getmTouchCallbacks().touchUp();
            }
            pullDismissLayout.dragHelper.settleCapturedViewAt(0, finalTop);
            pullDismissLayout.invalidate();
        }
    }

    public void setmTouchCallbacks(Big_Touch_Callbacks mTouchCallbacks) {
        this.mTouchCallbacks = mTouchCallbacks;
    }

    public interface Listener {
        /**
         * Layout is pulled down to dismiss
         * Good time to finish activity, remove fragment or any view
         */
        void onDismissed();

        /**
         * Convenient method to avoid layout_color overriding event
         * If you have a RecyclerView or ScrollerView in our layout_color your can
         * avoid Pull_Dismiss_Layout to handle event.
         *
         * @return true when ignore pull down event, f
         * false for allow Pull_Dismiss_Layout handle event
         */
        boolean onShouldInterceptTouchEvent();
    }

    public Big_Touch_Callbacks getmTouchCallbacks() {
        return mTouchCallbacks;
    }
}
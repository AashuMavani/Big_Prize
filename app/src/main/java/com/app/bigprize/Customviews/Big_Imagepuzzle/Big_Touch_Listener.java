package com.app.bigprize.Customviews.Big_Imagepuzzle;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.bigprize.Activity.Big_PlayPuzzleActivity;
import com.app.bigprize.utils.BIG_Custom_ScrollView;


public class Big_Touch_Listener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private Big_PlayPuzzleActivity activity;

    private BIG_Custom_ScrollView scrollView;
    public void setScrollView(BIG_Custom_ScrollView scrollView) {
        this.scrollView = scrollView;
    }
    public Big_Touch_Listener(Big_PlayPuzzleActivity activity) {
            this.activity = activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;

        Big_Puzzle_Piece piece = (Big_Puzzle_Piece) view;
        if (!piece.canMove) {
            return true;
        }

        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                scrollView.lockScrollView(false);
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                scrollView.lockScrollView(false);
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                activity.setTimer();
                break;
            case MotionEvent.ACTION_UP:
                scrollView.lockScrollView(true);
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    sendViewToBack(piece);
                    activity.checkGameOver();
                }
                break;
            default:
                scrollView.lockScrollView(true);
                break;
        }

        return true;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }
}
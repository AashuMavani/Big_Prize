package com.app.bigprize.Customviews.Big_Storyview.Big_Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class Big_DynamicImageView extends androidx.appcompat.widget.AppCompatImageView {

    public Big_DynamicImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
            this.setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
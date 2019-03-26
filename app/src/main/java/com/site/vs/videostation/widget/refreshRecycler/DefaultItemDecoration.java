package com.site.vs.videostation.widget.refreshRecycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/10/21 12:46
 */

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    //    private Drawable mDivider;
    int height;
    Paint paint;

    public DefaultItemDecoration(Context context, int height) {
//        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
//        mDivider = a.getDrawable(0);
//        a.recycle();
        this.height = height;
        paint = new Paint();
        paint.setColor(Color.parseColor("#F1F1F1"));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + height;
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, height);
    }
}

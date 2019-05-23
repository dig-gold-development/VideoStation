package com.site.vs.videostation.widget.refreshRecycler;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class GridMarginDecoration extends RecyclerView.ItemDecoration {

    private int margin = 0;
    private int leftMargin = 0;

    public GridMarginDecoration(int margin) {
        this.margin = margin;
        this.leftMargin = margin;
    }

    public GridMarginDecoration(int margin, int leftMargin) {
        this.margin = margin;
        this.leftMargin = leftMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(margin, margin, margin, margin);
    }
}
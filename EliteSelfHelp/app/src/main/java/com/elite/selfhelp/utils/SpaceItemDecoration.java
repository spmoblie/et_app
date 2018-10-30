package com.elite.selfhelp.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by user on 26/02/18.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }

    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}

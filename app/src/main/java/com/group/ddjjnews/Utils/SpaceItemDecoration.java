package com.group.ddjjnews.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int space;
    boolean ignoreFirst;
    public SpaceItemDecoration(int space, boolean ignoreFirst) {
        this.space = space;
        this.ignoreFirst = ignoreFirst;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = (space + 7);
        if (!ignoreFirst)
            if (parent.getChildLayoutPosition(view) == 0)
                outRect.top = space;

    }
}

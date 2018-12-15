package com.kelvingabe.bakerapp.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class RecipeItemDecoration extends RecyclerView.ItemDecoration {
    private final int offset;

    private RecipeItemDecoration(int offset) {
        this.offset = offset;
    }

    public RecipeItemDecoration(Context context, int offsetId) {
        this(context.getResources().getDimensionPixelSize(offsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(offset, offset, offset, offset);
    }
}

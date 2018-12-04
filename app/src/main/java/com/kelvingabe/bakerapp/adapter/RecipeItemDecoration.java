package com.kelvingabe.bakerapp.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


//Item decorator tfo the recipe list recycler view
public class RecipeItemDecoration extends RecyclerView.ItemDecoration {
    //
    private final int mItemOffset;

    private RecipeItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public RecipeItemDecoration(Context context, int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}

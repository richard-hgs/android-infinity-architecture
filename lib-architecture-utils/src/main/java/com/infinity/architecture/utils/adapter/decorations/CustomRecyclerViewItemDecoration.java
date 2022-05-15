package com.infinity.architecture.utils.adapter.decorations;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = "RvItemDecorProd";

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public CustomRecyclerViewItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //RecyclerView.Adapter adapter = parent.getAdapter();

        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        /*int qtdeLinhas = 0;
        int linhaAtual = (int) Math.ceil((float)(position + 1) / spanCount);
        if (adapter != null) {
            qtdeLinhas = (int) Math.ceil((float)(adapter.getItemCount() + 1) / spanCount);

        }*/

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }

            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }

        //Log.d(TAG, "position: " + position + " - Linha:" + linhaAtual + " / " + qtdeLinhas + " Coluna:" + column + " - OuctRect:" + outRect.toString());
    }
}
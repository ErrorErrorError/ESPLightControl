package com.beloo.widget.chipslayoutmanager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;

public class ChildViewsIterable implements Iterable<View> {

    private RecyclerView.LayoutManager layoutManager;

    public ChildViewsIterable(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public Iterator<View> iterator() {
        return new Iterator<View>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < layoutManager.getChildCount();
            }

            @Nullable
            @Override
            public View next() {
                return layoutManager.getChildAt(i++);
            }
        };
    }

    public int size() {
        return layoutManager.getChildCount();
    }
}

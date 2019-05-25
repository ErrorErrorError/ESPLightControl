package com.beloo.widget.chipslayoutmanager.cache;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewCacheFactory {

    private RecyclerView.LayoutManager layoutManager;

    public ViewCacheFactory(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @NonNull
    public IViewCacheStorage createCacheStorage() {
        return new ViewCacheStorage(layoutManager);
    }
}

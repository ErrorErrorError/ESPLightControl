package com.beloo.widget.chipslayoutmanager;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;

interface IChipsLayoutManagerContract extends IPositionsContract, IScrollingContract {
    /** use it to strictly disable scrolling.
     * If scrolling enabled it would be disabled in case all items fit on the screen */
    void setScrollingEnabledContract(boolean isEnabled);
    /**
     * change max count of row views in runtime
     */
    void setMaxViewsInRow(@IntRange(from = 1) Integer maxViewsInRow);

    /** retrieve max views in row settings*/
    @Nullable
    Integer getMaxViewsInRow();

    /** retrieve instantiated row breaker*/
    @NonNull
    IRowBreaker getRowBreaker();

    /** retrieve row strategy type*/
    @RowStrategy
    int getRowStrategyType();

    @Orientation
    /** orientation type of layout manager*/
    int layoutOrientation();

    /** whether or not scrolling disabled outside*/
    boolean isScrollingEnabledContract();
}

package com.beloo.widget.chipslayoutmanager.layouter;

import android.view.View;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.IScrollingController;
import com.beloo.widget.chipslayoutmanager.anchor.AnchorViewState;
import com.beloo.widget.chipslayoutmanager.anchor.IAnchorFactory;
import com.beloo.widget.chipslayoutmanager.anchor.RowsAnchorFactory;
import com.beloo.widget.chipslayoutmanager.gravity.RowGravityModifiersFactory;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.DecoratorBreakerFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.AbstractCriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.ICriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.InfiniteCriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.RowsCriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.placer.IPlacerFactory;
import com.beloo.widget.chipslayoutmanager.util.StateHelper;

public class RowsStateFactory implements IStateFactory {

    private ChipsLayoutManager lm;

    public RowsStateFactory(ChipsLayoutManager lm) {
        this.lm = lm;
    }

    private IOrientationStateFactory createOrientationStateFactory() {
        return lm.isLayoutRTL() ? new RTLRowsOrientationStateFactory() : new LTRRowsOrientationStateFactory();
    }

    @NonNull
    @Override
    public LayouterFactory createLayouterFactory(ICriteriaFactory criteriaFactory, IPlacerFactory placerFactory) {
        IOrientationStateFactory orientationStateFactory = createOrientationStateFactory();

        return new LayouterFactory(lm,
                orientationStateFactory.createLayouterCreator(lm),
                new DecoratorBreakerFactory(
                        lm.getViewPositionsStorage(),
                        lm.getRowBreaker(),
                        lm.getMaxViewsInRow(),
                        orientationStateFactory.createDefaultBreaker()),
                criteriaFactory,
                placerFactory,
                new RowGravityModifiersFactory(),
                orientationStateFactory.createRowStrategyFactory().createRowStrategy(lm.getRowStrategyType()));
    }

    @NonNull
    @Override
    public AbstractCriteriaFactory createDefaultFinishingCriteriaFactory() {
        return StateHelper.isInfinite(this)? new InfiniteCriteriaFactory() : new RowsCriteriaFactory();
    }

    @NonNull
    @Override
    public IAnchorFactory anchorFactory() {
        return new RowsAnchorFactory(lm, lm.getCanvas());
    }

    @Override
    public IScrollingController scrollingController() {
        return lm.verticalScrollingController();
    }

    @NonNull
    @Override
    public ICanvas createCanvas() {
        return new RowSquare(lm);
    }

    @Override
    public int getSizeMode() {
        return lm.getHeightMode();
    }

    @Override
    public int getStart() {
        return 0;
    }

    @Override
    public int getStart(@NonNull View view) {
        return lm.getDecoratedTop(view);
    }

    @Override
    public int getStart(@NonNull AnchorViewState anchor) {
        return anchor.getAnchorViewRect().top;
    }

    @Override
    public int getEnd() {
        return lm.getHeight();
    }

    @Override
    public int getEnd(@NonNull View view) {
        return lm.getDecoratedBottom(view);
    }

    @Override
    public int getEnd(@NonNull AnchorViewState anchor) {
        return anchor.getAnchorViewRect().bottom;
    }

    @Override
    public int getEndViewPosition() {
        return lm.getPosition(lm.getCanvas().getRightView());
    }

    @Override
    public int getStartAfterPadding() {
        return lm.getPaddingTop();
    }

    @Override
    public int getStartViewPosition() {
        return lm.getPosition(lm.getCanvas().getLeftView());
    }

    @Override
    public int getEndAfterPadding() {
        return lm.getHeight() - lm.getPaddingBottom();
    }

    @Override
    public int getStartViewBound() {
        return getStart(lm.getCanvas().getTopView());
    }

    @Override
    public int getEndViewBound() {
        return getEnd(lm.getCanvas().getBottomView());
    }

    @Override
    public int getTotalSpace() {
        return lm.getHeight() - lm.getPaddingTop()
                - lm.getPaddingBottom();
    }
}

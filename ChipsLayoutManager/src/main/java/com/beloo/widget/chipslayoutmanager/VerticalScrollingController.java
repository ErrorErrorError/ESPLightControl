package com.beloo.widget.chipslayoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.anchor.AnchorViewState;
import com.beloo.widget.chipslayoutmanager.layouter.IStateFactory;

class VerticalScrollingController extends ScrollingController implements IScrollingController {

    private ChipsLayoutManager lm;

    VerticalScrollingController(@NonNull ChipsLayoutManager layoutManager, IStateFactory stateFactory, IScrollerListener scrollerListener) {
        super(layoutManager, stateFactory, scrollerListener);
        this.lm = layoutManager;
    }

    @NonNull
    @Override
    public RecyclerView.SmoothScroller createSmoothScroller(@NonNull Context context, final int position, final int timeMs, @NonNull final AnchorViewState anchor) {
        return new LinearSmoothScroller(context) {
            /*
             * LinearSmoothScroller, at a minimum, just need to know the vector
             * (x/y distance) to travel in order to get from the current positioning
             * to the target.
             */
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                int visiblePosition = anchor.getPosition();
                //determine scroll up or scroll down needed
                return new PointF(0, position > visiblePosition ? 1 : -1);
            }

            @Override
            protected void onTargetFound(@NonNull View targetView, RecyclerView.State state, @NonNull Action action) {
                super.onTargetFound(targetView, state, action);
                int desiredTop = lm.getPaddingTop();
                int currentTop = lm.getDecoratedTop(targetView);

                int dy = currentTop - desiredTop;

                //perform fit animation to move target view at top of layout
                action.update(0, dy, timeMs, new LinearInterpolator());
            }
        };
    }

    @Override
    public boolean canScrollVertically() {
        canvas.findBorderViews();
        if (lm.getChildCount() > 0) {
            int top = lm.getDecoratedTop(canvas.getTopView());
            int bottom = lm.getDecoratedBottom(canvas.getBottomView());

            if (canvas.getMinPositionOnScreen() == 0
                    && canvas.getMaxPositionOnScreen() == lm.getItemCount() - 1
                    && top >= lm.getPaddingTop()
                    && bottom <= lm.getHeight() - lm.getPaddingBottom()) {
                return false;
            }
        } else {
            return false;
        }

        return lm.isScrollingEnabledContract();
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    void offsetChildren(int d) {
        lm.offsetChildrenVertical(d);
    }

}

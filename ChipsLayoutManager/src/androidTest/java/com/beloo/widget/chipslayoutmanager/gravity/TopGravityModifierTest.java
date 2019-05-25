package com.beloo.widget.chipslayoutmanager.gravity;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.ParamsType;

import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.beloo.widget.chipslayoutmanager.ParamsType.INVALID;
import static com.beloo.widget.chipslayoutmanager.ParamsType.VALID;

/** test for {@link TopGravityModifier}*/
public class TopGravityModifierTest extends GravityModifierTest {

    @NonNull
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.addAll(Arrays.asList(new Object[][] {
                {VALID, 0, 100, new Rect(0, 20, 0, 100), new Rect(0, 0, 0, 80)},
                {VALID, 0, 100, new Rect(0, 20, 0, 80), new Rect(0, 0, 0, 60)},
                {VALID, 0, 100, new Rect(0, 0, 0, 40), new Rect(0, 0, 0, 40)},
                {VALID, 0, 100, new Rect(0, 10, 0, 20), new Rect(0, 0, 0, 10)},
                {VALID, 0, 100, new Rect(0, 0, 0, 100), new Rect(0, 0, 0, 100)}
        }));
        data.addAll(getInvalidGravityModifierParams());
        return data;
    }

    public TopGravityModifierTest(ParamsType paramsType, int minTop, int maxBottom, Rect testRect, Rect expectedResultRect) {
        super(paramsType, minTop, maxBottom, testRect, expectedResultRect);
    }

    @NonNull
    @Override
    protected IGravityModifier getGravityModifier() {
        return new TopGravityModifier();
    }

    private static Collection<Object[]> getInvalidGravityModifierParams() {
        return Arrays.asList(new Object[][]{
                {INVALID, 0, 100, new Rect(-50, 0, 0, 0), new Rect(0, 0, 0, 0)},
                {INVALID, 20, 100, new Rect(10, 0, 0, 0), new Rect(0, 0, 0, 0)},
                {INVALID, 20, 100, new Rect(20, 0, 120, 0), new Rect(0, 0, 0, 0)}
        });
    }
}

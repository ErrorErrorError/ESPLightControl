package com.beloo.widget.chipslayoutmanager.gravity;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.ParamsType;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.beloo.widget.chipslayoutmanager.ParamsType.VALID;

@RunWith(Parameterized.class)
public class CenterInRowGravityModifierTest extends GravityModifierTest {

    @NonNull
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(Arrays.asList(new Object[][] {
                {VALID, 0, 100, new Rect(0, 20, 0, 100), new Rect(0, 10, 0, 90)},
                {VALID, 0, 100, new Rect(0, 20, 0, 80), new Rect(0, 20, 0, 80)},
                {VALID, 0, 100, new Rect(0, 0, 0, 40), new Rect(0, 30, 0, 70)},
                {VALID, 0, 100, new Rect(0, 10, 0, 20), new Rect(0, 45, 0, 55)},
                {VALID, 0, 100, new Rect(0, 0, 0, 100), new Rect(0, 0, 0, 100)}
        }));
        list.addAll(GravityDataProvider.getInvalidGravityModifierParams());
        return list;
    }

    public CenterInRowGravityModifierTest(ParamsType paramsType, int minTop, int maxBottom, Rect testRect, Rect expectedResultRect) {
        super(paramsType, minTop, maxBottom, testRect, expectedResultRect);
    }

    @NonNull
    @Override
    protected IGravityModifier getGravityModifier() {
        return new CenterInRowGravityModifier();
    }

}

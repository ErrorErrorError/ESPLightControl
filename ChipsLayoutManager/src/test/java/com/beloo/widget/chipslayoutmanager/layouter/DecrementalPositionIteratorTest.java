package com.beloo.widget.chipslayoutmanager.layouter;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DecrementalPositionIteratorTest extends AbstractPositionIteratorTest {

    @NonNull
    @Override
    AbstractPositionIterator providePositionIterator(int maxPosition) {
        return new DecrementalPositionIterator(maxPosition);
    }

    @Test(expected = IllegalStateException.class)
    public void callingNextWhenNegativePositionReachedShouldThrowException() {
        AbstractPositionIterator iterator = providePositionIterator(5);
        assertTrue(iterator.next() == 0);
        iterator.next();
    }

    @Test
    public void nextShouldDecreaseResultPosition() {
        AbstractPositionIterator iterator = providePositionIterator(5);
        iterator.move(3);
        assertTrue(iterator.next() == 3);
        assertTrue(iterator.next() == 2);
    }

    @Test
    public void hasNextShouldReturnTrueIfZeroPositionIsNotPrevious() {
        AbstractPositionIterator iterator = providePositionIterator(2);
        iterator.move(1);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }
}

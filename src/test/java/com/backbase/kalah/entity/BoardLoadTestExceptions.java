package com.backbase.kalah.entity;

import com.backbase.kalah.exceptions.InvalidPitCountException;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BoardLoadTestExceptions {
    enum Type {INVALID_PIT_COUNT_EXCEPTION, NULL_POINTER_EXCEPTION}

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, new int[]{}},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, new int[]{0}},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, new int[]{1, 2, 3, 4, 5}},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, new int[]{1, 2, 3, 4, 5, 6, 7}},

                {Type.NULL_POINTER_EXCEPTION, 1, null},
        });
    }

    @Parameterized.Parameter
    public Type type;

    @Parameterized.Parameter(1)
    public int id;

    @Parameterized.Parameter(2)
    public int[] pits;

    @Test(expected = InvalidPitCountException.class)
    public void testBoardCreationInvalidPitCount() {
        Assume.assumeTrue(type == Type.INVALID_PIT_COUNT_EXCEPTION);
        new Board(id, pits);
    }

    @Test(expected = NullPointerException.class)
    public void testBoardCreationInvalidStoneCount() {
        Assume.assumeTrue(type == Type.NULL_POINTER_EXCEPTION);
        new Board(id, pits);
    }
}

package com.backbase.kalah.entity;

import com.backbase.kalah.exceptions.InvalidPitCountException;
import com.backbase.kalah.exceptions.InvalidStoneCountException;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BoardInitializeTestExceptions {
    enum Type {INVALID_PIT_COUNT_EXCEPTION, INVALID_STONE_COUNT_EXCEPTION}

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, -1, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 0, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 1, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 2, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 3, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 4, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 5, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 7, 6},
                {Type.INVALID_PIT_COUNT_EXCEPTION, 1, 9, 6},

                {Type.INVALID_STONE_COUNT_EXCEPTION, 1, 14, -1},
                {Type.INVALID_STONE_COUNT_EXCEPTION, 1, 14, 0},
        });
    }

    @Parameterized.Parameter
    public Type type;

    @Parameterized.Parameter(1)
    public int id;

    @Parameterized.Parameter(2)
    public int pitCount;

    @Parameterized.Parameter(3)
    public int stoneCount;

    @Test(expected = InvalidPitCountException.class)
    public void testBoardCreationInvalidPitCount() {
        Assume.assumeTrue(type == Type.INVALID_PIT_COUNT_EXCEPTION);
        new Board(id, pitCount, stoneCount);
    }

    @Test(expected = InvalidStoneCountException.class)
    public void testBoardCreationInvalidStoneCount() {
        Assume.assumeTrue(type == Type.INVALID_STONE_COUNT_EXCEPTION);
        new Board(id, pitCount, stoneCount);
    }
}

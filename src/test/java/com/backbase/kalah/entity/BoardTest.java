package com.backbase.kalah.entity;

import com.backbase.kalah.entity.enums.KalahType;
import com.backbase.kalah.entity.enums.PitType;
import com.backbase.kalah.exceptions.InvalidPitCountException;
import com.backbase.kalah.exceptions.InvalidPitIdException;
import com.backbase.kalah.exceptions.InvalidStoneCountException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(1, 14, 6);
    }

    @Test
    public void testBoard() throws InvalidPitCountException, InvalidStoneCountException {
        Board board = new Board(1, 14, 6);
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}, board.stream().toArray());

        board = new Board(1, 6, 2);
        assertArrayEquals(new int[]{2, 2, 0, 2, 2, 0}, board.stream().toArray());
    }

    @Test
    public void stream() {
        assertEquals(14, board.stream().count());
    }

    @Test
    public void getId() {
        assertEquals(1, board.getId());
    }

    @Test
    public void getPitCount() {
        assertEquals(14, board.getPitCount());
    }

    @Test
    public void getPlayer1PitStart() {
        assertEquals(0, board.getPlayer1PitStart());
    }

    @Test
    public void getPlayer1PitEnd() {
        assertEquals(6, board.getPlayer1PitEnd());
    }

    @Test
    public void getPlayer2PitStart() {
        assertEquals(7, board.getPlayer2PitStart());
    }

    @Test
    public void getPlayer2PitEnd() {
        assertEquals(13, board.getPlayer2PitEnd());
    }

    @Test
    public void getPitValue() throws InvalidPitIdException {
        assertEquals(6, board.getPitValue(0));
        assertEquals(6, board.getPitValue(1));
        assertEquals(6, board.getPitValue(2));
        assertEquals(6, board.getPitValue(3));
        assertEquals(6, board.getPitValue(4));
        assertEquals(6, board.getPitValue(5));
        assertEquals(0, board.getPitValue(6));

        assertEquals(6, board.getPitValue(7));
        assertEquals(6, board.getPitValue(8));
        assertEquals(6, board.getPitValue(9));
        assertEquals(6, board.getPitValue(10));
        assertEquals(6, board.getPitValue(11));
        assertEquals(6, board.getPitValue(12));
        assertEquals(0, board.getPitValue(13));

        int exceptionCount = 0;

        try {
            board.getPitValue(-1);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.getPitValue(14);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(2, exceptionCount);
    }

    @Test
    public void getOppositePitId() throws InvalidPitIdException {
        assertEquals(12, board.getOppositePitId(0));
        assertEquals(2, board.getOppositePitId(10));
        assertEquals(5, board.getOppositePitId(7));

        assertEquals(0, board.getOppositePitId(12));
        assertEquals(10, board.getOppositePitId(2));
        assertEquals(7, board.getOppositePitId(5));

        int exceptionCount = 0;

        try {
            board.getOppositePitId(6);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.getOppositePitId(13);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(2, exceptionCount);
    }

    @Test
    public void move() throws InvalidPitIdException {
        //                   0  1  2  3  4  5  6  7  8  9 10 11 12 13
        //                  {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}; // initial values
        int[] moveResult1 = {0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0}; // after move(0)
        int[] moveResult2 = {0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0}; // after move(1)
        int[] moveResult3 = {1, 0, 8, 8, 8, 0, 3, 8, 8, 7, 7, 7, 7, 0}; // after move(5)
        int[] moveResult4 = {2, 1, 9, 8, 8, 0, 3, 8, 0, 8, 8, 8, 8, 1}; // after move(8)

        int lastStonePitId = board.move(0);
        assertEquals(6, lastStonePitId);
        assertArrayEquals(moveResult1, board.stream().toArray());

        lastStonePitId = board.move(1);
        assertEquals(8, lastStonePitId);
        assertArrayEquals(moveResult2, board.stream().toArray());

        lastStonePitId = board.move(5);
        assertEquals(0, lastStonePitId);
        assertArrayEquals(moveResult3, board.stream().toArray());

        lastStonePitId = board.move(8);
        assertEquals(2, lastStonePitId);
        assertArrayEquals(moveResult4, board.stream().toArray());

        int exceptionCount = 0;

        try {
            board.move(5);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.move(6);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.move(8);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.move(13);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(4, exceptionCount);
    }

    @Test
    public void getPitType() throws InvalidPitIdException {
        assertEquals(PitType.PLAYER1_KALAH, board.getPitType(6));
        assertEquals(PitType.PLAYER2_KALAH, board.getPitType(13));

        for (int pitId = 0; pitId < 6; pitId++)
            assertEquals(PitType.PLAYER1_PIT, board.getPitType(pitId));

        for (int pitId = 7; pitId < 13; pitId++)
            assertEquals(PitType.PLAYER2_PIT, board.getPitType(pitId));

        int exceptionCount = 0;

        try {
            board.getPitType(-1);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.getPitType(14);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(2, exceptionCount);
    }

    @Test
    public void moveStonesToKalah() throws InvalidPitIdException {
        board.moveStonesToKalah(0, KalahType.PLAYER1_KALAH);
        assertEquals(0, board.getPitValue(0));
        assertEquals(6, board.getPitValue(6));
        assertEquals(0, board.getPitValue(13));

        board.moveStonesToKalah(0, KalahType.PLAYER1_KALAH);
        assertEquals(0, board.getPitValue(0));
        assertEquals(6, board.getPitValue(6));
        assertEquals(0, board.getPitValue(13));

        board.moveStonesToKalah(1, KalahType.PLAYER2_KALAH);
        assertEquals(0, board.getPitValue(1));
        assertEquals(6, board.getPitValue(6));
        assertEquals(6, board.getPitValue(13));

        board.moveStonesToKalah(0, KalahType.PLAYER2_KALAH);
        assertEquals(0, board.getPitValue(1));
        assertEquals(6, board.getPitValue(6));
        assertEquals(6, board.getPitValue(13));
    }
}
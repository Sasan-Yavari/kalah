package com.backbase.kalah.game;

import com.backbase.kalah.exceptions.InvalidPitIdException;
import com.backbase.kalah.exceptions.InvalidPlayerException;
import com.backbase.kalah.game.enums.GameStatus;
import com.backbase.kalah.game.enums.KalahType;
import com.backbase.kalah.game.enums.PitType;
import com.backbase.kalah.game.enums.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BoardEntityTest {
    private BoardEntity board;

    @Before
    public void setUp() {
        board = new BoardEntity(14, 6);
    }

    @Test
    public void testBoard() {
        BoardEntity boardEntity = new BoardEntity(14, 6);
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}, boardEntity.getPits());

        boardEntity = new BoardEntity(6, 2);
        assertArrayEquals(new int[]{2, 2, 0, 2, 2, 0}, boardEntity.getPits());
    }

    @Test
    public void getPlayer1LastPitIndex() {
        assertEquals(6, board.getPlayer1LastPitIndex());
    }

    @Test
    public void getPlayer2LastPitIndex() {
        assertEquals(13, board.getPlayer2LastPitIndex());
    }

    @Test
    public void getPitValue() {
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
    public void getOppositePitId() {
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
    public void move() {
        //                   0  1  2  3  4  5  6  7  8  9 10 11 12 13
        //                  {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}; // initial values
        int[] moveResult1 = {0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0}; // after move(0)
        int[] moveResult2 = {0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0}; // after move(1)
        int[] moveResult3 = {1, 1, 8, 8, 8, 8, 2, 7, 0, 7, 7, 7, 7, 1}; // after move(8)
        int[] moveResult4 = {2, 1, 8, 8, 8, 0, 3, 8, 1, 8, 8, 8, 8, 1}; // after move(5)

        int lastStonePitId = board.move(0);
        assertEquals(6, lastStonePitId);
        assertEquals(Player.PLAYER_1, board.getNextPlayer());
        assertArrayEquals(moveResult1, board.getPits());

        lastStonePitId = board.move(1);
        board.changeNextPlayer();
        assertEquals(8, lastStonePitId);
        assertEquals(Player.PLAYER_2, board.getNextPlayer());
        assertArrayEquals(moveResult2, board.getPits());

        lastStonePitId = board.move(8);
        board.changeNextPlayer();
        assertEquals(1, lastStonePitId);
        assertEquals(Player.PLAYER_1, board.getNextPlayer());
        assertArrayEquals(moveResult3, board.getPits());

        lastStonePitId = board.move(5);
        board.changeNextPlayer();
        assertEquals(0, lastStonePitId);
        assertEquals(Player.PLAYER_2, board.getNextPlayer());
        assertArrayEquals(moveResult4, board.getPits());

        int exceptionCount = 0;

        try {
            board.move(5);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPlayerException);
            exceptionCount++;
        }

        try {
            board.move(6);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPlayerException);
            exceptionCount++;
        }

        try {
            board.move(13);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(3, exceptionCount);
    }

    @Test
    public void getPitType() {
        assertEquals(PitType.PLAYER_1_KALAH, board.getPitType(6));
        assertEquals(PitType.PLAYER_2_KALAH, board.getPitType(13));

        for (int pitId = 0; pitId < 6; pitId++)
            assertEquals(PitType.PLAYER_1_PIT, board.getPitType(pitId));

        for (int pitId = 7; pitId < 13; pitId++)
            assertEquals(PitType.PLAYER_2_PIT, board.getPitType(pitId));

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
    public void getPitPlayer() {
        for (int pitId = 0; pitId <= 6; pitId++)
            assertEquals(Player.PLAYER_1, board.getPitPlayer(pitId));

        for (int pitId = 7; pitId <= 13; pitId++)
            assertEquals(Player.PLAYER_2, board.getPitPlayer(pitId));

        int exceptionCount = 0;

        try {
            board.getPitPlayer(-1);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        try {
            board.getPitPlayer(14);
        } catch (Exception ex) {
            assertTrue(ex instanceof InvalidPitIdException);
            exceptionCount++;
        }

        assertEquals(2, exceptionCount);
    }

    @Test
    public void moveStonesToKalah() {
        board.moveStonesToKalah(0, KalahType.PLAYER_1_KALAH);
        assertEquals(0, board.getPitValue(0));
        assertEquals(6, board.getPitValue(6));
        assertEquals(0, board.getPitValue(13));

        board.moveStonesToKalah(0, KalahType.PLAYER_1_KALAH);
        assertEquals(0, board.getPitValue(0));
        assertEquals(6, board.getPitValue(6));
        assertEquals(0, board.getPitValue(13));

        board.moveStonesToKalah(1, KalahType.PLAYER_2_KALAH);
        assertEquals(0, board.getPitValue(1));
        assertEquals(6, board.getPitValue(6));
        assertEquals(6, board.getPitValue(13));

        board.moveStonesToKalah(0, KalahType.PLAYER_2_KALAH);
        assertEquals(0, board.getPitValue(1));
        assertEquals(6, board.getPitValue(6));
        assertEquals(6, board.getPitValue(13));
    }

    @Test
    public void hasAnyStone() {
        assertTrue(this.board.hasAnyStone(Player.PLAYER_1));
        assertTrue(this.board.hasAnyStone(Player.PLAYER_2));
    }

    @Test
    public void flushToKalah() {
        board.flushToKalah(Player.PLAYER_1);

        IntStream.range(0, board.getPlayer1LastPitIndex()).forEach(pitId -> assertEquals(0, board.getPitValue(pitId)));
        IntStream.range(board.getPlayer1LastPitIndex() + 1, board.getPlayer2LastPitIndex()).forEach(pitId -> assertEquals(6, board.getPitValue(pitId)));

        assertEquals(36, board.getPitValue(board.getPlayer1LastPitIndex()));
        assertEquals(0, board.getPitValue(board.getPlayer2LastPitIndex()));

        board.flushToKalah(Player.PLAYER_2);

        IntStream.range(0, board.getPlayer1LastPitIndex()).forEach(pitId -> assertEquals(0, board.getPitValue(pitId)));
        IntStream.range(board.getPlayer1LastPitIndex() + 1, board.getPlayer2LastPitIndex()).forEach(pitId -> assertEquals(0, board.getPitValue(pitId)));

        assertEquals(36, board.getPitValue(board.getPlayer1LastPitIndex()));
        assertEquals(36, board.getPitValue(board.getPlayer2LastPitIndex()));
    }

    @Test
    public void getPits() {
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}, board.getPits());
    }

    @Test
    public void getNextPlayer() {
        assertEquals(Player.PLAYER_1, board.getNextPlayer());
    }

    @Test
    public void getStatus() {
        assertEquals(GameStatus.RUNNING, board.getStatus());
    }

    @Test
    public void getWinner() {
        assertEquals(Optional.empty(), board.getWinner());
    }

    @Test
    public void gameOver() {
        board.gameOver();
        assertEquals(GameStatus.GAME_OVER, board.getStatus());
    }

    @Test
    public void setWinner() {
        assertEquals(Optional.empty(), board.getWinner());

        board.setWinner(Player.PLAYER_1);
        assertEquals(Optional.of(Player.PLAYER_1), board.getWinner());

        board.setWinner(Player.PLAYER_2);
        assertEquals(Optional.of(Player.PLAYER_2), board.getWinner());
    }

    @Test
    public void changeNextPlayer() {
        assertEquals(Player.PLAYER_1, board.getNextPlayer());
        board.changeNextPlayer();
        assertEquals(Player.PLAYER_2, board.getNextPlayer());
        board.changeNextPlayer();
        assertEquals(Player.PLAYER_1, board.getNextPlayer());
    }
}
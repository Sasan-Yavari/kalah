package com.backbase.kalah.game;

import com.backbase.kalah.game.enums.GameStatus;
import com.backbase.kalah.game.enums.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ControllerTest {
    private BoardEntity board;
    private Controller controller;

    @Before
    public void setUp() {
        board = new BoardEntity(14, 6);
        controller = new Controller(0, board);
    }

    @Test
    public void getGameId() {
        assertEquals(0, controller.getGameId());
    }

    @Test
    public void streamBoard() {
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}, controller.streamBoard().toArray());
    }

    @Test
    public void move() {
        controller.move(0);
        assertEquals(Player.PLAYER_1, board.getNextPlayer());

        controller.move(1);
        assertEquals(Player.PLAYER_2, board.getNextPlayer());
    }

    @Test
    public void moveToWin() {
        int[] pits = board.getPits();
        int[] beforeWinSituation = {0, 0, 0, 0, 0, 6, 0, 12, 12, 12, 12, 12, 12, 0};
        System.arraycopy(beforeWinSituation, 0, pits, 0, pits.length);
        controller.move(5);
        assertEquals(GameStatus.GAME_OVER, board.getStatus());
        assertEquals(Optional.of(Player.PLAYER_2), board.getWinner());
    }

    @Test
    public void moveToGainOpponentStones() {
        int[] pits = board.getPits();
        int[] beforeGainSituation = {0, 0, 0, 0, 1, 8, 0, 12, 12, 12, 12, 12, 12, 0};
        System.arraycopy(beforeGainSituation, 0, pits, 0, pits.length);
        controller.move(5);
        assertEquals(0, board.getPitValue(0));
        assertEquals(0, board.getPitValue(12));
        assertEquals(15, board.getPitValue(6));
        assertEquals(13, board.getPitValue(7));
        assertEquals(GameStatus.RUNNING, board.getStatus());
        assertEquals(Optional.empty(), board.getWinner());
    }
}
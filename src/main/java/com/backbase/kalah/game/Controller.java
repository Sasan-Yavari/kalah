package com.backbase.kalah.game;

import com.backbase.kalah.exceptions.GameOverException;
import com.backbase.kalah.game.enums.GameStatus;
import com.backbase.kalah.game.enums.PitType;
import com.backbase.kalah.game.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Controller is the Use-Case layer of the application
 */
public class Controller {
    private static final String INFO_TEMPLATE = "[Move] [GameId: %d] [PitId: %d] [Status: %s] [Winner: %s]";

    private Logger logger = LoggerFactory.getLogger(Controller.class);
    private BoardEntity board;
    private int gameId;

    /**
     * Creates a new {@link Controller}
     *
     * @param gameId id of the game
     * @param board  associated {@link BoardEntity}
     */
    Controller(final int gameId, final BoardEntity board) {
        if (board == null)
            throw new NullPointerException("board is null");

        this.gameId = gameId;
        this.board = board;
    }

    /**
     * Returns {@code gameId}
     *
     * @return id of the game
     */
    int getGameId() {
        return gameId;
    }

    /**
     * Streams the pits value of the associated {@link BoardEntity}
     *
     * @return instance of {@link IntStream}
     */
    public IntStream streamBoard() {
        return Arrays.stream(board.getPits());
    }

    /**
     * This is the main method that implements use-cases. Calling this method will cause to
     * a move. Board status will change if everything goes well. Otherwise, proper Exception
     * will be thrown. Application rules are applied in this method.
     *
     * @param pitId id of the pit
     */
    public void move(final int pitId) {
        if (board.getStatus() == GameStatus.GAME_OVER)
            throw new GameOverException("Selected game is over. Winner: " + board.getWinner().map(Enum::name).orElse("Not available"));

        PitType pitType = board.getPitType(pitId);

        int lastPitId = board.move(pitId);
        int lastPitValue = board.getPitValue(lastPitId);
        PitType lastPitType = board.getPitType(lastPitId);

        if (lastPitValue == 1 && lastPitType == pitType) {
            int oppositePitId = board.getOppositePitId(lastPitId);
            int oppositePitValue = board.getPitValue(oppositePitId);

            if (oppositePitValue > 0) {
                board.moveStonesToKalah(lastPitId, pitType.getKalahType());
                board.moveStonesToKalah(oppositePitId, pitType.getKalahType());
            }
        }

        updateTurn(lastPitType);
        updateStatus();

        logger.info(String.format(INFO_TEMPLATE, gameId, pitId, board.getStatus(), board.getWinner().map(Enum::name).orElse("Not available")));
    }

    /**
     * Checks the latest move and decides about changing the next player of the associated board.
     *
     * @param lastPitType {@link PitType} of the last pit of latest move
     */
    private void updateTurn(PitType lastPitType) {
        if ((board.getNextPlayer() != Player.PLAYER_1 || lastPitType != PitType.PLAYER_1_KALAH) &&
                (board.getNextPlayer() != Player.PLAYER_2 || lastPitType != PitType.PLAYER_2_KALAH)) {
            board.changeNextPlayer();
        }
    }

    /**
     * Checks the board and updates the game status and game winner according to it
     */
    private void updateStatus() {
        if (!board.hasAnyStone(Player.PLAYER_1)) {
            board.flushToKalah(Player.PLAYER_2);
            board.gameOver();
        } else if (!board.hasAnyStone(Player.PLAYER_2)) {
            board.flushToKalah(Player.PLAYER_1);
            board.gameOver();
        }

        if (board.getStatus() == GameStatus.GAME_OVER) {
            int player1Score = board.getPitValue(board.getPlayer1LastPitIndex());
            int player2Score = board.getPitValue(board.getPlayer2LastPitIndex());

            if (player1Score > player2Score) {
                board.setWinner(Player.PLAYER_1);
            } else if (player1Score < player2Score) {
                board.setWinner(Player.PLAYER_2);
            }
        }
    }
}

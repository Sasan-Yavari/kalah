package com.backbase.kalah.controller;

import com.backbase.kalah.controller.enums.GameStatus;
import com.backbase.kalah.entity.Board;
import com.backbase.kalah.entity.enums.PitType;
import com.backbase.kalah.entity.enums.PlayerType;
import com.backbase.kalah.exceptions.GameOverException;
import com.backbase.kalah.exceptions.InvalidPlayerException;

import java.io.Serializable;
import java.util.Optional;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private Board board;
    private GameStatus status = GameStatus.RUNNING;
    private PlayerType nextPlayerType;
    private PlayerType winner = null;

    public Game(final Board board) {
        if (board == null)
            throw new NullPointerException("board is null");

        this.board = board;
        this.nextPlayerType = PlayerType.PLAYER_1;
    }

    public void move(final int pitId) {
        if (status == GameStatus.GAME_OVER)
            throw new GameOverException();

        PlayerType playerType = board.getPitPlayer(pitId);

        validatePlayerTurn(playerType);

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

        checkWinner();

        if ((lastPitType != PitType.PLAYER_1_KALAH && lastPitType != PitType.PLAYER_2_KALAH) || (lastPitType.getPlayerType() != playerType))
            nextPlayerType = nextPlayerType.getNext();
    }

    private void validatePlayerTurn(PlayerType playerType) {
        if (!playerType.equals(nextPlayerType))
            throw new InvalidPlayerException(nextPlayerType + "'s turn");
    }

    public Optional<PlayerType> getWinner() {
        return Optional.ofNullable(winner);
    }

    public GameStatus getStatus() {
        return status;
    }

    private void checkWinner() {
        if (!board.hasAnyStone(PlayerType.PLAYER_1)) {
            board.flushToKalah(PlayerType.PLAYER_1);
            status = GameStatus.GAME_OVER;
        } else if (!board.hasAnyStone(PlayerType.PLAYER_2)) {
            board.flushToKalah(PlayerType.PLAYER_2);
            status = GameStatus.GAME_OVER;
        }

        if (status == GameStatus.GAME_OVER) {
            int player1Score = board.getPitValue(board.getPlayer1LastPitIndex());
            int player2Score = board.getPitValue(board.getPlayer2LastPitIndex());

            if (player1Score > player2Score) {
                winner = PlayerType.PLAYER_1;
            } else if (player1Score < player2Score) {
                winner = PlayerType.PLAYER_2;
            }
        }
    }
}

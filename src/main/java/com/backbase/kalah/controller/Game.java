package com.backbase.kalah.controller;

import com.backbase.kalah.entity.Board;
import com.backbase.kalah.entity.enums.PitType;
import com.backbase.kalah.exceptions.InvalidPitIdException;

public class Game {
    private Board board;

    public Game(Board board) {
        if (board == null)
            throw new NullPointerException("board is null");

        this.board = board;
    }

    public void move(final int pitId) throws InvalidPitIdException {
        //TODO: Implement
        PitType startPitType = board.getPitType(pitId);

        int lastPitId = board.move(pitId);
        int lastPitValue = board.getPitValue(lastPitId);
        PitType lastPitType = board.getPitType(lastPitId);

        if (lastPitValue == 1 && lastPitType == startPitType) {
            int oppositePitId = board.getOppositePitId(lastPitId);
            int oppositePitValue = board.getPitValue(oppositePitId);

            if (oppositePitValue > 0) {
                board.moveStonesToKalah(lastPitId, startPitType.getKalahType());
                board.moveStonesToKalah(oppositePitId, startPitType.getKalahType());
            }
        }
    }
}

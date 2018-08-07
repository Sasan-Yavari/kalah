package com.backbase.kalah.entity;

import com.backbase.kalah.entity.enums.KalahType;
import com.backbase.kalah.entity.enums.PitType;
import com.backbase.kalah.exceptions.InvalidPitCountException;
import com.backbase.kalah.exceptions.InvalidPitIdException;
import com.backbase.kalah.exceptions.InvalidStoneCountException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int pitCount;
    private int player1PitStart;
    private int player1PitEnd;
    private int player2PitStart;
    private int player2PitEnd;

    private int[] pits;

    public Board(final int id, final int pitCount, final int stoneCount) throws InvalidPitCountException, InvalidStoneCountException {
        checkPitCount(pitCount);

        if (stoneCount < 1)
            throw new InvalidStoneCountException("stoneCount must be greater than zero");

        this.id = id;
        this.pits = new int[pitCount];

        setBoundaries();

        IntStream.range(0, pitCount)
                .filter(pitId -> pitId != player1PitEnd && pitId != player2PitEnd)
                .forEach(pitId -> pits[pitId] = stoneCount);
    }

    public Board(final int id, final int[] pits) throws InvalidPitCountException {
        if (pits == null)
            throw new NullPointerException("pits is null");

        checkPitCount(pits.length);

        this.id = id;
        this.pits = pits;

        setBoundaries();
    }

    public IntStream stream() {
        return Arrays.stream(pits);
    }

    public int getId() {
        return id;
    }

    public int getPitCount() {
        return pitCount;
    }

    public int getPlayer1PitStart() {
        return player1PitStart;
    }

    public int getPlayer1PitEnd() {
        return player1PitEnd;
    }

    public int getPlayer2PitStart() {
        return player2PitStart;
    }

    public int getPlayer2PitEnd() {
        return player2PitEnd;
    }

    public int getPitValue(final int pitId) throws InvalidPitIdException {
        checkPitId(pitId);
        return pits[pitId];
    }

    public int getOppositePitId(final int pitId) throws InvalidPitIdException {
        checkPitId(pitId);
        PitType type = getPitType(pitId);

        if (type == PitType.PLAYER1_KALAH || type == PitType.PLAYER2_KALAH)
            throw new InvalidPitIdException("Pit(" + pitId + ") is a Kalah");

        return pitCount - 2 - pitId;
    }

    public int move(final int pitId) throws InvalidPitIdException {
        int pitValue = getPitValue(pitId);

        if (pitValue == 0)
            throw new InvalidPitIdException("Pit(" + pitId + ") is empty");

        PitType type = getPitType(pitId);

        if (type == PitType.PLAYER1_KALAH || type == PitType.PLAYER2_KALAH)
            throw new InvalidPitIdException("Pit(" + pitId + ") is a Kalah");

        PitType restrictedType = type == PitType.PLAYER1_PIT ? PitType.PLAYER2_KALAH : PitType.PLAYER1_KALAH;
        int nextPit = pitId;

        pits[pitId] = 0;

        while (pitValue > 0) {
            nextPit++;

            if (nextPit > player2PitEnd)
                nextPit = 0;

            PitType nextPitType = getPitType(nextPit);

            if (nextPitType != restrictedType) {
                pitValue--;
                pits[nextPit]++;
            }
        }

        return nextPit;
    }

    public PitType getPitType(final int pitId) throws InvalidPitIdException {
        if (pitId == player1PitEnd) {
            return PitType.PLAYER1_KALAH;
        } else if (pitId == player2PitEnd) {
            return PitType.PLAYER2_KALAH;
        } else if (pitId >= player1PitStart && pitId < player1PitEnd) {
            return PitType.PLAYER1_PIT;
        } else if (pitId >= player2PitStart && pitId < player2PitEnd) {
            return PitType.PLAYER2_PIT;
        } else throw new InvalidPitIdException("pitId must be between 0 and " + player2PitEnd);
    }

    public void moveStonesToKalah(final int pitId, KalahType kalahType) throws InvalidPitIdException {
        checkPitId(pitId);

        int kalahId = kalahType == KalahType.PLAYER1_KALAH ? player1PitEnd : player2PitEnd;

        pits[kalahId] += pits[pitId];
        pits[pitId] = 0;
    }

    private void checkPitId(final int pitId) throws InvalidPitIdException {
        if (pitId < player1PitStart || pitId > player2PitEnd)
            throw new InvalidPitIdException("pitId must be between 0 and " + player2PitEnd);
    }

    private void checkPitCount(final int pitCount) throws InvalidPitCountException {
        if (pitCount < 6 || pitCount % 2 != 0)
            throw new InvalidPitCountException("pitCount minimum value is 6 and it must be a factor of 2");
    }

    private void setBoundaries() {
        pitCount = pits.length;
        player1PitStart = 0;
        player1PitEnd = pitCount / 2 - 1;
        player2PitStart = player1PitEnd + 1;
        player2PitEnd = pitCount - 1;
    }
}
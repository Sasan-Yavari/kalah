package com.backbase.kalah.entity;

import com.backbase.kalah.entity.enums.KalahType;
import com.backbase.kalah.entity.enums.PitType;
import com.backbase.kalah.entity.enums.PlayerType;
import com.backbase.kalah.exceptions.InvalidPitCountException;
import com.backbase.kalah.exceptions.InvalidPitIdException;
import com.backbase.kalah.exceptions.InvalidStoneCountException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Board is the deepest entity of this program that is not depended on
 * any other entity or layer and it knows just about the business rules.
 *
 * Game rules (Use-Cases) are not controlled here.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int pitCount;
    private int player1LastPitIndex;
    private int player2FirstPitIndex;
    private int player2LastPitIndex;

    private int[] pits;

    public Board(final int id, final int pitCount, final int stoneCount) {
        checkPitCount(pitCount);

        if (stoneCount < 1)
            throw new InvalidStoneCountException("stoneCount must be greater than zero");

        this.id = id;
        this.pits = new int[pitCount];

        setBoundaries();

        IntStream.range(0, pitCount)
                .filter(pitId -> pitId != player1LastPitIndex && pitId != player2LastPitIndex)
                .forEach(pitId -> pits[pitId] = stoneCount);
    }

    public Board(final int id, final int[] pits) {
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

    public int getPlayer1LastPitIndex() {
        return player1LastPitIndex;
    }

    public int getPlayer2FirstPitIndex() {
        return player2FirstPitIndex;
    }

    public int getPlayer2LastPitIndex() {
        return player2LastPitIndex;
    }

    public int getPitValue(final int pitId) {
        checkPitId(pitId);
        return pits[pitId];
    }

    public int getOppositePitId(final int pitId) {
        checkPitId(pitId);
        PitType type = getPitType(pitId);

        if (type == PitType.PLAYER_1_KALAH || type == PitType.PLAYER_2_KALAH)
            throw new InvalidPitIdException("Pit(" + pitId + ") is a Kalah");

        return pitCount - 2 - pitId;
    }

    public int move(final int pitId) {
        int pitValue = getPitValue(pitId);

        if (pitValue == 0)
            throw new InvalidPitIdException("Pit(" + pitId + ") is empty");

        PitType type = getPitType(pitId);

        if (type == PitType.PLAYER_1_KALAH || type == PitType.PLAYER_2_KALAH)
            throw new InvalidPitIdException("Pit(" + pitId + ") is a Kalah");

        PitType restrictedType = type == PitType.PLAYER_1_PIT ? PitType.PLAYER_2_KALAH : PitType.PLAYER_1_KALAH;
        int nextPit = pitId;

        pits[pitId] = 0;

        while (pitValue > 0) {
            nextPit++;

            if (nextPit > player2LastPitIndex)
                nextPit = 0;

            PitType nextPitType = getPitType(nextPit);

            if (nextPitType != restrictedType) {
                pitValue--;
                pits[nextPit]++;
            }
        }

        return nextPit;
    }

    public boolean hasAnyStone(final PlayerType playerType) {
        int start = playerType == PlayerType.PLAYER_1 ? 0 : player2FirstPitIndex;
        int end = playerType == PlayerType.PLAYER_1 ? player1LastPitIndex : player2LastPitIndex;

        return IntStream.range(start, end).anyMatch(pitId -> pits[pitId] > 0);
    }

    public PitType getPitType(final int pitId) {
        if (pitId == player1LastPitIndex) {
            return PitType.PLAYER_1_KALAH;
        } else if (pitId == player2LastPitIndex) {
            return PitType.PLAYER_2_KALAH;
        } else if (pitId >= 0 && pitId < player1LastPitIndex) {
            return PitType.PLAYER_1_PIT;
        } else if (pitId >= player2FirstPitIndex && pitId < player2LastPitIndex) {
            return PitType.PLAYER_2_PIT;
        } else throw new InvalidPitIdException("pitId must be between 0 and " + player2LastPitIndex);
    }

    public PlayerType getPitPlayer(final int pitId) {
        if (pitId >= 0 && pitId <= player1LastPitIndex) {
            return PlayerType.PLAYER_1;
        } else if (pitId >= player2FirstPitIndex && pitId <= player2LastPitIndex) {
            return PlayerType.PLAYER_2;
        } else throw new InvalidPitIdException("pitId must be between 0 and " + player2LastPitIndex);
    }

    public void moveStonesToKalah(final int pitId, KalahType kalahType) {
        checkPitId(pitId);

        int kalahId = kalahType == KalahType.PLAYER_1_KALAH ? player1LastPitIndex : player2LastPitIndex;

        pits[kalahId] += pits[pitId];
        pits[pitId] = 0;
    }

    public void flushToKalah(PlayerType playerType) {
        int start = playerType == PlayerType.PLAYER_1 ? 0 : player2FirstPitIndex;
        int end = playerType == PlayerType.PLAYER_1 ? player1LastPitIndex : player2LastPitIndex;

        IntStream.range(start, end).forEach(pitId -> {
            pits[end] += pits[pitId];
            pits[pitId] = 0;
        });
    }

    private void checkPitId(final int pitId) {
        if (pitId < 0 || pitId > player2LastPitIndex)
            throw new InvalidPitIdException("pitId must be between 0 and " + player2LastPitIndex);
    }

    private void checkPitCount(final int pitCount) {
        if (pitCount < 6 || pitCount % 2 != 0)
            throw new InvalidPitCountException("pitCount minimum value is 6 and it must be a factor of 2");
    }

    private void setBoundaries() {
        pitCount = pits.length;
        player1LastPitIndex = pitCount / 2 - 1;
        player2FirstPitIndex = player1LastPitIndex + 1;
        player2LastPitIndex = pitCount - 1;
    }
}
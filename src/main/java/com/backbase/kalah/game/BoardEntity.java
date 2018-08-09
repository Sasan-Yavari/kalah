package com.backbase.kalah.game;

import com.backbase.kalah.exceptions.InvalidPitCountException;
import com.backbase.kalah.exceptions.InvalidPitIdException;
import com.backbase.kalah.exceptions.InvalidPlayerException;
import com.backbase.kalah.exceptions.InvalidStoneCountException;
import com.backbase.kalah.game.enums.GameStatus;
import com.backbase.kalah.game.enums.KalahType;
import com.backbase.kalah.game.enums.PitType;
import com.backbase.kalah.game.enums.Player;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * BoardEntity is the deepest entity of this program that is not depended on
 * any other entity or layer and it knows just about the business rules.
 * <p>
 * Game rules (Use-Cases) are not controlled here.
 */
class BoardEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pitCount;
    private int player1LastPitIndex;
    private int player2FirstPitIndex;
    private int player2LastPitIndex;

    private int[] pits;

    private GameStatus status = GameStatus.RUNNING;
    private Player nextPlayer;
    private Player winner = null;

    /**
     * Instantiates a new BoardEntity.
     * Throws InvalidPitCountException for pitCounts smaller than 6 and for pitCounts that are not a factor of 2.
     * Throws InvalidStoneCountException if the stoneCount is not greater than zero.
     *
     * @param pitCount   pitCount is a positive number larger than 6 and it must be a factor of 2
     * @param stoneCount stoneCount must be greater than zero"
     */
    BoardEntity(final int pitCount, final int stoneCount) {
        checkPitCount(pitCount);

        if (stoneCount < 1)
            throw new InvalidStoneCountException("stoneCount must be greater than zero");

        this.pits = new int[pitCount];
        this.pitCount = pitCount;
        this.player1LastPitIndex = pitCount / 2 - 1;
        this.player2FirstPitIndex = player1LastPitIndex + 1;
        this.player2LastPitIndex = pitCount - 1;
        this.nextPlayer = Player.PLAYER_1;

        IntStream.range(0, pitCount)
                .filter(pitId -> pitId != player1LastPitIndex && pitId != player2LastPitIndex)
                .forEach(pitId -> pits[pitId] = stoneCount);
    }

    /**
     * Returns the pits array
     *
     * @return an {@code int[]}
     */
    int[] getPits() {
        return pits;
    }

    /**
     * Returns the last pit index for player 1
     *
     * @return value of parameter {@code player1LastPitIndex}
     */
    int getPlayer1LastPitIndex() {
        return player1LastPitIndex;
    }

    /**
     * Returns the last pit index for player 2
     *
     * @return value of parameter {@code player2LastPitIndex}
     */
    int getPlayer2LastPitIndex() {
        return player2LastPitIndex;
    }

    /**
     * Returns the stone count available inside {@code pits[pitId]}
     * Throws InvalidPitIdException if the pitId is not valid.
     *
     * @param pitId id of the pit
     * @return value of the pitId cell from pits array
     */
    int getPitValue(final int pitId) {
        checkPitId(pitId);
        return pits[pitId];
    }

    /**
     * Returns the value of the opposite pit of the pitId.
     * For example in a 14 pit game, the opposite of {@code pits[0]} is {@code pits[12]}. In this case
     * if we call this method with {@code pitId = 0} this method will return the stone count of {@code pits[12]}
     *
     * @param pitId id of the pit
     * @return the value of the opposite pit
     */
    int getOppositePitId(final int pitId) {
        checkPitId(pitId);
        PitType type = getPitType(pitId);

        if (type == PitType.PLAYER_1_KALAH || type == PitType.PLAYER_2_KALAH)
            throw new InvalidPitIdException("Selected pit is a Kalah");

        return pitCount - 2 - pitId;
    }

    /**
     * Removes stones from {@code pits[pitId]} and divides them between next pits.
     * According to business rules, opponents Kalah is restricted in this action and
     * the method will skip apponents Kalah.
     *
     * @param pitId id of the source pit
     * @return the id of the last seeded pit
     */
    int move(final int pitId) {
        PitType type = getPitType(pitId);
        int pitValue = getPitValue(pitId);

        if (type.getPlayer() != nextPlayer) {
            throw new InvalidPlayerException(nextPlayer + "'s turn");
        }

        if (type == PitType.PLAYER_1_KALAH || type == PitType.PLAYER_2_KALAH) {
            throw new InvalidPitIdException("Selected pit is a Kalah");
        }

        if (pitValue == 0) {
            throw new InvalidPitIdException("Selected pit is empty");
        }


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

    /**
     * @param player player
     * @return true if the player has at least one stone in one of his pits
     */
    boolean hasAnyStone(final Player player) {
        int start = player == Player.PLAYER_1 ? 0 : player2FirstPitIndex;
        int end = player == Player.PLAYER_1 ? player1LastPitIndex : player2LastPitIndex;

        return IntStream.range(start, end).anyMatch(pitId -> pits[pitId] > 0);
    }

    /**
     * @param pitId id of the pit
     * @return {@link Player} that is the owner of the given pitId
     */
    Player getPitPlayer(final int pitId) {
        if (pitId >= 0 && pitId <= player1LastPitIndex) {
            return Player.PLAYER_1;
        } else if (pitId >= player2FirstPitIndex && pitId <= player2LastPitIndex) {
            return Player.PLAYER_2;
        } else throw new InvalidPitIdException("pitId must be between 0 and " + player2LastPitIndex);
    }

    /**
     * @return {@link Player} that must play the next move
     */
    Player getNextPlayer() {
        return nextPlayer;
    }

    /**
     * Returns the type of given pit.
     *
     * @param pitId id of the pit
     * @return {@link PitType}
     */
    PitType getPitType(final int pitId) {
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

    /**
     * Returns the current status of the game
     *
     * @return {@link GameStatus}
     */
    GameStatus getStatus() {
        return status;
    }

    /**
     * Returns the winner of the game
     *
     * @return {@link Optional<Player>}
     */
    Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    /**
     * Moves all stones from {@code pits[pitId]} to given Kalah
     *
     * @param pitId     id of the source pit
     * @param kalahType type of destination Kalah
     */
    void moveStonesToKalah(final int pitId, final KalahType kalahType) {
        checkPitId(pitId);

        int kalahId = kalahType == KalahType.PLAYER_1_KALAH ? player1LastPitIndex : player2LastPitIndex;

        pits[kalahId] += pits[pitId];
        pits[pitId] = 0;
    }

    /**
     * Flushes all stones of the given player to his Kalah
     *
     * @param player the source player
     */
    void flushToKalah(final Player player) {
        int start = player == Player.PLAYER_1 ? 0 : player2FirstPitIndex;
        int end = player == Player.PLAYER_1 ? player1LastPitIndex : player2LastPitIndex;

        IntStream.range(start, end).forEach(pitId -> {
            pits[end] += pits[pitId];
            pits[pitId] = 0;
        });
    }

    /**
     * Sets game status to {@code GameStatus.GAME_OVER}
     */
    void gameOver() {
        this.status = GameStatus.GAME_OVER;
    }

    /**
     * Sets game winner
     *
     * @param winner winner of the game
     */
    void setWinner(final Player winner) {
        this.winner = winner;
    }

    /**
     * Changes the next player
     */
    void changeNextPlayer() {
        this.nextPlayer = nextPlayer.getNext();
    }

    /**
     * Checks and validates the given pitId. Throws InvalidPitIdException in case of any violations.
     * pitId must be between 0 and {@code player2LastPitIndex}
     *
     * @param pitId id of the pit
     */
    private void checkPitId(final int pitId) {
        if (pitId < 0 || pitId > player2LastPitIndex)
            throw new InvalidPitIdException("pitId must be between 0 and " + player2LastPitIndex);
    }

    /**
     * Checks and validates the given pitCount. Throws InvalidPitCountException in case of any violations.
     * pitCount minimum value is 6 and it must be a factor of 2
     *
     * @param pitCount the pitCount
     */
    private void checkPitCount(final int pitCount) {
        if (pitCount < 6 || pitCount % 2 != 0)
            throw new InvalidPitCountException("pitCount minimum value is 6 and it must be a factor of 2");
    }
}
package com.backbase.kalah.entity.enums;

public enum PitType {
    PLAYER_1_PIT(KalahType.PLAYER_1_KALAH, PlayerType.PLAYER_1),
    PLAYER_2_PIT(KalahType.PLAYER_2_KALAH, PlayerType.PLAYER_2),
    PLAYER_1_KALAH(KalahType.PLAYER_1_KALAH, PlayerType.PLAYER_1),
    PLAYER_2_KALAH(KalahType.PLAYER_2_KALAH, PlayerType.PLAYER_2);

    private PlayerType playerType;
    private KalahType kalahType;

    PitType(KalahType kalahType, PlayerType playerType) {
        this.kalahType = kalahType;
        this.playerType = playerType;
    }

    public KalahType getKalahType() {
        return kalahType;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }
}

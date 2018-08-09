package com.backbase.kalah.game.enums;

public enum PitType {
    PLAYER_1_PIT(KalahType.PLAYER_1_KALAH, Player.PLAYER_1),
    PLAYER_2_PIT(KalahType.PLAYER_2_KALAH, Player.PLAYER_2),
    PLAYER_1_KALAH(KalahType.PLAYER_1_KALAH, Player.PLAYER_1),
    PLAYER_2_KALAH(KalahType.PLAYER_2_KALAH, Player.PLAYER_2);

    private KalahType kalahType;
    private Player player;

    PitType(KalahType kalahType, Player player) {
        this.kalahType = kalahType;
        this.player = player;
    }

    public KalahType getKalahType() {
        return kalahType;
    }

    public Player getPlayer() {
        return player;
    }
}

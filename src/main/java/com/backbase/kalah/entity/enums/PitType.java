package com.backbase.kalah.entity.enums;

public enum PitType {
    PLAYER1_PIT(KalahType.PLAYER1_KALAH),
    PLAYER2_PIT(KalahType.PLAYER2_KALAH),
    PLAYER1_KALAH(KalahType.PLAYER1_KALAH),
    PLAYER2_KALAH(KalahType.PLAYER2_KALAH);

    private KalahType kalahType;

    PitType(KalahType kalahType) {
        this.kalahType = kalahType;
    }

    public KalahType getKalahType() {
        return kalahType;
    }
}

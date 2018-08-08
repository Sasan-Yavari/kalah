package com.backbase.kalah.entity.enums;

public enum PlayerType {
    PLAYER_1,
    PLAYER_2;

    public PlayerType getNext() {
        return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }
}

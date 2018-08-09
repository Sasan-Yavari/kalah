package com.backbase.kalah.game.enums;

public enum Player {
    PLAYER_1,
    PLAYER_2;

    public Player getNext() {
        return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }
}

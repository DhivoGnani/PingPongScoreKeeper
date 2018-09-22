package me.dhivo.android.pingpongscorekeeper.components;

public enum PlayerEnum {
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    private final String text;

    PlayerEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

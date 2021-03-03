package sk.tuke.gamestudio.game.pexeso.core;

public class Tile {
    private TileState state;
    private char value;

    public Tile ( char value ) {
        this.state = TileState.CLOSED;
        this.value = value;
    }

    public void setValue ( char value ) {
        this.value = value;
    }

    public char getValue () {
        return this.value;
    }

    public TileState getState () {
        return this.state;
    }

    void setState ( TileState state ) {
        this.state = state;
    }
}

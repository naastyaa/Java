package sk.tuke.gamestudio.game.pexeso.core;

import java.util.Arrays;
import java.util.Random;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final int size;
    private final Tile[][] tiles;
    private GameState state;
    private int numberOfOpenTiles;
    private long startMillis;
    private Tile firstOpenTile;
    private Tile secondOpenTile;


    public Field ( int rowCount , int columnCount ) {
        this.state = GameState.PLAYING;
        this.numberOfOpenTiles = 0;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.size = this.rowCount * this.columnCount;
        if (this.size % 2 == 1) {
            throw new IllegalArgumentException ( "Odd number" );
        } else {
            this.tiles = new Tile[rowCount][columnCount];
            this.generate ();
        }
    }

    private void generate () {
        this.fillWithCards ();
        this.randomize ();
        this.startMillis = System.currentTimeMillis ();
    }

    private void fillWithCards () {
        int maximum = this.rowCount * this.columnCount;
        int count = 0;
        if (maximum % 2 != 0) {
            --maximum;
        }

        char letter = 'a';

        for (int row = 0; row < this.rowCount; ++row) {
            for (int column = 0; column < this.columnCount; ++column) {
                if (count == maximum / 2) {
                    letter = 'a';
                }

                this.tiles[row][column] = new Tile ( letter );
                this.tiles[row][column].setState ( TileState.CLOSED );
                ++count;
                ++letter;
            }
        }

    }


    private void randomize () {
        //int rounds = true;
        Random r = new Random ();

        for (int i = 0; i < 100; ++i) {
            int[] t1;
            int[] t2;
            t1 = this.getRandomTile ( r );
            t2 = this.getRandomTile ( r );
            this.swapTiles ( t1[0] , t1[1] , t2[0] , t2[1] );
        }

    }

    private int[] getRandomTile ( Random r ) {
        return new int[]{r.nextInt ( this.rowCount ) , r.nextInt ( this.columnCount )};
    }

    private void swapTiles ( int row1 , int column1 , int row2 , int column2 ) {
        Tile tempTile = this.getTile ( row1 , column1 );
        this.tiles[row1][column1] = this.tiles[row2][column2];
        this.tiles[row2][column2] = tempTile;
    }

    public int getRowCount () {
        return this.rowCount;
    }

    public int getColumnCount () {
        return this.columnCount;
    }

    public GameState getState () {
        return this.state;
    }

    public Tile getTile ( int row , int column ) {
        return this.tiles[row][column];
    }

    public void openTile ( int row , int column ) {
        if (this.state == GameState.PLAYING) {
            Tile tile = this.tiles[row][column];
            if (tile.getState () == TileState.CLOSED) {
                tile.setState ( TileState.OPEN );
                ++this.numberOfOpenTiles;
                if (this.firstOpenTile != null && this.secondOpenTile != null) {
                    this.firstOpenTile.setState ( TileState.CLOSED );
                    this.secondOpenTile.setState ( TileState.CLOSED );
                    this.firstOpenTile = null;
                    this.secondOpenTile = null;
                }

                if (this.firstOpenTile == null) {
                    this.firstOpenTile = tile;
                } else if (this.firstOpenTile.getValue () == tile.getValue ()) {
                    this.firstOpenTile.setState ( TileState.FIXED );
                    tile.setState ( TileState.FIXED );
                    this.firstOpenTile = null;
                } else {
                    this.numberOfOpenTiles -= 2;
                    this.secondOpenTile = tile;
                }

                if (this.isSolved ()) {
                    this.state = GameState.SOLVED;
                }
            }
        }

    }

    private boolean isSolved () {
        return this.size == this.numberOfOpenTiles;
    }

    private int getPlayingTime () {
        return (int) (System.currentTimeMillis () - this.startMillis) / 1000;
    }

    public int getScore () {
        return this.rowCount * this.columnCount * 30 - this.getPlayingTime () ;
    }



}


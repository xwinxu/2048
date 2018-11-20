package com.example.shume.game2048;

import android.service.quicksettings.Tile;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The 2048 board.
 */
public class Board2048 extends Observable implements Serializable, Iterable<Tile2048> {

    /**
     * The number of rows in Board.
     */
    static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    static int NUM_COLS = 4;

    /**
     * The tiles on the board in row-major order.
     */
    public Tile2048[][] tiles = new Tile2048[NUM_ROWS][NUM_COLS];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    Board2048(List<Tile2048> tiles) {
//        Iterator<Tile2048> tIterator = tiles.iterator();
//
//        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
//            for (int col = 0; col != Board2048.NUM_COLS; col++) {
//                Tile2048 tile = tIterator.next();
//                this.tiles[row][col] = tile;
//            }
//        }

        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
            for (int col = 0; col != Board2048.NUM_COLS; col++) {
                Tile2048 tile = new Tile2048(-1);
                this.tiles[row][col] = tile;
            }
        }
        spawnTile();
        spawnTile();
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        return NUM_COLS * NUM_ROWS;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile2048 getTile(int row, int col) {
        return tiles[row][col];
    }

    public void mergeLeft() {
        pushLeft();
        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
            for (int col = 1; col != Board2048.NUM_COLS; col++) {
                Tile2048 prevTile = tiles[row][col - 1];
                Tile2048 currTile = tiles[row][col];
                if (prevTile.getBackground() == currTile.getBackground() & prevTile.getBackground() != -1) {
                    tiles[row][col - 1] = new Tile2048(2*prevTile.getBackground());
                    tiles[row][col] = new Tile2048(-1);
                }
            }
        }
        pushLeft();
        spawnTile();
    }

    private void pushLeft() {
        for (int row = 0; row < Board2048.NUM_ROWS; row++) {
            int farthest = 0;
            for (int col = 0; col < Board2048.NUM_COLS; col++) {
                if (tiles[row][col].getBackground() != -1) {
                    Tile2048 tempTile = tiles[row][farthest];
                    tiles[row][farthest] = tiles[row][col];
                    tiles[row][col] = tempTile;
                    farthest++;
                }
            }
        }
    }

    // TODO: Complete mergeRight()
    public void mergeRight() {
        pushRight();
        for (int row = 0; row < Board2048.NUM_ROWS; row++) {
            for (int col = tiles[row].length - 2; col >= 0; col--) {
                Tile2048 prevTile = tiles[row][col + 1];
                Tile2048 currTile = tiles[row][col];
                if (prevTile.getBackground() == currTile.getBackground() & prevTile.getBackground() != -1) {
                    tiles[row][col + 1] = new Tile2048(2*prevTile.getBackground());
                    tiles[row][col] = new Tile2048(-1);
                }
            }
        }
        pushRight();
        spawnTile();
    }

    private void pushRight() {
        for (int row = 0; row < Board2048.NUM_ROWS; row++) {
            int farthest = tiles[row].length;
            for (int col = tiles[row].length - 1; col >= 0; col--) {
                if (tiles[row][col].getBackground() != -1) {
                    Tile2048 tempTile = tiles[row][farthest];
                    tiles[row][farthest] = tiles[row][col];
                    tiles[row][col] = tempTile;
                    farthest--;
                }
            }
        }
    }

    // TODO: Complete mergeUp()
    public void mergeUp() {
        pushUp();
        for (int col = 0; col < Board2048.NUM_COLS; col++) {
            for (int row = 1; row < Board2048.NUM_ROWS; row++) {
                Tile2048 prevTile = tiles[row - 1][col];
                Tile2048 currTile = tiles[row][col];
                if (prevTile.getBackground() == currTile.getBackground() & prevTile.getBackground() != -1) {
                    tiles[row - 1][col] = new Tile2048(2*prevTile.getBackground());
                    tiles[row][col] = new Tile2048(-1);
                }
            }
        }
        pushUp();
        spawnTile();
    }

    private void pushUp() {
        for (int col = 0; col < Board2048.NUM_COLS; col++) {
            int farthest = 0;
            for (int row = 0; row < Board2048.NUM_ROWS; row++) {
                if (tiles[row][col].getBackground() != -1) {
                    Tile2048 tempTile = tiles[row][farthest];
                    tiles[row][farthest] = tiles[row][col];
                    tiles[row][col] = tempTile;
                    farthest++;
                }
            }
        }
    }

    // TODO: Complete mergeDown()
    public void mergeDown() {
        pushDown();
        for (int col = 0; col < Board2048.NUM_COLS; col++) {
            for (int row = Board2048.NUM_ROWS - 2; row >= 0; row--) {
                Tile2048 prevTile = tiles[row + 1][col];
                Tile2048 currTile = tiles[row][col];
                if (prevTile.getBackground() == currTile.getBackground() & prevTile.getBackground() != -1) {
                    tiles[row + 1][col] = new Tile2048(2*prevTile.getBackground());
                    tiles[row][col] = new Tile2048(-1);
                }
            }
        }
        pushDown();
        spawnTile();
    }

    private void pushDown() {
        for (int col = 0; col < Board2048.NUM_COLS; col++) {
            int farthest = Board2048.NUM_ROWS - 1;
            for (int row = Board2048.NUM_ROWS - 1; row >= 0; row--) {
                if (tiles[row][col].getBackground() != -1) {
                    Tile2048 tempTile = tiles[row][farthest];
                    tiles[row][farthest] = tiles[row][col];
                    tiles[row][col] = tempTile;
                    farthest--;
                }
            }
        }
    }

    private void spawnTile() {
        ArrayList<int[]> emptySpots = getEmptySpots();
        double ran = Math.random();
        Tile2048 newTile = ran >= 0.8 ? new Tile2048(4) : new Tile2048(2);
        int ranIndex = new Random().nextInt(emptySpots.size());
        this.tiles[emptySpots.get(ranIndex)[0]][emptySpots.get(ranIndex)[1]] = newTile;
    }

    private ArrayList<int[]> getEmptySpots() {
        ArrayList empty = new ArrayList();
        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
            for (int col = 0; col != Board2048.NUM_COLS; col++) {
                if (this.tiles[row][col].getBackground() == -1) {
                    int[] pos = {row, col};
                    empty.add(pos);
                }
            }
        }
        return empty;
    }

    public boolean gameWon() {
        boolean won = false;
        outer:
        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
            for (int col = 0; col != Board2048.NUM_COLS; col++) {
                if (tiles[row][col].getBackground() >= 2048) {
                    won = true;
                    break outer;
                }
            }
        }
        return won;
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * Return an iterator for this Board.
     *
     * @return iterator for this Board.
     */
    @NonNull
    public Iterator<Tile2048> iterator() {
        return new TileIterator(this.tiles);
    }

    /**
     * Iterator for Board. Iterates over the tiles on the board.
     */
    public class TileIterator implements Iterator<Tile2048> {

        private int row;

        private int col;

        private Tile2048[][] tiles;

        TileIterator(Tile2048[][] tiles) {
            this.row = 0;
            this.col = 0;
            this.tiles = tiles;
        }

        @Override
        public boolean hasNext() {return row < Board2048.NUM_ROWS - 1 & col < Board2048.NUM_COLS - 1;}

        @Override
        public Tile2048 next() {
            Tile2048 returnTile = this.tiles[row][col];
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else if (col == Board2048.NUM_COLS) {
                row++;
                col = 0;
            } else {
                col++;
            }
            return returnTile;
        }
    }
}

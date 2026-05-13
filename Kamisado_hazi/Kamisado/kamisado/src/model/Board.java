package model;

import javax.swing.*;

/**
 * A játéktábla modellje.
 * Egy 8x8-as mátrixot tartalmaz, ahol minden mező egy ikon (torony) vagy üres lehet.
 * Csak az adat tárolásáért felel, nem tartalmaz játéklogikát.
 */
public class Board {

    // A tábla mérete (Kamisado szabvány)
    public static final int SIZE = 8;

    // A tábla maga: 8x8 ikon vagy null
    private final Icon[][] board = new Icon[SIZE][SIZE];

    /**
     * Ellenőrzi, hogy a megadott sor és oszlop index a táblán belül van-e.
     */
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    /**
     * Visszaadja a mező tartalmát (ikon vagy null).
     */
    public Icon get(int r, int c) {
        return board[r][c];
    }

    /**
     * Megmondja, hogy a megadott mező üres-e.
     */
    public boolean isEmpty(int r, int c) {
        return board[r][c] == null;
    }

    /**
     * Beállítja a mező tartalmát.
     * Ezt használja a GameLogic minden lépéskor.
     */
    public void set(int r, int c, Icon icon) {
        board[r][c] = icon;
    }

    /**
     * A teljes tábla kiürítése új kör indításakor.
     */
    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c] = null;
            }
        }
    }
}

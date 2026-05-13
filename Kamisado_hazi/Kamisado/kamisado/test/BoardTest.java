package kamisado.test;

import model.Board;
import org.junit.jupiter.api.Test;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Board osztály működését teszteli:
 *  - üres tábla létrejötte
 *  - ikonok beállítása és kiolvasása
 *  - tábla törlése
 */
public class BoardTest {

    /**
     * Ellenőrzi, hogy az új Board minden mezője null.
     */
    @Test
    void testBoardStartsEmpty() {
        Board b = new Board();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                assertNull(b.get(r, c), "A kezdő tábla mezőjének null-nak kell lennie.");
            }
        }
    }

    /**
     * Ellenőrzi, hogy a set() ténylegesen beállítja
     * és a get() visszaadja ugyanazt az ikont.
     */
    @Test
    void testSetAndGet() {
        Board b = new Board();
        Icon icon = new ImageIcon();
        b.set(3, 4, icon);
        assertEquals(icon, b.get(3, 4), "A set után a get-nek ugyanazt az ikont kell adnia.");
    }

    /**
     * Ellenőrzi, hogy a clear() minden mezőt null-ra állít.
     */
    @Test
    void testClearBoard() {
        Board b = new Board();
        b.set(1, 1, new ImageIcon());
        b.set(2, 2, new ImageIcon());
        b.clear();

        assertNull(b.get(1, 1), "Clear után null kell legyen.");
        assertNull(b.get(2, 2), "Clear után null kell legyen.");
    }
}

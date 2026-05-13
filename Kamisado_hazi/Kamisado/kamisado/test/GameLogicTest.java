package kamisado.test;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

public class GameLogicTest {

    /** Kezdőállapot ellenőrzése */
    @Test
    void testInitialState() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        assertFalse(logic.isDarkTurn(), "Világosnak kell kezdenie");
        assertNull(logic.getRequiredColor(), "Kezdéskor nincs kötelező szín");
    }

    /** canMove alap logika tesztelése (világos egyenesen előre léphet) */
    @Test
    void testCanMoveForwardLight() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        // Tegyük le egy másik bábut a világos elé, hogy blokkolja
        logic.getBoard().set(6, 0, GameAssets.load("b_blue", 70)); 

        // Most világosnak NEM szabad tudni lépni
        assertFalse(logic.canMove(7, 0, 6, 0));
    }


    /** Sötét előre léphet (ha lerakjuk kézzel) */
    @Test
    void testCanMoveForwardDarkAfterTurnSwitch() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        logic.afterMoveSetNext("blue"); // váltás → sötét következik

        // Sötét bábu vezérlése (0,2) → (1,2) akkor léphetne, ha üres lenne
        logic.getBoard().set(1, 2, null);  // Kiürítjük manuálisan

        assertTrue(logic.canMove(0, 2, 1, 2), "Sötétnek előre kell tudnia lépni");
    }

    /** Kényszínszín jól állítódik be */
    @Test
    void testRequiredColorAfterMove() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        // Világos lép egy bábuval egy sárga mezőre → következő szín "yellow"
        logic.getBoard().set(6, 3, null);  // célmező üres
        logic.move(7, 3, 6, 3);
        logic.afterMoveSetNext("yellow");

        assertEquals("yellow", logic.getRequiredColor());
        assertTrue(logic.isDarkTurn(), "Lépés után sötét következik");
    }

    /** Győzelem felismerése */
    @Test
    void testCheckWin() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        // Tegyünk egy világos bábut a felső sorba (r=0)
    Icon icon = logic.getBoard().get(7, 0);
        logic.getBoard().set(7, 0, null);
        logic.getBoard().set(0, 0, icon);

        assertTrue(logic.checkWin(0, 0), "Világos győzött volna");
    }

    /** Pontnövelés működik */
    @Test
    void testAwardPoint() {
        GameLogic logic = new GameLogic();
        logic.awardPointToWinner();  // világos kezd → világos kap pontot
        assertEquals(1, logic.getLightScore());
        assertEquals(0, logic.getDarkScore());
    }

    /** Nincs lépés → hasRequiredPieceMove false */
    @Test
    void testNoRequiredPieceMove() {
        GameLogic logic = new GameLogic();
        logic.setupInitialTowers();

        // Világos kezd, kényszínszínre beállítjuk pl. brown
        logic.afterMoveSetNext("brown"); // sötét következik, brown-t kell lépnie

        // Megkeressük a sötét barna bábut → könnyen megtalálható (0. sor)
        // Szándékosan körbezárjuk, hogy ne tudjon lépni
        Board b = logic.getBoard();
        b.set(0, 1, GameAssets.load("b_brown", 70));

        // körbezárás:
        b.set(1, 1, new ImageIcon()); // akadály
        b.set(1, 0, new ImageIcon());
        b.set(1, 2, new ImageIcon());

        assertFalse(logic.hasRequiredPieceMove(),
                "Ha a kényszerített bábu nem tud lépni, akkor false");
    }
}

package model;

import javax.swing.*;
import java.awt.*;

/**
 * Segédosztály képek (ikonok) betöltéséhez és méretezéséhez.
 *
 * A bábuk képfájljai a /ui/img/ mappában találhatók.
 * Az ikonokhoz egy leírás (desc) is tartozik, pl.: "b_blue" vagy "w_red".
 */
public class GameAssets {

    /**
     * Betölti és átméretezi a megadott nevű PNG ikont.
     *
     * @param desc  a képfájl neve kiterjesztés nélkül (pl. "b_blue")
     * @param size  az ikon mérete (négyzetes)
     * @return      az elkészült ImageIcon, vagy null ha a fájl nem található
     */
    public static ImageIcon load(String desc, int size) {
        String filename = desc + ".png";
        var url = GameAssets.class.getResource("/ui/img/" + filename);

        // Ha a kép nem található, nincs ikon (hiba nélkül lépünk tovább)
        if (url == null) return null;

        // Kép betöltése + átméretezése
        Image img = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);

        // Visszaadjuk az ikont a leírás megőrzésével (desc fontos játéklogikában!)
        return new ImageIcon(img, desc);
    }
}

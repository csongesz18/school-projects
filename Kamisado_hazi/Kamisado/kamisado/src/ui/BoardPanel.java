package ui;

import model.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardPanel extends JPanel {

    private final JButton[][] cells;     // A tábla mezőit megjelenítő gombok
    private final ImageIcon dotIcon;     // A léphető helyeket jelölő kis pötty ikon
    private final int size;              // Tábla mérete (tipikusan 8)

    public BoardPanel(String[][] colors, ImageIcon dotIcon) {
        this.size = colors.length;
        this.dotIcon = dotIcon;
        this.cells = new JButton[size][size];

        setLayout(new GridLayout(size, size)); // Rácsos elrendezés

        // A mezők létrehozása és színezése
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton btn = new JButton();
                btn.setOpaque(true);
                btn.setBackground(ColorUtil.getColorFromName(colors[r][c])); // Mező háttérszín a színmátrix alapján
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                cells[r][c] = btn;
                add(btn);
            }
        }
    }

    /**
     * Kattintás esemény felvétele minden mezőre.
     * A listener megkapja a mező sorát és oszlopát.
     */
    public void addCellClickListener(java.util.function.BiConsumer<Integer, Integer> listener) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                final int rr = r, cc = c; // szükséges, mert lambda használja
                cells[r][c].addActionListener(e -> listener.accept(rr, cc));
            }
        }
    }

    /**
     * Frissíti az ikonokat a táblán a Board objektum alapján.
     */
    public void refresh(Board board) {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                cells[r][c].setIcon(board.get(r, c));
    }

    /**
     * Minden kiemelést és pöttyöt eltávolít a tábláról.
     */
    public void clearHighlights(Board board) {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++) {
                cells[r][c].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                cells[r][c].setIcon(board.get(r, c));
            }
    }

    /**
     * A kiválasztott bábu mezőjének megjelölése vastag fekete kerettel.
     */
    public void highlightSelected(int r, int c) {
        cells[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
    }

    /**
     * Egy mező megjelölése pöttyel (léphető hely).
     * Ha van ikon a mezőn, rárajzoljuk a pöttyöt. Ha nincs, simán pötty ikont rakunk.
     */
    public void highlightMove(int r, int c) {
        Icon base = cells[r][c].getIcon();

        // Ha nincs ikon, csak pöttyöt teszünk rá
        if (!(base instanceof ImageIcon orig)) {
            cells[r][c].setIcon(dotIcon);
            return;
        }

        // Pötty rárajzolása a bábu ikonra
        Image bg = orig.getImage();
        Image dot = dotIcon.getImage();
        Image combined = new BufferedImage(bg.getWidth(null), bg.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(bg, 0, 0, null);
        g.drawImage(dot, (bg.getWidth(null)-dot.getWidth(null))/2, (bg.getHeight(null)-dot.getHeight(null))/2, null);
        g.dispose();

        cells[r][c].setIcon(new ImageIcon(combined, orig.getDescription()));
    }
}

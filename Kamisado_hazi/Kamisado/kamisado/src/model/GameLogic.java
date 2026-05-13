package model;

import javax.swing.*;

/**
 * A játék fő logikáját megvalósító osztály.
 * Feladata a bábuk mozgatása, a kényszínszín kezelése,
 * a játékosváltás, a pontszámok és győzelmek kezelése.
 *
 * Megjelenítést nem végez → ezt a GameGui és a BoardPanel intézi.
 */
public class GameLogic {

    private final Board board = new Board();   // a tábla aktuális állapota

    private boolean darkTurn = false;          // false = világos kezd
    private String requiredColor = null;       // a következő kör kötelező színe

    private int darkScore = 0;
    private int lightScore = 0;

    // ---- GETTEREK A GUI SZÁMÁRA ----
    public Board getBoard() { return board; }
    public boolean isDarkTurn() { return darkTurn; }
    public String getRequiredColor() { return requiredColor; }
    public int getDarkScore() { return darkScore; }
    public int getLightScore() { return lightScore; }

    /**
     * Eldönti, hogy kijelölhető-e egy bábu.
     * Csak a soron következő játékos bábuját lehet választani,
     * és ha kényszínszín van, akkor csak a megfelelő színűt.
     */
    public boolean canSelect(ImageIcon icon) {
        if (icon == null) return false;
        String desc = icon.getDescription();
        if (desc == null || desc.length() < 3) return false;

        boolean isDarkPiece = desc.startsWith("b_");
        if (isDarkPiece != darkTurn) return false;

        if (requiredColor == null) return true;
        return requiredColor.equals(desc.substring(2));
    }

    /** Pontszámok kézi beállítása (betöltéshez). */
    public void setScores(int d, int l) {
        darkScore = d;
        lightScore = l;
    }

    /**
     * Lépés érvényességének ellenőrzése.
     * Csak előre vagy átlósan előre lehet menni,
     * az út nem tartalmazhat másik bábut, és a célmező üres kell legyen.
     */
    public boolean canMove(int sr, int sc, int tr, int tc) {
        if (!board.inBounds(sr, sc) || !board.inBounds(tr, tc)) return false;
        if (!board.isEmpty(tr, tc)) return false;

        int dir = darkTurn ? +1 : -1;   // sötét lefele, világos felfele lép
        int dr = tr - sr;
        int dc = tc - sc;

        // előre vagy átlósan előre
        if (Integer.signum(dr) != dir) return false;
        if (!(dc == 0 || Math.abs(dc) == Math.abs(dr))) return false;

        // útvizsgálat (ne legyen bábu közben)
        int stepR = Integer.signum(dr);
        int stepC = Integer.signum(dc);
        int r = sr + stepR;
        int c = sc + stepC;

        while (r != tr || c != tc) {
            if (!board.isEmpty(r, c)) return false;
            r += stepR;
            c += stepC;
        }
        return true;
    }

    /** A lépés fizikai végrehajtása a táblán. */
    public void move(int sr, int sc, int tr, int tc) {
        Icon piece = board.get(sr, sc);
        board.set(tr, tc, piece);
        board.set(sr, sc, null);
    }

    /** Ellenőrzi, hogy a bábu elérte-e az ellenfél kezdősorát (körgyőzelem). */
    public boolean checkWin(int r, int c) {
        ImageIcon piece = (ImageIcon) board.get(r, c);
        if (piece == null) return false;
        boolean isDark = piece.getDescription().startsWith("b_");
        return (isDark && r == Board.SIZE - 1) || (!isDark && r == 0);
    }

    /** Pont hozzáadása a kör győztesének. */
    public void awardPointToWinner() {
        if (darkTurn) darkScore++;
        else lightScore++;
    }

    /** Mérkőzés vége: valamelyik játékos elérte az 5 pontot. */
    public boolean checkMatchEnd() {
        return darkScore == 5 || lightScore == 5;
    }

    /** Új kör indítása → tábla törlése, világos kezd, nincs kényszínszín. */
    public void resetForNextRound() {
        board.clear();
        requiredColor = null;
        darkTurn = false;
    }

    /** Pontszámok lenullázása új mérkőzéshez. */
    public void resetScores() {
        darkScore = 0;
        lightScore = 0;
    }

    /** Lépés után a kör átadása, és a következő kényszínszín beállítása. */
    public void afterMoveSetNext(String landedColor) {
        darkTurn = !darkTurn;
        requiredColor = (landedColor == null || landedColor.isBlank()) ? null : landedColor;
    }

    /** JSON betöltés után a játékállapot helyreállítása. */
    public void setStateAfterLoad(boolean darkTurnLoaded, String requiredColorLoaded) {
        this.darkTurn = darkTurnLoaded;
        this.requiredColor = requiredColorLoaded;
    }

    /**
     * A kezdőállás bábujainak felrakása.
     * Felső sor: sötét (b_...), alsó sor: világos (w_...).
     */
    public void setupInitialTowers() {
        int size = 70;

        for (int c = 0; c < Board.SIZE; c++)
            board.set(0, c, GameAssets.load("b_" + ColorTranslator.toEnglish(ColorBoard.COLORS[0][c]), size));

        for (int c = 0; c < Board.SIZE; c++)
            board.set(Board.SIZE - 1, c, GameAssets.load("w_" + ColorTranslator.toEnglish(ColorBoard.COLORS[Board.SIZE - 1][c]), size));
    }

    /**
     * Megnézi, hogy a soron következő játékosnak van-e legalább
     * egy szabályos lépési lehetősége.
     */
    public boolean hasAnyValidMove() {
        String prefix = darkTurn ? "b_" : "w_";
        String req = requiredColor;

        for (int r = 0; r < Board.SIZE; r++)
            for (int c = 0; c < Board.SIZE; c++) {
                Icon ic = board.get(r, c);
                if (ic instanceof ImageIcon img && img.getDescription().startsWith(prefix)) {

                    // ha nincs kényszínszín, vagy ez a megfelelő bábu
                    if (req == null || img.getDescription().equals(prefix + req)) {

                        // van-e legalább egy érvényes lépés
                        for (int rr = 0; rr < Board.SIZE; rr++)
                            for (int cc = 0; cc < Board.SIZE; cc++)
                                if (canMove(r, c, rr, cc))
                                    return true;
                    }
                }
            }
        return false;
    }

    /**
     * Megnézi, hogy a kötelező színű bábu tud-e lépni.
     */
    public boolean hasRequiredPieceMove() {
        String req = requiredColor;
        if (req == null) return true; // nincs kényszer, tehát oké

        String prefix = darkTurn ? "b_" : "w_";

        // megkeressük a kötelező színű bábut
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Icon ic = board.get(r, c);
                if (ic instanceof ImageIcon img && img.getDescription().equals(prefix + req)) {

                    // nézzük, tud-e bármelyik célmezőre lépni
                    for (int rr = 0; rr < Board.SIZE; rr++) {
                        for (int cc = 0; cc < Board.SIZE; cc++) {
                            if (canMove(r, c, rr, cc)) return true;
                        }
                    }
                    return false; // megtaláltuk, de nem tud lépni
                }
            }
        }

        return false; // nincs is ilyen bábu (nem fordulhat elő)
    }
}

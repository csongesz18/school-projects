package model;

/**
 * A Kamisado tábla színeinek elrendezése.
 * 
 * Ez a fix színmátrix határozza meg, hogy az egyes mezők milyen színűek.
 * Fontos, mert a lépések után a landolt mező színe meghatározza,
 * hogy a következő játékos melyik színű tornyával léphet.
 *
 * Minden mező egy magyar színnevet tartalmaz.
 */
public class ColorBoard {

    /**
     * A tábla színei soronként definiálva.
     * Mindig 8×8-as, így megfelel a Board.SIZE-nek.
     */
    public static final String[][] COLORS = {
        {"barna","lila","kék","sárga","rózsaszín","zöld","piros","narancs"},
        {"zöld","barna","sárga","piros","lila","rózsaszín","narancs","kék"},
        {"piros","sárga","barna","zöld","kék","narancs","rózsaszín","lila"},
        {"sárga","kék","lila","barna","narancs","piros","zöld","rózsaszín"},
        {"rózsaszín","zöld","piros","narancs","barna","lila","kék","sárga"},
        {"lila","rózsaszín","narancs","kék","zöld","barna","sárga","piros"},
        {"kék","narancs","rózsaszín","lila","piros","sárga","barna","zöld"},
        {"narancs","piros","zöld","rózsaszín","sárga","kék","lila","barna"},
    };
}

package ui;

import java.awt.*;

public class ColorUtil {

    /**
     * Magyar színneveket alakít Java Color objektumokká.
     * A tábla mezőinek háttérszínét állítjuk be vele.
     */
    public static Color getColorFromName(String name) {
        return switch (name) {
            case "narancs" -> new Color(255,165,0);  // narancssárga
            case "kék" -> new Color(0,0,139);        // sötétkék
            case "lila" -> new Color(128,0,128);     // lila
            case "barna" -> new Color(150,75,0);     // barna
            case "sárga" -> new Color(204,204,0);    // mustár-sárga
            case "rózsaszín" -> Color.PINK;          // rózsaszín
            case "zöld" -> new Color(0,100,0);       // sötétzöld
            case "piros" -> new Color(139,0,0);      // bordós piros
            default -> Color.GRAY;                   // ha ismeretlen, szürke
        };
    }
}

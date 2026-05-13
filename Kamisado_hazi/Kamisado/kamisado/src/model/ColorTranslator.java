package model;

/**
 * Színfordító segédosztály.
 *
 * Feladata, hogy a tábla magyar színneveit angol formára alakítsa.
 * Az ikonok fájlnevei angolul vannak, ezért szükség van erre az átalakításra.
 */
public class ColorTranslator {

    /**
     * Magyar szín → angol szín átalakítása.
     * Ha a szín nem ismert, akkor visszaadja az eredetit.
     */
    public static String toEnglish(String hun) {
        if (hun == null) {
            return null;
        }
        return switch (hun) {
            case "narancs" -> "orange";
            case "kék" -> "blue";
            case "lila" -> "purple";
            case "barna" -> "brown";
            case "sárga" -> "yellow";
            case "rózsaszín" -> "pink";
            case "zöld" -> "green";
            case "piros" -> "red";
            default -> hun; // ismeretlen bemenet → változatlanul hagyjuk
        };
    }
}

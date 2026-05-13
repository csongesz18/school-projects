package kamisado.test;

import model.ColorTranslator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A ColorTranslator osztály magyar → angol színfordítást teszteli.
 */
public class ColorTranslatorTest {

    /**
     * Minden ismert magyar szín helyesen legyen angolra fordítva.
     */
    @Test
    void testKnownColors() {
        assertEquals("orange", ColorTranslator.toEnglish("narancs"));
        assertEquals("blue", ColorTranslator.toEnglish("kék"));
        assertEquals("purple", ColorTranslator.toEnglish("lila"));
        assertEquals("brown", ColorTranslator.toEnglish("barna"));
        assertEquals("yellow", ColorTranslator.toEnglish("sárga"));
        assertEquals("pink", ColorTranslator.toEnglish("rózsaszín"));
        assertEquals("green", ColorTranslator.toEnglish("zöld"));
        assertEquals("red", ColorTranslator.toEnglish("piros"));
    }

    /**
     * Ismeretlen szín esetén az eredeti szöveget kell visszakapni.
     */
    @Test
    void testUnknownColorReturnsOriginal() {
        assertEquals("???", ColorTranslator.toEnglish("???"));
        assertEquals("", ColorTranslator.toEnglish(""));
        assertEquals("valami", ColorTranslator.toEnglish("valami"));
    }

    /**
     * Null bemenet ne okozzon kivételt.
     */
    @Test
    void testNullSafety() {
        assertDoesNotThrow(() -> ColorTranslator.toEnglish(null));
    }

    /**
     * Null bemenetre null a visszatérési érték.
     */
    @Test
    void testNullInputReturnsNull() {
        assertNull(ColorTranslator.toEnglish(null));
    }
}

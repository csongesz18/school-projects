package model;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON alapú mentés és visszatöltés külső könyvtár nélkül.
 * A mentett állapot tartalmazza:
 *   - melyik játékos következik
 *   - a kényszerszínt
 *   - a tábla teljes ikonmátrixát
 */
public class SaveManager {

    /** Játékállás mentése JSON fájlba. */
    public static void save(GameLogic logic, File file) {
        File out = ensureJsonExtension(file);
        Board board = logic.getBoard();

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"))) {

            pw.println("{");
            pw.println("  \"turn\": \"" + (logic.isDarkTurn() ? "dark" : "light") + "\",");

            // kényszínszín: szöveg vagy null
            String req = logic.getRequiredColor();
            pw.println("  \"requiredColor\": " +
                    (req == null || req.isBlank() ? "null," : "\"" + escape(req) + "\","));

            // tábla mentése: 8×8 ikonleírások
            pw.println("  \"board\": [");
            for (int r = 0; r < Board.SIZE; r++) {
                pw.print("    [");
                for (int c = 0; c < Board.SIZE; c++) {
                    Icon icon = board.get(r, c);
                    String val = "empty";
                    if (icon instanceof ImageIcon img && img.getDescription() != null) {
                        val = img.getDescription();
                    }
                    pw.print("\"" + escape(val) + "\"");
                    if (c < Board.SIZE - 1) pw.print(", ");
                }
                pw.print("]");
                if (r < Board.SIZE - 1) pw.println(","); else pw.println();
            }
            pw.println("  ]");
            pw.println("}");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Játékállás betöltése JSON-ból. */
    public static void load(GameLogic logic, File file) {
        Board board = logic.getBoard();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) sb.append(line).append('\n');
            String json = sb.toString();

            // játékos, kényszínszín
            boolean darkTurn = "dark".equals(extractString(json, "\"turn\""));
            String required = extractNullableString(json, "\"requiredColor\"");

            // tábla beolvasása
            List<List<String>> matrix = extractMatrix(json, "\"board\"");
            for (int r = 0; r < Board.SIZE; r++) {
                List<String> row = matrix.get(r);
                for (int c = 0; c < Board.SIZE; c++) {
                    String desc = row.get(c);
                    board.set(r, c,
                            "empty".equals(desc) ? null : GameAssets.load(desc, 70));
                }
            }

            // logikai állapot visszaállítása
            logic.setStateAfterLoad(darkTurn, required);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ----- JSON segédfüggvények ----- */
    
    private static File ensureJsonExtension(File f) {
        String name = f.getName().toLowerCase();
        if (!name.endsWith(".json")) {
            return new File(f.getParentFile(), f.getName() + ".json");
        }
        return f;
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String s) {
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String extractString(String json, String key) {
        int k = json.indexOf(key);
        if (k < 0) return null;
        int colon = json.indexOf(':', k);
        int q1 = json.indexOf('"', colon + 1);
        int q2 = nextQuote(json, q1 + 1);
        if (q1 < 0 || q2 < 0) return null;
        return unescape(json.substring(q1 + 1, q2));
    }

    private static String extractNullableString(String json, String key) {
        int k = json.indexOf(key);
        if (k < 0) return null;
        int colon = json.indexOf(':', k);
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (json.startsWith("null", i)) return null;
        int q1 = json.indexOf('"', i);
        int q2 = nextQuote(json, q1 + 1);
        if (q1 < 0 || q2 < 0) return null;
        return unescape(json.substring(q1 + 1, q2));
    }

    private static int nextQuote(String s, int from) {
        int i = from;
        while (i >= 0 && i < s.length()) {
            i = s.indexOf('"', i);
            if (i < 0) return -1;
            // ha az idézőjel előtt backslash van, az escape, keressünk tovább
            int backslashes = 0;
            int j = i - 1;
            while (j >= 0 && s.charAt(j) == '\\') { backslashes++; j--; }
            if (backslashes % 2 == 0) return i; // nem escaped
            i++; // escaped volt → tovább
        }
        return -1;
    }

    /**
     * Mátrix (sorok listája) kinyerése a "board": [...] részéből.
     * Itt volt a hiba: eddig az egész nagy tömböt egy sornak értelmezte.
     */
    private static List<List<String>> extractMatrix(String json, String key) {
        List<List<String>> rows = new ArrayList<>();

        int k = json.indexOf(key);
        if (k < 0) return rows;

        int start = json.indexOf('[', k);
        if (start < 0) return rows;

        int level = 0, end = -1;
        for (int i = start; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '[') level++;
            else if (ch == ']') {
                level--;
                if (level == 0) {
                    end = i;
                    break;
                }
            }
        }
        if (end < 0) return rows;

        // levágjuk a külső szögletes zárójeleket: [ [ ... ], [ ... ], ... ]
        String array = json.substring(start + 1, end); // csak a belső rész

        int pos = 0;
        while (true) {
            int rowStart = array.indexOf('[', pos);
            if (rowStart < 0) break;

            int lvl = 0, rowEnd = -1;
            for (int i = rowStart; i < array.length(); i++) {
                char ch = array.charAt(i);
                if (ch == '[') lvl++;
                else if (ch == ']') {
                    lvl--;
                    if (lvl == 0) {
                        rowEnd = i;
                        break;
                    }
                }
            }
            if (rowEnd < 0) break;

            String row = array.substring(rowStart + 1, rowEnd); // "a","b","c"
            rows.add(splitJsonStringArray(row));
            pos = rowEnd + 1;
        }
        return rows;
    }

    /** EZ VOLT A HIBÁS RÉSZ – most korrektül léptet a vessző után is. */
    private static List<String> splitJsonStringArray(String row) {
        List<String> res = new ArrayList<>();
        int n = row.length();
        int i = 0;
        while (i < n) {
            // szóközök átugrása
            while (i < n && Character.isWhitespace(row.charAt(i))) i++;
            if (i >= n) break;

            // következő idéző kezdete
            if (row.charAt(i) != '"') { i++; continue; }
            int q1 = i;
            int q2 = q1 + 1;
            while (true) {
                q2 = row.indexOf('"', q2);
                if (q2 < 0) break;
                int backslashes = 0, j = q2 - 1;
                while (j >= 0 && row.charAt(j) == '\\') { backslashes++; j--; }
                if (backslashes % 2 == 0) break; // nem escaped
                q2++;
            }
            String val = unescape(row.substring(q1 + 1, q2));
            res.add(val);

            // lépjünk túl a záró idézőn
            i = q2 + 1;
            // szóközök és VESSZŐ átugrása
            while (i < n && (Character.isWhitespace(row.charAt(i)) || row.charAt(i) == ',')) i++;
        }
        return res;
    }
}

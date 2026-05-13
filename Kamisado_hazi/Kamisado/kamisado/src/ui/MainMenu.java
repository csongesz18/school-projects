package ui;

import model.GameLogic;
import model.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * A játék főmenüje.
 * Innen lehet új játékot indítani, betölteni, súgót megnyitni vagy kilépni.
 */
public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Kamisado - Menü");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cím felül
        JLabel title = new JLabel("K A M I S A D O", SwingConstants.CENTER);
        title.setFont(new Font("Algerian", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        // Menü gombok
        JPanel center = new JPanel(new GridLayout(4,1,10,10));
        JButton newGame = new JButton("Új játék indítása");
        JButton loadGame = new JButton("Játék betöltése (JSON)");
        JButton help = new JButton("Súgó / Játékszabályok");
        JButton exit = new JButton("Kilépés");

        center.add(newGame);
        center.add(loadGame);
        center.add(help);
        center.add(exit);
        add(center, BorderLayout.CENTER);

        // Új játék indítása
        newGame.addActionListener(e -> {
            new GameGui();   // új ablakban megnyitjuk a játékot
            dispose();       // bezárjuk a menüt
        });

        // JSON Állás betöltése
        loadGame.addActionListener(e -> {
            File saveDir = new File("saves");
            if (!saveDir.exists()) saveDir.mkdirs();

            JFileChooser fc = new JFileChooser(saveDir);

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                GameLogic logic = new GameLogic();

                // 🔥 ÚJ JSON betöltés (nem Wrapperes!)
                SaveManager.load(logic, fc.getSelectedFile());

                new GameGui(logic); // játék indítása a betöltött állapottal
                dispose();          // menü bezárása
            }
        });

        // Súgó megjelenítése
        help.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    """
                    KAMISADO - Rövid játékszabály
                    
                    • Mindig a világos kezd.
                    • Csak előre és átlósan előre lehet lépni.
                    • A következő játékos lépő bábujának színét 
                      az előző lépés landolt mezője határozza meg.
                    • Ha eléred az ellenfél kezdősorát → győzelem.
                    • Ha az ellenfél nem tud lépni → győzelem.
                    • Ha senki sem tud lépni → döntetlen.
                    """,
                    "Súgó",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Kilépés teljes programból
        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}

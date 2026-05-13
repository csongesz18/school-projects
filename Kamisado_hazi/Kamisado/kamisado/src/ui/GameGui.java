package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * A teljes játékablak grafikus felülete.
 * Itt történik:
 *  - a tábla kirajzolása,
 *  - kattintások kezelése,
 *  - körváltás és kijelzések frissítése,
 *  - pontszámok és mentés/betöltés kezelése.
 */
public class GameGui extends JFrame {
    private static final int SIZE = Board.SIZE;

    private final GameLogic logic;                // játékállapot és szabályok
    private int selectedRow = -1, selectedCol = -1;

    private JLabel turnLabel, scoreDarkLabel, scoreLightLabel;
    private ImageIcon dotIcon;
    private BoardPanel boardPanel;
    private JButton resetScoresBtn;

    /** Új játék indítása üres állapottal. */
    public GameGui() {
        this.logic = new GameLogic();
        setupUI();
        logic.setupInitialTowers();
        boardPanel.refresh(logic.getBoard());
        updateScoreLabels();
        updateTurnLabel();
    }

    /** Játék indítása betöltött állapottal (load után). */
    public GameGui(GameLogic loadedLogic) {
        this.logic = loadedLogic;
        setupUI();
        boardPanel.refresh(logic.getBoard());
        updateScoreLabels();
        updateTurnLabel();
        autoSelectRequiredPiece();
    }

    /** Felület felépítése: felső sáv, pontkijelző, tábla, gombok. */
    private void setupUI() {
        resetScoresBtn = new JButton("Pontok törlése");
        setTitle("Kamisado");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        dotIcon = loadPng("dot", 15);

        // ─── Felső sáv (körjelzés + vezérlőgombok) ─────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        turnLabel = new JLabel("", SwingConstants.LEFT);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(turnLabel, BorderLayout.WEST);

        JButton newGameButton  = new JButton("Új játék (Főmenü)");
        JButton saveJSONButton = new JButton("Mentés JSON");
        JButton loadJSONButton = new JButton("Betöltés JSON");

        JPanel rightButtons = new JPanel();
        rightButtons.add(newGameButton);
        rightButtons.add(resetScoresBtn);
        rightButtons.add(saveJSONButton);
        rightButtons.add(loadJSONButton);
        topBar.add(rightButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ─── Pontkijelző oldalpanel ─────────────────
        JPanel score = new JPanel();
        score.setLayout(new BoxLayout(score, BoxLayout.Y_AXIS));
        score.setPreferredSize(new Dimension(180, getHeight()));

        JLabel title = new JLabel("Pontok", SwingConstants.CENTER);
        title.setFont(new Font("Algerian", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreDarkLabel = new JLabel();
        scoreDarkLabel.setFont(new Font("Algerian", Font.PLAIN, 18));
        scoreDarkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLightLabel = new JLabel();
        scoreLightLabel.setFont(new Font("Algerian", Font.PLAIN, 18));
        scoreLightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        score.add(Box.createVerticalStrut(20));
        score.add(title);
        score.add(Box.createVerticalStrut(15));
        score.add(scoreDarkLabel);
        score.add(Box.createVerticalStrut(10));
        score.add(scoreLightLabel);

        add(score, BorderLayout.WEST);

        // ─── Játéktábla ─────────────────
        boardPanel = new BoardPanel(ColorBoard.COLORS, dotIcon);
        add(boardPanel, BorderLayout.CENTER);
        boardPanel.addCellClickListener(this::handleCellClick);

        // ─── Gombok működése ─────────────────

        // pontok törlése
        resetScoresBtn.addActionListener(e -> {
            logic.resetScores();
            updateScoreLabels();
        });

        // vissza főmenübe
        newGameButton.addActionListener(e -> {
            new MainMenu();
            dispose();
        });

        // JSON mentés
        saveJSONButton.addActionListener(e -> {
            File dir = new File("saves");
            if (!dir.exists()) dir.mkdirs();
            JFileChooser fc = new JFileChooser(dir);
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                SaveManager.save(logic, fc.getSelectedFile());
            }
        });

        // JSON betöltés
        loadJSONButton.addActionListener(e -> {
            File dir = new File("saves");
            if (!dir.exists()) dir.mkdirs();
            JFileChooser fc = new JFileChooser(dir);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                SaveManager.load(logic, fc.getSelectedFile());
                boardPanel.refresh(logic.getBoard());
                updateScoreLabels();
                updateTurnLabel();
                autoSelectRequiredPiece();
            }
        });

        setVisible(true);
    }

    /**
     * Egy mezőre kattintva:
     *  - kijelölés első kattintásra
     *  - lépés második kattintásra, ha szabályos
     */
    private void handleCellClick(int r, int c) {

        // első kattintás → kijelölés
        if (selectedRow == -1) {
            Icon ic = logic.getBoard().get(r, c);
            if (!(ic instanceof ImageIcon) || !logic.canSelect((ImageIcon) ic)) return;

            boardPanel.clearHighlights(logic.getBoard());
            selectedRow = r;
            selectedCol = c;
            boardPanel.highlightSelected(r, c);

            // lehetséges lépések mutatása
            for (int rr = 0; rr < SIZE; rr++)
                for (int cc = 0; cc < SIZE; cc++)
                    if (logic.canMove(r, c, rr, cc))
                        boardPanel.highlightMove(rr, cc);
            return;
        }

        // második kattintás → lépés
        if (logic.canMove(selectedRow, selectedCol, r, c)) {
            logic.move(selectedRow, selectedCol, r, c);
            boardPanel.refresh(logic.getBoard());

            // elérte a túloldalt → körgyőzelem
            if (logic.checkWin(r, c)) {
                logic.awardPointToWinner();
                updateScoreLabels();
                JOptionPane.showMessageDialog(this,
                        (logic.isDarkTurn() ? "Sötét" : "Világos") + " játékos nyert! +1 pont!",
                        "Kör vége", JOptionPane.INFORMATION_MESSAGE);

                logic.resetForNextRound();
                logic.setupInitialTowers();
                boardPanel.refresh(logic.getBoard());
                selectedRow = selectedCol = -1;
                updateTurnLabel();
                return;
            }

            // körváltás + kényszínszín beállítása
            logic.afterMoveSetNext(ColorTranslator.toEnglish(ColorBoard.COLORS[r][c]));
            boardPanel.clearHighlights(logic.getBoard());
            selectedRow = selectedCol = -1;
            autoSelectRequiredPiece();
            updateTurnLabel();

            // ha a kötelező bábu nem tud lépni → automatikus veszteség
            if (!logic.hasRequiredPieceMove()) {
                if (logic.isDarkTurn()) {
                    // sötét következne → de nem tud lépni → sötét veszít
                    logic.setScores(logic.getDarkScore(), logic.getLightScore() + 1);
                    JOptionPane.showMessageDialog(this, "Sötét nem tud lépni.\nVilágos +1 pont!");
                } else {
                    logic.setScores(logic.getDarkScore() + 1, logic.getLightScore());
                    JOptionPane.showMessageDialog(this, "Világos nem tud lépni.\nSötét +1 pont!");
                }

                // új kör indítása
                logic.resetForNextRound();
                logic.setupInitialTowers();
                boardPanel.refresh(logic.getBoard());
                updateTurnLabel();
                updateScoreLabels();
                return;
            }

        } else {
            // érvénytelen lépés → visszaállítjuk a kijelölést
            boardPanel.clearHighlights(logic.getBoard());
            selectedRow = selectedCol = -1;
        }
    }

    /** Automatikusan kijelöli a kényszínszínű bábut, ha léteznie kell. */
    private void autoSelectRequiredPiece() {
        String req = logic.getRequiredColor();
        if (req == null) return;

        String prefix = logic.isDarkTurn() ? "b_" : "w_";

        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Icon ic = logic.getBoard().get(r, c);
                if (ic instanceof ImageIcon img && (prefix + req).equals(img.getDescription())) {

                    selectedRow = r;
                    selectedCol = c;
                    boardPanel.highlightSelected(r, c);

                    // léphető pozíciók mutatása
                    for (int rr = 0; rr < SIZE; rr++)
                        for (int cc = 0; cc < SIZE; cc++)
                            if (logic.canMove(r, c, rr, cc))
                                boardPanel.highlightMove(rr, cc);
                    return;
                }
            }
    }

    /** A körjelző felirata. */
    private void updateTurnLabel() {
        String player = logic.isDarkTurn() ? "SÖTÉT" : "VILÁGOS";
        String req = logic.getRequiredColor();

        if (req != null) {
            player += switch (req) {
                case "blue"   -> " (K)";
                case "purple" -> " (L)";
                case "brown"  -> " (B)";
                case "yellow" -> " (S)";
                case "pink"   -> " (R)";
                case "green"  -> " (Z)";
                case "red"    -> " (P)";
                case "orange" -> " (N)";
                default -> "";
            };
        }

        turnLabel.setText("Következő játékos: " + player);
    }

    /** PNG ikon betöltése és átméretezése. */
    private ImageIcon loadPng(String name, int size) {
        var url = getClass().getResource("/ui/img/" + name + ".png");
        if (url == null) return null;
        Image scaled = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled, name);
    }

    /** Pontszámok kiírása és a törlés gomb láthatósága. */
    private void updateScoreLabels() {
        scoreDarkLabel.setText("Sötét: " + logic.getDarkScore());
        scoreLightLabel.setText("Világos: " + logic.getLightScore());
        resetScoresBtn.setVisible(logic.getDarkScore() > 0 || logic.getLightScore() > 0);
    }
}

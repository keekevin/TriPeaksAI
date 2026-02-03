package org.example.View;

import org.example.Controller.DualGameAIController;
import org.example.Controller.DualGameController;
import org.example.Controller.DualGamePlayerController;
import org.example.Model.ModalitaGioco;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DualGameGUI extends JFrame {

    private DualGameController dualController;
    private TriPeaksGUI playerGui;
    private TriPeaksGUI aiGui;

    private DualGamePlayerController playerController;
    private DualGameAIController aiController;

    private JLabel playerMovesLabel;
    private JLabel aiMovesLabel;

    private JPanel leftPanel;
    private JPanel rightPanel;


    private boolean playerTurn = true;
    private boolean gameEnded = false;

    private Color playerActiveColor = new Color(0, 200, 0);
    private Color aiActiveColor = new Color(200, 0, 0);

    public DualGameGUI(String solverPath) {
        this.dualController = new DualGameController(solverPath);

        setTitle("TriPeaks - Umano vs IA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 900);
        setLocationRelativeTo(null);

        initComponents();
        iniziaPartita();
    }


    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1,2,20,0));
        topPanel.setBackground(new Color(0,100,150));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        playerMovesLabel = new JLabel("Giocatore:0 mosse",SwingConstants.CENTER);
        playerMovesLabel.setFont(new Font("Arial",Font.BOLD,22));
        playerMovesLabel.setForeground(Color.WHITE);

        aiMovesLabel = new JLabel("IA:0 mosse",SwingConstants.CENTER);
        aiMovesLabel.setFont(new Font("Arial",Font.BOLD,22));
        aiMovesLabel.setForeground(Color.WHITE);


        topPanel.add(playerMovesLabel);
        topPanel.add(aiMovesLabel);

        add(topPanel,BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridLayout(1,2,5,0));
        centerPanel.setBackground(Color.BLACK);

        leftPanel = createGamePanel("👤 IL TUO TAVOLO", new Color(0, 200, 0));
        rightPanel = createGamePanel("🤖 TAVOLO IA", new Color(200, 0, 0));

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        add(centerPanel,BorderLayout.CENTER);
    }


    private JPanel createGamePanel(String title, Color activeColor) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.BLACK);

        // Bordo normale (inattivo)
        Border inactiveBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 3),
                title,
                0, 0,
                new Font("Arial", Font.BOLD, 18),
                Color.GRAY
        );

        container.setBorder(inactiveBorder);

        container.putClientProperty("activeColor",activeColor);
        container.putClientProperty("title",title);

        return container;
    }

    public void setActivePanel(boolean isPlayerActive) {
        SwingUtilities.invokeLater(() -> {
            if (isPlayerActive) {
                // Player attivo
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(playerActiveColor, 5),
                        "👤 IL TUO TAVOLO ⚡ IL TUO TURNO",
                        0, 0,
                        new Font("Arial", Font.BOLD, 20),
                        playerActiveColor
                ));

                // ✨ RIMUOVI OVERLAY DAL PLAYER
                removeOverlay(leftPanel);

                // IA inattiva
                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "🤖 TAVOLO IA ⏸️",
                        0, 0,
                        new Font("Arial", Font.PLAIN, 16),
                        Color.GRAY
                ));

                // ✨ AGGIUNGI OVERLAY ALL'IA
                addDarkOverlay(rightPanel);

            } else {
                // IA attiva
                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(aiActiveColor, 5),
                        "🤖 TAVOLO IA ⚡ TURNO IA",
                        0, 0,
                        new Font("Arial", Font.BOLD, 20),
                        aiActiveColor
                ));

                // ✨ RIMUOVI OVERLAY DALL'IA
                removeOverlay(rightPanel);

                // Player inattivo
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "👤 IL TUO TAVOLO ⏸️",
                        0, 0,
                        new Font("Arial", Font.PLAIN, 16),
                        Color.GRAY
                ));

                // ✨ AGGIUNGI OVERLAY AL PLAYER
                addDarkOverlay(leftPanel);
            }

            leftPanel.revalidate();
            leftPanel.repaint();
            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }



    private void addDarkOverlay(JPanel panel) {
        // Rimuovi overlay precedente se esiste
        removeOverlay(panel);

        // Crea overlay semi-trasparente
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 120)); // Nero 60% opaco
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        overlay.setOpaque(false);
        overlay.setName("darkOverlay"); // Tag per trovarlo dopo

        // Aggiungi SOPRA tutto (usa un GlassPane-like approach)
        panel.add(overlay, BorderLayout.CENTER);
        panel.setComponentZOrder(overlay, 0); // Metti in primo piano

        panel.revalidate();
        panel.repaint();
    }


    private void removeOverlay(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel && "darkOverlay".equals(comp.getName())) {
                panel.remove(comp);
                break;
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createLabeledPanel(String title,Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor,3),
                title,
                0,
                0,
                new Font("Arial",Font.BOLD,18),
                Color.WHITE));
        panel.setBackground(Color.BLACK);
        return panel;
    }


    private void iniziaPartita() {
        try {
            dualController.iniziaPartita();

            playerController = new DualGamePlayerController(dualController, this);
            aiController = new DualGameAIController(dualController, this);

            playerGui = new TriPeaksGUI(ModalitaGioco.UMANO, playerController, 0.7,true);
            aiGui = new TriPeaksGUI(ModalitaGioco.IA, aiController, 0.7,true);

            playerGui.updateDisplay(dualController.getPlayerGame());
            aiGui.updateDisplay(dualController.getAiGame());

            leftPanel.add(playerGui.getContentPane(), BorderLayout.CENTER);
            rightPanel.add(aiGui.getContentPane(), BorderLayout.CENTER);

            // ✅ USA IL NUOVO METODO
            setActivePanel(true);  // Player attivo all'inizio

            revalidate();
            repaint();

            System.out.println("✅ Partita dual inizializzata - Turni alternati attivi!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nell'inizializzazione: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public void onPlayerMove(){
        if(gameEnded) return;

        dualController.incrementPlayerMoves();

        SwingUtilities.invokeLater(() -> {
            updateLabels();
            playerGui.updateDisplay(dualController.getPlayerGame());

            if (dualController.getPlayerGame().isFinita()) {
                onPlayerFinished();
                return;
            }

            playerTurn = false;
            setActivePanel(false);  // ✅ IA attiva

            Timer timer = new Timer(1000, e -> turnoIA());
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void turnoIA() {
        if(gameEnded) return;

        System.out.println("\n🤖 === TURNO IA ===");

        aiController.faiMossaIA();
        dualController.incrementAiMoves();

        updateLabels();
        aiGui.updateDisplay(dualController.getAiGame());

        if(dualController.getAiGame().isFinita()) {
            onAIFinished();
        } else {
            playerTurn = true;
            setActivePanel(true);  // ✅ Player attivo
        }
    }

    private void updateLabels() {
        int playerDeck = dualController.getPlayerGame().getCarteRimanentiMazzo();
        int aiDeck = dualController.getAiGame().getCarteRimanentiMazzo();

        int playerMoves = dualController.getPlayerMoves();
        int aiMoves = dualController.getAiMoves();

        playerMovesLabel.setText(
                "👤 Giocatore | Mosse: " + playerMoves + " | 🂠 Mazzo: " + playerDeck
        );

        aiMovesLabel.setText(
                "🤖 IA | Mosse: " + aiMoves + " | 🂠 Mazzo: " + aiDeck
        );
    }

    private void onPlayerFinished() {
        gameEnded = true;
        dualController.setPlayerFinished(true);

        System.out.println("👤 Giocatore ha terminato!");

        // ✨ SOLO IA ATTIVA
        SwingUtilities.invokeLater(() -> {
            leftPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                    "👤 IL TUO TAVOLO ✅ COMPLETATO",
                    0, 0,
                    new Font("Arial", Font.BOLD, 16),
                    Color.GREEN
            ));

            rightPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.ORANGE, 5),
                    "🤖 TAVOLO IA ⚡ CONTINUA DA SOLA...",
                    0, 0,
                    new Font("Arial", Font.BOLD, 20),
                    Color.ORANGE
            ));
        });

        continuaAI();
    }

    private void onAIFinished() {
        gameEnded = true;
        dualController.setAiFinished(true);

        System.out.println("🤖 IA ha terminato!");

        mostraRisultatoFinale();
    }

    private void continuaAI() {
        Timer aiTimer = new Timer(1500, e -> {
            if (dualController.getAiGame().isFinita()) {
                ((Timer) e.getSource()).stop();
                mostraRisultatoFinale();
                return;
            }

            aiController.faiMossaIA();
            dualController.incrementAiMoves();

            updateLabels();
            aiGui.updateDisplay(dualController.getAiGame());
        });
        aiTimer.start();
    }

    public void mostraRisultatoFinale() {
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(1000);  // Pausa per vedere l'ultima mossa
            } catch (InterruptedException ignored) {}

            StringBuilder msg = new StringBuilder();
            msg.append("╔════════════════════════════════════╗\n");
            msg.append("║      RISULTATO FINALE              ║\n");
            msg.append("╚════════════════════════════════════╝\n\n");

            boolean playerWon = dualController.getPlayerGame().haVinto();
            boolean aiWon = dualController.getAiGame().haVinto();

            msg.append("👤 GIOCATORE: ");
            if (playerWon) {
                msg.append("✅ COMPLETATO in " + dualController.getPlayerMoves() + " mosse\n");
            } else {
                msg.append("❌ BLOCCATO dopo " + dualController.getPlayerMoves() + " mosse\n");
            }

            msg.append("🤖 IA:        ");
            if (aiWon) {
                msg.append("✅ COMPLETATO in " + dualController.getAiMoves() + " mosse\n");
            } else {
                msg.append("❌ BLOCCATO dopo " + dualController.getAiMoves() + " mosse\n");
            }

            msg.append("\n");

            // Determina il vincitore
            if (playerWon && aiWon) {
                int diff = dualController.getPlayerMoves() - dualController.getAiMoves();
                if (diff < 0) {
                    msg.append("🏆 HAI VINTO! Sei stato più efficiente di " + Math.abs(diff) + " mosse!");
                } else if (diff > 0) {
                    msg.append("🤖 IA HA VINTO! È stata più efficiente di " + diff + " mosse!");
                } else {
                    msg.append("🤝 PAREGGIO PERFETTO! Stesso numero di mosse!");
                }
            } else if (playerWon) {
                msg.append("🏆 HAI VINTO! L'IA si è bloccata!");
            } else if (aiWon) {
                msg.append("🤖 IA HA VINTO! Ti sei bloccato!");
            } else {
                msg.append("🤷 Entrambi bloccati!");
            }

            JOptionPane.showMessageDialog(this,
                    msg.toString(),
                    "Risultato Finale",
                    JOptionPane.INFORMATION_MESSAGE);

            // Opzione per rigiocare
            int choice = JOptionPane.showConfirmDialog(this,
                    "Vuoi fare una rivincita?",
                    "Nuova Partita",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // Reset e ricomincia
                gameEnded = false;
                playerTurn = true;

                // Rimuovi le vecchie GUI
                Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
                JPanel leftPanel = (JPanel) components[0];
                JPanel rightPanel = (JPanel) components[1];
                leftPanel.removeAll();
                rightPanel.removeAll();

                iniziaPartita();
            } else {
                System.exit(0);
            }
        });
    }

    public boolean isPlayerTurn() {
        return playerTurn && !gameEnded;
    }


}

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
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(playerActiveColor, 5),
                        "👤 IL TUO TAVOLO ⚡ IL TUO TURNO",
                        0, 0,
                        new Font("Arial", Font.BOLD, 20),
                        playerActiveColor
                ));

                removeOverlay(leftPanel);

                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "🤖 TAVOLO IA ⏸️",
                        0, 0,
                        new Font("Arial", Font.PLAIN, 16),
                        Color.GRAY
                ));

                addDarkOverlay(rightPanel);

            } else {
                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(aiActiveColor, 5),
                        "🤖 TAVOLO IA ⚡ TURNO IA",
                        0, 0,
                        new Font("Arial", Font.BOLD, 20),
                        aiActiveColor
                ));

                removeOverlay(rightPanel);

                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "👤 IL TUO TAVOLO ⏸️",
                        0, 0,
                        new Font("Arial", Font.PLAIN, 16),
                        Color.GRAY
                ));

                addDarkOverlay(leftPanel);
            }

            leftPanel.revalidate();
            leftPanel.repaint();
            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }



    private void addDarkOverlay(JPanel panel) {
        removeOverlay(panel);

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
        overlay.setName("darkOverlay");

        panel.add(overlay, BorderLayout.CENTER);
        panel.setComponentZOrder(overlay, 0);
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

            setActivePanel(true);

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

            if (dualController.isAiFinished()) {
                if (dualController.getAiGame().haVinto()) {
                    return;
                }
                playerTurn = true;
                setActivePanel(true);
                return;
            }

            playerTurn = false;
            setActivePanel(false);

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
            setActivePanel(true);
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
        dualController.setPlayerFinished(true);
        boolean playerWon = dualController.getPlayerGame().haVinto();

        System.out.println("👤 Giocatore ha terminato! Vinto: " + playerWon);

        SwingUtilities.invokeLater(() -> {
            if (playerWon) {
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GREEN, 3),
                        "👤 IL TUO TAVOLO ✅ HAI VINTO!",
                        0, 0,
                        new Font("Arial", Font.BOLD, 16),
                        Color.GREEN
                ));
            } else {
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.RED, 2),
                        "👤 IL TUO TAVOLO ❌ BLOCCATO",
                        0, 0,
                        new Font("Arial", Font.BOLD, 16),
                        Color.RED
                ));
            }
        });

        if (playerWon) {
            if (dualController.isAiFinished()) {
                gameEnded = true;
                mostraRisultatoFinale();
            } else {
                System.out.println("🤖 L'IA ha un'ultima chance per pareggiare...");

                SwingUtilities.invokeLater(() -> {
                    setActivePanel(false);

                    Timer timer = new Timer(1500, e -> {
                        ultimaMossaIA();
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            }
        }
        else {
            if (dualController.isAiFinished()) {
                gameEnded = true;
                mostraRisultatoFinale();
            } else {
                continuaAI();
            }
        }
    }

    private void onAIFinished() {
        dualController.setAiFinished(true);
        boolean aiWon = dualController.getAiGame().haVinto();

        System.out.println("🤖 IA ha terminato! Vinto: " + aiWon);

        SwingUtilities.invokeLater(() -> {
            if (aiWon) {
                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GREEN, 3),
                        "🤖 TAVOLO IA ✅ IA HA VINTO!",
                        0, 0,
                        new Font("Arial", Font.BOLD, 16),
                        Color.GREEN
                ));
            } else {
                rightPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.RED, 2),
                        "🤖 TAVOLO IA ❌ BLOCCATA",
                        0, 0,
                        new Font("Arial", Font.BOLD, 16),
                        Color.RED
                ));
            }

            if (!aiWon && !dualController.isPlayerFinished()) {
                leftPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(playerActiveColor, 5),
                        "👤 IL TUO TAVOLO ⚡ CONTINUA DA SOLO...",
                        0, 0,
                        new Font("Arial", Font.BOLD, 20),
                        playerActiveColor
                ));

                removeOverlay(leftPanel);
            }
        });

        if (aiWon) {
            gameEnded = true;
            mostraRisultatoFinale();
        }
        else {
            if (dualController.isPlayerFinished()) {
                gameEnded = true;
                mostraRisultatoFinale();
            } else {
                playerTurn = true;
                setActivePanel(true);
            }
        }
    }

    private void ultimaMossaIA() {
        System.out.println("\n🤖 === ULTIMA MOSSA IA ===");

        aiController.faiMossaIA();
        dualController.incrementAiMoves();

        updateLabels();
        aiGui.updateDisplay(dualController.getAiGame());

        gameEnded = true;

        if(dualController.getAiGame().isFinita()) {
            dualController.setAiFinished(true);
            boolean aiWon = dualController.getAiGame().haVinto();

            SwingUtilities.invokeLater(() -> {
                if (aiWon) {
                    rightPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.GREEN, 3),
                            "🤖 TAVOLO IA ✅ IA HA VINTO!",
                            0, 0,
                            new Font("Arial", Font.BOLD, 16),
                            Color.GREEN
                    ));
                } else {
                    rightPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.RED, 2),
                            "🤖 TAVOLO IA ❌ BLOCCATA",
                            0, 0,
                            new Font("Arial", Font.BOLD, 16),
                            Color.RED
                    ));
                }
            });
        }

        mostraRisultatoFinale();
    }

    private void continuaAI() {
        Timer aiTimer = new Timer(1500, e -> {
            if (dualController.getAiGame().isFinita()) {
                ((Timer) e.getSource()).stop();

                if (dualController.getAiGame().haVinto()) {
                    onAIFinished();
                } else {
                    gameEnded = true;
                    mostraRisultatoFinale();
                }
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
                Thread.sleep(1000);
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
                msg.append(" Entrambi bloccati!");
            }

            JOptionPane.showMessageDialog(this,
                    msg.toString(),
                    "Risultato Finale",
                    JOptionPane.INFORMATION_MESSAGE);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Vuoi fare una rivincita?",
                    "Nuova Partita",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                gameEnded = false;
                playerTurn = true;

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
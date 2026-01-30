package org.example;

import org.example.Controller.DualGameAIController;
import org.example.Controller.DualGameController;
import org.example.Controller.DualGamePlayerController;

import javax.swing.*;
import java.awt.*;

public class DualGameGUI extends JFrame {

    private DualGameController dualController;
    private TriPeaksGUI playerGui;
    private TriPeaksGUI aiGui;

    private DualGamePlayerController playerController;
    private DualGameAIController aiController;

    private JLabel playerMovesLabel;
    private JLabel aiMovesLabel;
    private JLabel turnLabel;

    private JPanel leftPanel;
    private JPanel rightPanel;


    private boolean playerTurn = true;
    private boolean gameEnded = false;

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


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0,100,150));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        turnLabel = new JLabel("Turno: Giocatore",SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 28));
        turnLabel.setForeground(Color.YELLOW);

        JPanel movesPanel = new JPanel(new GridLayout(1, 2,20,0));
        movesPanel.setOpaque(false);

        playerMovesLabel = new JLabel("Giocatore: 0 mosse",SwingConstants.CENTER);
        playerMovesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerMovesLabel.setForeground(Color.WHITE);

        aiMovesLabel = new JLabel("AI: 0 mosse",SwingConstants.CENTER);
        aiMovesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        aiMovesLabel.setForeground(Color.WHITE);

        movesPanel.add(playerMovesLabel);
        movesPanel.add(aiMovesLabel);

        topPanel.add(turnLabel, BorderLayout.NORTH);
        topPanel.add(movesPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1,2,5,0));
        centerPanel.setBackground(Color.BLACK);

        leftPanel = createLabeledPanel("👤 IL TUO TAVOLO", new Color(0, 120, 0));

        rightPanel = createLabeledPanel("🤖 TAVOLO IA", new Color(100,0,0));

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

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
        try{
            dualController.iniziaPartita();


            playerController = new DualGamePlayerController(dualController,this);
            aiController = new DualGameAIController(dualController,this);

            playerGui = new TriPeaksGUI(ModalitaGioco.UMANO,playerController,0.7);
            aiGui = new TriPeaksGUI(ModalitaGioco.IA, aiController,0.7);

            playerGui.updateDisplay(dualController.getPlayerGame());
            aiGui.updateDisplay(dualController.getAiGame());

            leftPanel.add(playerGui.getContentPane(),BorderLayout.CENTER);
            rightPanel.add(aiGui.getContentPane(),BorderLayout.CENTER);

            revalidate();
            repaint();

            System.out.println("✅ Partita dual inizializzata - Turni alternati attivi!");

        }catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nell'inizializzazione: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public void onPlayerMove(){
        if(gameEnded) return;

        dualController.incrementPlayerMoves();
        updateLabels();

        SwingUtilities.invokeLater(() -> {
            playerGui.updateDisplay(dualController.getPlayerGame());
        });

        if (dualController.getPlayerGame().isFinita()) {
            onPlayerFinished();
            return;
        }

        playerTurn = false;
        turnLabel.setText("🤖 TURNO: IA");
        turnLabel.setForeground(Color.RED);

        Timer timer = new Timer(1000, e->{
            turnoIA();
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void turnoIA() {
        if(gameEnded) return;

        aiController.faiMossaIA();
        dualController.incrementAiMoves();

        updateLabels();

        SwingUtilities.invokeLater(() -> {
            aiGui.updateDisplay(dualController.getAiGame());
        });

        if(dualController.getAiGame().isFinita()) {
            onAIFinished();
            return;
        }

        playerTurn = true;
        turnLabel.setText("🎮 TURNO: GIOCATORE");
        turnLabel.setForeground(Color.YELLOW);
    }

    private void updateLabels() {
        playerMovesLabel.setText("👤 Giocatore: " + dualController.getPlayerMoves() + " mosse");
        aiMovesLabel.setText("🤖 IA: " + dualController.getAiMoves() + " mosse");
    }

    private void onPlayerFinished() {
        gameEnded = true;
        dualController.setPlayerFinished(true);

        System.out.println("👤 Giocatore ha terminato!");

        continuaAI();
    }

    private void onAIFinished() {
        gameEnded = true;
        dualController.setAiFinished(true);

        System.out.println("🤖 IA ha terminato!");

        mostraRisultatoFinale();
    }

    private void continuaAI() {
        turnLabel.setText("🤖 IA CONTINUA DA SOLA...");
        turnLabel.setForeground(Color.ORANGE);

        Timer aiTimer = new Timer(1500,null);
        aiTimer.addActionListener(e->{
            if(dualController.getAiGame().isFinita()) {
                aiTimer.stop();
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

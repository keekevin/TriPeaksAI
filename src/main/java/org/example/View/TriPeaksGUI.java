package org.example.View;

import org.example.Controller.GameActions;
import org.example.Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class TriPeaksGUI extends JFrame {
    private TriPeaksGame game;
    private JPanel tableauPanel;
    private JPanel controlPanel;
    private JLabel scoreLabel;
    private JLabel deckLabel;
    private JLabel wasteLabel;
    private JButton newGameButton;
    private JButton drawButton;
    private Map<String, ImageIcon> cardImages;
    private ImageIcon backImage;
    private JButton deckCardButton;
    private JLabel wasteCardLabel;
    private ModalitaGioco modalita;
    private GameActions controller;




    private Map<Integer, CartaButton> cartaButtons;
    private static final int DEFAULT_CARD_WIDTH = 80;
    private static final int DEFAULT_CARD_HEIGHT = 110;
    private static final int DEFAULT_CARD_SPACING_X = 90;
    private static final int DEFAULT_CARD_SPACING_Y = 70;

    private int CARD_WIDTH;
    private int CARD_HEIGHT;
    private int CARD_SPACING_X;
    private int CARD_SPACING_Y;
    private double scaleFactor = 1.0;

    public TriPeaksGUI(ModalitaGioco modalita,GameActions controller,double scale,boolean isDualMode) {
        this.modalita = modalita;
        this.controller = controller;
        this.cartaButtons = new HashMap<>();
        this.cardImages = new HashMap<>();
        this.scaleFactor = scale;

        this.CARD_WIDTH = (int)(DEFAULT_CARD_WIDTH * scale);
        this.CARD_HEIGHT = (int)(DEFAULT_CARD_HEIGHT * scale);
        this.CARD_SPACING_X = (int)(DEFAULT_CARD_SPACING_X * scale);
        this.CARD_SPACING_Y = (int)(DEFAULT_CARD_SPACING_Y * scale);

        loadCardImages();

        setTitle("TriPeaks Solitaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        initComponents(!isDualMode);
    }

    public TriPeaksGUI(ModalitaGioco modalita,GameActions controller) {
        this.modalita = modalita;
        this.controller = controller;
        this.cartaButtons = new HashMap<>();
        this.cardImages = new HashMap<>();
        this.scaleFactor = 1.0;

        this.CARD_WIDTH = DEFAULT_CARD_WIDTH;
        this.CARD_HEIGHT = DEFAULT_CARD_HEIGHT;
        this.CARD_SPACING_X = DEFAULT_CARD_SPACING_X;
        this.CARD_SPACING_Y = DEFAULT_CARD_SPACING_Y;

        loadCardImages();


        setTitle("TriPeaks Solitaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void loadCardImages() {
        String[] semi = {"cuori","quadri","fiori","picche"};
        String[] valori = {"asso","2","3","4","5","6","7","8","9","10","jack","regina","re"};


        for(String seme: semi){
            for(int i = 0; i < valori.length; i++){
                String key = (i + 1) + "_" + seme;
                String filename = "/cards/" + valori[i] + "_" + seme + ".png";

                try{
                    var is = getClass().getResourceAsStream(filename);
                    BufferedImage img = ImageIO.read(is);

                    Image scaledImg = img.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
                    cardImages.put(key, new ImageIcon(scaledImg));
                } catch (IOException e){
                    System.err.println("Impossibile caricare l'immagine " + filename);
                    cardImages.put(key, createDefaultCardImage(i + 1, seme));
                }
            }
        }
        try {
            BufferedImage backImg =
                    ImageIO.read(getClass().getResourceAsStream("/cards/retro.png"));

            Image scaledBack =
                    backImg.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);

            backImage = new ImageIcon(scaledBack);
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine del retro");
            backImage = createDefaultBackImage();
        }
    }

    private ImageIcon createDefaultCardImage(int valore, String seme) {
        BufferedImage img = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        String valoreStr = valore == 1 ? "A" : valore == 11 ? "J" : valore == 12 ? "Q" : valore == 13 ? "K" : String.valueOf(valore);
        g.drawString(valoreStr, 10, 30);

        String simbolo = "";
        switch (seme) {
            case "cuori": simbolo = "♥"; g.setColor(Color.RED); break;
            case "quadri": simbolo = "♦"; g.setColor(Color.RED); break;
            case "fiori": simbolo = "♣"; break;
            case "picche": simbolo = "♠"; break;
        }
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(simbolo, CARD_WIDTH / 2 - 10, CARD_HEIGHT / 2 + 10);

        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon createDefaultBackImage() {
        BufferedImage img = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(0, 0, 150));
        g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect(5, 5, CARD_WIDTH - 10, CARD_HEIGHT - 10);

        g.dispose();
        return new ImageIcon(img);
    }



    private void initComponents(boolean showControls) {
        setLayout(new BorderLayout(10, 10));

        if (showControls) {
            controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            controlPanel.setBackground(new Color(0, 100, 0));

            scoreLabel = new JLabel("Punteggio: 0");
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

            deckLabel = new JLabel("Mazzo: 24");
            deckLabel.setForeground(Color.WHITE);
            deckLabel.setFont(new Font("Arial", Font.BOLD, 16));

            wasteLabel = new JLabel("Scarto: ");
            wasteLabel.setForeground(Color.WHITE);
            wasteLabel.setFont(new Font("Arial", Font.BOLD, 16));

            drawButton = new JButton("Pesca Carta");
            drawButton.addActionListener(e -> controller.pescaCarta());

            newGameButton = new JButton("Nuova Partita");
            newGameButton.addActionListener(e -> controller.nuovaPartita());

            controlPanel.add(scoreLabel);
            controlPanel.add(deckLabel);
            controlPanel.add(wasteLabel);
            controlPanel.add(drawButton);
            controlPanel.add(newGameButton);

            add(controlPanel, BorderLayout.NORTH);
        }

        tableauPanel = new JPanel(null){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255,255,255,100));
                g2d.fillRoundRect((int)(400*scaleFactor),(int)(550 * scaleFactor),(int)(100 * scaleFactor),(int)(150*scaleFactor),10,10);
                g2d.setColor(Color.WHITE);
                g2d.drawRoundRect((int)(400*scaleFactor),(int)(550*scaleFactor),(int)(100 * scaleFactor),(int) (150*scaleFactor),10,10);
                g2d.drawString("SCARTO",(int)(415 * scaleFactor),(int) (630 * scaleFactor));
            }
        };

        deckCardButton = new JButton(backImage);
        deckCardButton.setBounds((int)(280 * scaleFactor),(int)(550*scaleFactor), CARD_WIDTH, CARD_HEIGHT);
        deckCardButton.setBorderPainted(false);
        deckCardButton.setContentAreaFilled(false);
        deckCardButton.setFocusPainted(false);
        deckCardButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deckCardButton.addActionListener(e -> controller.pescaCarta());

        wasteCardLabel = new JLabel();
        wasteCardLabel.setBounds((int)(400*scaleFactor),(int)(550 * scaleFactor), CARD_WIDTH,CARD_HEIGHT);

        tableauPanel.add(deckCardButton);
        tableauPanel.add(wasteCardLabel);

        tableauPanel.setBackground(new Color(0, 120, 0));
        add(tableauPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        initComponents(true);
    }

    public void resetView() {
        for (Component c : tableauPanel.getComponents()) {
            if (c instanceof CartaButton) {
                tableauPanel.remove(c);
            }
        }
        cartaButtons.clear();
    }
    public void updateDisplay(TriPeaksGame game) {
        this.game = game;

        Set<Integer> idGiocabili = new HashSet<>();
        for (MossaValida m : game.getMosseValide()) {
            idGiocabili.add(m.getIdCarta());
        }

        if (!cartaButtons.isEmpty()) {
            for (Carta carta : game.getLayout()) {
                CartaButton btn = cartaButtons.get(carta.getId());
                if (btn != null) {
                    btn.aggiornaStato(
                            game.getPosizioni().get(carta.getPosizione()).isCoperta(),
                            idGiocabili.contains(carta.getId())
                    );
                }
            }
            Set<Integer> idNelLayout = new HashSet<>();
            for (Carta c : game.getLayout()) idNelLayout.add(c.getId());

            List<Integer> daRimuovere = new ArrayList<>();
            for (Map.Entry<Integer, CartaButton> entry : cartaButtons.entrySet()) {
                if (!idNelLayout.contains(entry.getKey())) {
                    tableauPanel.remove(entry.getValue());
                    daRimuovere.add(entry.getKey());
                }
            }
            for (Integer id : daRimuovere) cartaButtons.remove(id);

        } else {
            creaTuttiBottoni(game, idGiocabili);
        }

        aggiornaLabel(game);

        if (game.getCarteRimanentiMazzo() > 0) {
            if (deckCardButton.getParent() == null) tableauPanel.add(deckCardButton);
            deckCardButton.setEnabled(true);
        } else {
            if (deckCardButton.getParent() != null) tableauPanel.remove(deckCardButton);
        }

        Carta scarto = game.getCartaScartoObj();
        if (scarto != null) {
            String key = scarto.getValore() + "_" + scarto.getSeme();
            wasteCardLabel.setIcon(cardImages.get(key));
        }

        tableauPanel.revalidate();
        tableauPanel.repaint();

        if (scoreLabel != null) {
            if (game.haVinto()) {
                int scelta = JOptionPane.showConfirmDialog(this,
                        "🎉 HAI VINTO! Punteggio finale: " + game.getPunteggio() + "\n\nVuoi giocare ancora?",
                        "Vittoria!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (scelta == JOptionPane.YES_OPTION) {
                    controller.nuovaPartita();
                }else if (scelta == JOptionPane.NO_OPTION){
                    System.exit(0);
                }
            } else if (game.haPerso()) {
                int scelta = JOptionPane.showConfirmDialog(this,
                        "😞 Game Over! Punteggio: " + game.getPunteggio() + "\n\nVuoi riprovare?",
                        "Sconfitta", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (scelta == JOptionPane.YES_OPTION) {
                    controller.nuovaPartita();
                }else if (scelta == JOptionPane.NO_OPTION){
                    System.exit(0);
                }
            }
        }
    }

    private void aggiornaLabel(TriPeaksGame game) {
        if (scoreLabel != null) scoreLabel.setText("Punteggio: " + game.getPunteggio());
        if (deckLabel != null)  deckLabel.setText("Mazzo: " + game.getCarteRimanentiMazzo());
        if (wasteLabel != null) wasteLabel.setText("Scarto: " + game.getCartaScartoString());
        if (drawButton != null) drawButton.setEnabled(game.getCarteRimanentiMazzo() > 0);
    }

    private void creaTuttiBottoni(TriPeaksGame game, Set<Integer> idGiocabili) {
        List<Carta> carteOrdinate = new ArrayList<>(game.getLayout());
        carteOrdinate.sort((c1, c2) -> {
            Posizione pos1 = game.getPosizioni().get(c1.getPosizione());
            Posizione pos2 = game.getPosizioni().get(c2.getPosizione());
            return Integer.compare(pos2.getRiga(), pos1.getRiga());
        });

        for (Carta carta : carteOrdinate) {
            Posizione pos = game.getPosizioni().get(carta.getPosizione());

            int MAX_RIGA = 3;
            int LAST_ROW_SHIFT = (int)(-200 * scaleFactor);
            int offsetX = (int)(100 * scaleFactor);
            int offsetY = (int)(50 * scaleFactor);

            int x;
            int y = offsetY + pos.getRiga() * CARD_SPACING_Y;

            if (pos.getRiga() == MAX_RIGA) {
                x = (int)(120 * scaleFactor)
                        + pos.getColonna() * CARD_SPACING_X
                        + pos.getRiga() * (CARD_SPACING_X / 2)
                        + LAST_ROW_SHIFT;
            } else {
                x = offsetX + pos.getColonna() * CARD_SPACING_X;
            }

            boolean giocabile = idGiocabili.contains(carta.getId());

            CartaButton btn = new CartaButton(carta, pos.isCoperta(), giocabile, controller);
            btn.setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
            tableauPanel.add(btn);
            cartaButtons.put(carta.getId(), btn);
        }
    }


    private class CartaButton extends JButton {
        private Carta carta;
        private boolean coperta;
        private boolean giocabile;


        public CartaButton(Carta carta, boolean coperta, boolean giocabile,GameActions controller) {
            this.carta = carta;
            this.coperta = coperta;
            this.giocabile = giocabile;


            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            setContentAreaFilled(false);
            setOpaque(false);

            if (coperta) {
                setIcon(backImage);
                setEnabled(true);
            } else {
                String key = carta.getValore() + "_" + carta.getSeme();
                ImageIcon cardIcon = cardImages.get(key);

                if (cardIcon != null) {
                    setIcon(cardIcon);
                } else {
                    setIcon(createDefaultCardImage(carta.getValore(),carta.getSeme()));
                }

                if (giocabile) {
                    setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    setBorderPainted(true);
                     addActionListener(e -> {

                        if (controller != null) {
                            controller.giocaCarta(carta.getId());
                        } else {
                        }
                    });
                } else{
                }




                setCursor(giocabile
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
            }
        }


        public boolean isGiocabile() {
            return giocabile;
        }

        public void aggiornaStato(boolean nuovaCoperta, boolean nuovaGiocabile) {
            if (this.coperta == nuovaCoperta && this.giocabile == nuovaGiocabile) {
                return;
            }

            this.coperta = nuovaCoperta;
            this.giocabile = nuovaGiocabile;

            if (nuovaCoperta) {
                setIcon(backImage);
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                removeActionListeners();
            } else {
                String key = carta.getValore() + "_" + carta.getSeme();
                setIcon(cardImages.getOrDefault(key,
                        createDefaultCardImage(carta.getValore(), carta.getSeme())));

                if (nuovaGiocabile) {
                    setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    removeActionListeners();
                    addActionListener(e -> controller.giocaCarta(carta.getId()));
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    removeActionListeners();
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }

        private void removeActionListeners() {
            for (ActionListener al : getActionListeners()) {
                removeActionListener(al);
            }
        }
    }
}
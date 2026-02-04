package org.example.View;

import org.example.Model.ModalitaGioco;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuView extends JDialog {

    private JButton umanoButton;
    private JButton iaButton;
    private JButton umanoVsIaButton;
    private ModalitaGioco scelta;

    public MenuView(JFrame parent) {
        super(parent, "Seleziona modalità", true);
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(25, 118, 210),
                        0, getHeight(), new Color(13, 71, 161)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("<html><center>🎮<br>TriPeaks Solitaire</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Scegli la tua modalità di gioco", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 230, 255));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setOpaque(false);

        umanoButton = createStyledButton("👤 Gioco Io", "Gioca tu contro il solitario", new Color(76, 175, 80));
        iaButton = createStyledButton("🤖 Gioca l'IA", "Guarda l'IA risolvere il gioco", new Color(255, 152, 0));
        umanoVsIaButton = createStyledButton("🤜🤛 Duello", "Sfida l'IA: chi vince per primo?", new Color(244, 67, 54));

        umanoButton.addActionListener(e -> {
            scelta = ModalitaGioco.UMANO;
            dispose();
        });

        iaButton.addActionListener(e -> {
            scelta = ModalitaGioco.IA;
            dispose();
        });

        umanoVsIaButton.addActionListener(e -> {
            scelta = ModalitaGioco.UMANO_VS_IA;
            dispose();
        });

        buttonPanel.add(umanoButton);
        buttonPanel.add(iaButton);
        buttonPanel.add(umanoVsIaButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text, String description, Color baseColor) {
        JButton button = new JButton() {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = baseColor.darker().darker();
                } else if (hovered) {

                    int r = Math.min(255, baseColor.getRed() + 30);
                    int g = Math.min(255, baseColor.getGreen() + 30);
                    int b = Math.min(255, baseColor.getBlue() + 30);
                    bgColor = new Color(r, g, b);
                } else {
                    bgColor = baseColor;
                }

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(baseColor.darker().darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

                super.paintComponent(graphics);
            }

            public void setHovered(boolean h) {
                this.hovered = h;
                repaint();
            }
        };

        button.setLayout(new BorderLayout(10, 8));
        button.setBorder(new EmptyBorder(20, 25, 20, 25)); // Aumentato padding
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel mainLabel = new JLabel(text);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(255, 255, 255, 220));
        descLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel textPanel = new JPanel(new BorderLayout(5, 8));
        textPanel.setOpaque(false);
        textPanel.add(mainLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.CENTER);

        button.add(textPanel, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JButton btn = (JButton)evt.getSource();
                try {
                    btn.getClass().getMethod("setHovered", boolean.class).invoke(btn, true);
                } catch (Exception ignored) {}
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JButton btn = (JButton)evt.getSource();
                try {
                    btn.getClass().getMethod("setHovered", boolean.class).invoke(btn, false);
                } catch (Exception ignored) {}
            }
        });

        return button;
    }

    public JButton getUmanoButton() {
        return umanoButton;
    }

    public JButton getIaButton() {
        return iaButton;
    }

    public JButton getUmanoVsIaButton() {
        return umanoVsIaButton;
    }

    public ModalitaGioco getScelta() {
        return scelta;
    }
}
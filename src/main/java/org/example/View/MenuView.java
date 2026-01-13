package org.example.View;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JDialog {

    private JButton umanoButton;
    private JButton iaButton;


    public MenuView(JFrame parent) {
        super(parent,"Seleziona modalità",true);
        setSize(300,150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        JLabel label = new JLabel("Chi deve giocare?",SwingConstants.CENTER);
        label.setFont(new Font("Arial",Font.BOLD,16));

        umanoButton = new JButton("Gioco io");
        iaButton = new JButton("Gioca l'IA");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(umanoButton);
        buttonPanel.add(iaButton);

        add(label,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
    }

    public JButton getUmanoButton() {
        return umanoButton;
    }

    public JButton getIaButton() {
        return iaButton;
    }


}

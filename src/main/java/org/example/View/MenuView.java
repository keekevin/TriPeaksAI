package org.example.View;

import org.example.ModalitaGioco;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JDialog {

    private JButton umanoButton;
    private JButton iaButton;
    private JButton umanoVsIaButton;
    private ModalitaGioco scelta;


    public MenuView(JFrame parent) {
        super(parent,"Seleziona modalità",true);
        setSize(400,180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        JLabel label = new JLabel("Chi deve giocare?",SwingConstants.CENTER);
        label.setFont(new Font("Arial",Font.BOLD,16));

        umanoButton = new JButton("Gioco io");
        iaButton = new JButton("Gioca l'IA");
        umanoVsIaButton = new JButton("Umano vs IA");

        umanoButton.addActionListener(e ->{
            scelta = ModalitaGioco.UMANO;
            dispose();
        });

        iaButton.addActionListener(e ->{
            scelta = ModalitaGioco.IA;
            dispose();
        });

        umanoVsIaButton.addActionListener(e ->{
            scelta = ModalitaGioco.UMANO_VS_IA;
            dispose();
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(umanoButton);
        buttonPanel.add(iaButton);
        buttonPanel.add(umanoVsIaButton);

        add(label,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
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

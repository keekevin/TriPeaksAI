package org.example;

import org.example.Controller.GameController;
import org.example.Model.ModalitaGioco;
import org.example.View.DualGameGUI;
import org.example.View.MenuView;

import javax.swing.*;

public class MainGUI {


    public static void main(String[] args) {

        String os = System.getProperty("os.name").toLowerCase();
        String solverPath;

        if (os.contains("win")) {
            solverPath = "lib/dlv2.exe";
        } else if (os.contains("mac")) {
            solverPath = "lib/dlv2-macOS-64bit.mac_5";
        } else {
            solverPath = "lib/dlv2";
        }

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame();
            MenuView menu = new MenuView(frame);
            menu.setVisible(true);

            ModalitaGioco modalita = menu.getScelta();
            frame.dispose();

            if (modalita == null) {
                System.exit(0);
            }

            if (modalita == ModalitaGioco.UMANO_VS_IA) {
                DualGameGUI dualGUI = new DualGameGUI(solverPath);
                dualGUI.setVisible(true);
            } else {
                new GameController(solverPath, modalita);
            }
        });
    }
}

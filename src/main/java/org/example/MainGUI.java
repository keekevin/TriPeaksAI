package org.example;

import org.example.Controller.GameController;
import org.example.View.MenuView;

import javax.swing.*;

public class MainGUI {


    public static void main(String[] args) {

        // 🔹 Selezione sistema operativo
        String os = System.getProperty("os.name").toLowerCase();
        String solverPath;

        if (os.contains("win")) {
            solverPath = "lib/dlv2.exe";
        } else if (os.contains("mac")) {
            solverPath = "lib/dlv2-macOS-64bit.mac_5";
        } else {
            solverPath = "lib/dlv2";
        }

        // 🔹 Avvio GUI su Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // 1️⃣ Menu (modale)
            JFrame frame = new JFrame();
            MenuView menu = new MenuView(frame);
            menu.setVisible(true);   // BLOCCA finché non scegli

            ModalitaGioco modalita = menu.getScelta();
            frame.dispose();

            if (modalita == null) {
                System.exit(0);
            }

            // 2️⃣ Avvio gioco con modalità scelta
            if (modalita == ModalitaGioco.UMANO_VS_IA) {
                // ✨ NUOVA MODALITÀ - Turni alternati
                DualGameGUI dualGUI = new DualGameGUI(solverPath);
                dualGUI.setVisible(true);
            } else {
                // Modalità esistenti (UMANO o IA)
                new GameController(solverPath, modalita);
            }
        });
    }
}

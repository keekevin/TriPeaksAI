package org.example;

import org.example.Controller.GameController;

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
            new GameController(solverPath, ModalitaGioco.UMANO);
        });
    }
}

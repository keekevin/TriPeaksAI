package org.example.Controller;

import org.example.DualGameGUI;

public class DualGamePlayerController implements GameActions{

    private DualGameController dualController;

    private DualGameGUI gui;

    public DualGamePlayerController(DualGameController dualController, DualGameGUI gui) {
        this.dualController = dualController;
        this.gui = gui;
    }

    @Override
    public void giocaCarta(int idCarta) {
        if (!gui.isPlayerTurn()) {
            System.out.println("⚠️  Non è il tuo turno!");
            return;
        }

        System.out.println("👤 === GIOCATORE GIOCA CARTA ID: " + idCarta + " ===");

        dualController.getPlayerGame().giocaCartaById(idCarta);
        gui.onPlayerMove();  // Notifica la GUI
    }

    @Override
    public void pescaCarta() {
        if(!gui.isPlayerTurn()) {
            System.out.println("⚠️  Non è il tuo turno!");
            return;
        }

        System.out.println("👤 === GIOCATORE PESCA CARTA ===");

        boolean pescata = dualController.getPlayerGame().pescaCarta();

        if(pescata) {
            System.out.println("✓ Carta pescata");
        }

        gui.onPlayerMove();
    }

    @Override
    public void nuovaPartita() {}
}

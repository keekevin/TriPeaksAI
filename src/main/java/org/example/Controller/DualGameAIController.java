package org.example.Controller;

import org.example.Model.AzioneGioca;
import org.example.Model.AzionePesca;
import org.example.View.DualGameGUI;

public class DualGameAIController implements GameActions{

    private DualGameController dualController;
    private DualGameGUI gui;

    public DualGameAIController(DualGameController dualController, DualGameGUI gui) {
        this.dualController = dualController;
        this.gui = gui;
    }

    public void faiMossaIA() {
        Object azione = dualController.getAiGame().decidiAzioneASP();

        if (azione == null) {
            System.err.println("⚠️  IA: Nessuna azione disponibile!");
            return;
        }

        if (azione instanceof AzioneGioca) {
            AzioneGioca ag = (AzioneGioca) azione;
            giocaCarta(ag.getIdCarta());
        } else if (azione instanceof AzionePesca) {
            pescaCarta();
        } else {
            System.err.println("⚠️  IA: Azione sconosciuta: " + azione);
        }
    }

    @Override
    public void giocaCarta(int idCarta) {
        System.out.println("🤖 === IA GIOCA CARTA ID: " + idCarta + " ===");
        dualController.getAiGame().giocaCartaById(idCarta);
    }

    @Override
    public void pescaCarta() {
        System.out.println("🤖 === IA PESCA CARTA ===");
        dualController.getAiGame().pescaCarta();
    }

    @Override
    public void nuovaPartita() {
        // Non usato in modalità dual
    }

}

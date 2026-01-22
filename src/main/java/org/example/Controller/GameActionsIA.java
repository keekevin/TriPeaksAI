package org.example.Controller;

import org.example.AzioneGioca;
import org.example.AzionePesca;
import org.example.TriPeaksGame;

public class GameActionsIA {
    private final GameActions controller;
    private TriPeaksGame game;


    public GameActionsIA(GameActions controller, TriPeaksGame game) {
        this.controller = controller;
        this.game = game;
    }
//    public void faiMossa() {
//
//        if (game.isVinta() || game.isPersa()) return;
//
//        List<MossaValida> mosse = game.getMosseValide();
//
//        // 1️⃣ PESCA OBBLIGATA
//        if (mosse.isEmpty()) {
//            if (game.puoPescare()) {
//                controller.pescaCarta();
//            }
//            return;
//        }
//
//        // 2️⃣ PESCA SCELTA (per ora NON la facciamo)
//        // Qui in futuro deciderai se pescare anche con mosse disponibili
//
//        // 3️⃣ GIOCA UNA CARTA
//        MossaValida scelta = scegliMossa(mosse);
//        controller.giocaCarta(scelta.getIdCarta());
//    }

    public void faiMossa() {

        if (game.isFinita()) return;

        Object azione = game.decidiAzioneASP();

        if (azione == null) {
            System.err.println("⚠️ Nessuna azione restituita da ASP");
            return;
        }

        if (azione instanceof AzioneGioca) {
            AzioneGioca a = (AzioneGioca) azione;
            controller.giocaCarta(a.getIdCarta());
        }
        else if (azione instanceof AzionePesca) {
            controller.pescaCarta();
        }
        else {
            System.err.println("⚠️ Azione ASP sconosciuta: " + azione);
        }
    }

}

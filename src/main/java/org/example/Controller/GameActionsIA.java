package org.example.Controller;

import org.example.MossaValida;
import org.example.TriPeaksGame;

import java.util.List;

public class GameActionsIA {
    private final GameActions controller;
    private TriPeaksGame game;


    public GameActionsIA(GameActions controller, TriPeaksGame game) {
        this.controller = controller;
        this.game = game;
    }
    public void faiMossa() {

        if (game.isVinta() || game.isPersa()) return;

        List<MossaValida> mosse = game.getMosseValide();

        // 1️⃣ PESCA OBBLIGATA
        if (mosse.isEmpty()) {
            if (game.puoPescare()) {
                controller.pescaCarta();
            }
            return;
        }

        // 2️⃣ PESCA SCELTA (per ora NON la facciamo)
        // Qui in futuro deciderai se pescare anche con mosse disponibili

        // 3️⃣ GIOCA UNA CARTA
        MossaValida scelta = scegliMossa(mosse);
        controller.giocaCarta(scelta.getIdCarta());
    }

    private MossaValida scegliMossa(List<MossaValida> mosse) {
        return mosse.get(0); // per ora naive
    }

}

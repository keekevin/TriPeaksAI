package org.example.Controller;

import org.example.ModalitaGioco;
import org.example.TriPeaksGUI;
import org.example.TriPeaksGame;


public class GameController implements GameActions{
    private ModalitaGioco modalita;
    private TriPeaksGame game;
    private TriPeaksGUI view;
    private final String solverPath;
    private GameActionsIA ia;
    private boolean iaStaGiocando = false;


    public GameController(String solverPath, ModalitaGioco modalita){
        this.modalita = modalita;
        this.solverPath = solverPath;
        this.game = new TriPeaksGame(solverPath);
        this.view = new TriPeaksGUI(modalita,this);
        nuovaPartita();
        view.setVisible(true);

    }

@Override
public void giocaCarta(int idCarta) {
    System.out.println("=== IA GIOCA CARTA ID: " + idCarta + " ===");
    game.giocaCartaById(idCarta);
    view.updateDisplay(game);
}



    @Override
    public void pescaCarta() {
        System.out.println("=== PESCA CARTA ===");
        boolean pescata = game.pescaCarta();
        if (pescata) {
            System.out.println("✓ Carta pescata con successo");
        } else {
            System.err.println("✗ Impossibile pescare: mazzo vuoto");
        }
        view.updateDisplay(game);
    }

    @Override
    public void nuovaPartita() {
        System.out.println("=== NUOVA PARTITA ===");
        try {
            game = new TriPeaksGame(solverPath);
            game.inizia();
            view.updateDisplay(game);
            System.out.println("✓ Partita inizializzata");
            if (modalita == ModalitaGioco.IA) {
                ia = new GameActionsIA(this, game);
                turnoIAseNecessario(); // 👈 SOLO QUI
            }

        } catch (Exception e) {
            System.err.println("✗ Errore nell'inizializzazione:");
            e.printStackTrace();
        }
    }
    private void turnoIAseNecessario() {
        if (modalita == ModalitaGioco.IA && ia != null && !iaStaGiocando) {
            iaStaGiocando = true;

            new Thread(() -> {
                try {
                    while (!game.isFinita()) {
                        Thread.sleep(1000);
                        ia.faiMossa();
                    }
                } catch (InterruptedException ignored) {}
                iaStaGiocando = false;
            }).start();
        }
    }


}

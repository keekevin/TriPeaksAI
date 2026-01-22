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

//    @Override
//    public void giocaCarta(int idCarta) {
//        System.out.println("=== TENTATIVO DI GIOCARE CARTA ID: " + idCarta + " ===");
//
//        List<MossaValida> mosse = game.getMosseValide();
//        System.out.println("Mosse valide trovate: " + mosse.size());
//
//        for (MossaValida m : mosse) {
//            System.out.println("  - Mossa valida: ID=" + m.getIdCarta() + ", Valore=" + m.getValoreCarta());
//        }
//
//        boolean mossaTrovata = false;
//        for (MossaValida m : mosse) {
//            if (m.getIdCarta() == idCarta) {
//                System.out.println("✓ Mossa trovata! Eseguo...");
//                game.giocaMossa(m);
//                mossaTrovata = true;
//                break;
//            }
//        }
//
//        if (!mossaTrovata) {
//            System.err.println("✗ ERRORE: Mossa non trovata per carta ID " + idCarta);
//            System.err.println("Le mosse valide erano:");
//            for (MossaValida m : mosse) {
//                System.err.println("  - ID=" + m.getIdCarta());
//            }
//        }
//
//        view.updateDisplay(game);
//        System.out.println("=== FINE TENTATIVO ===\n");
//
//    }
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
                        Thread.sleep(4000);
                        ia.faiMossa();
                    }
                } catch (InterruptedException ignored) {}
                iaStaGiocando = false;
            }).start();
        }
    }


}

package org.example;

import it.unical.mat.embasp.base.Handler;
import org.example.Model.TriPeaksGame;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Handler handler;
    public static void main(String[] args) {
        try{
       // ====================================================================
        // CONFIGURAZIONE SISTEMA OPERATIVO
        // Scommenta SOLO la riga corrispondente al tuo sistema operativo:
        // ====================================================================

        // Se esegui su WINDOWS 64bit scommenta la seguente riga:
        // String solverPath = "lib/dlv2.exe";

        // Se esegui su LINUX 64bit scommenta la seguente riga:
        // String solverPath = "lib/dlv2";

        // Se esegui su MacOS 64bit scommenta la seguente riga:
        String solverPath = "lib/dlv2-macOS-64bit.mac_5";

        // ====================================================================

        //Crea il gioco
        TriPeaksGame game = new TriPeaksGame(solverPath);

        game.inizia();


        while(!game.haVinto() && !game.haPerso()) {
            System.out.println("\nPunteggio: " + game.getPunteggio());
            System.out.println("Carta scarto: " + game.getCartaScartoString());


            var mosseValide = game.getMosseValide();


            if (!mosseValide.isEmpty()) {
                System.out.println("Mosse valide disponibili: " + mosseValide.size());

                game.giocaMossa(mosseValide.get(0));
            } else {
                System.out.println("Nessuna mossa valida, pesco una carta...");
                game.pescaCarta();
            }
        }

            // Risultato finale
            if (game.haVinto()) {
                System.out.println("\n🎉 HAI VINTO! Punteggio finale: " + game.getPunteggio());
            } else {
                System.out.println("\n😞 HAI PERSO! Punteggio: " + game.getPunteggio());
            }
    } catch(Exception e){
            System.err.println("Errore durante l'esecuzione del gioco:");
            e.printStackTrace();}
    }
}
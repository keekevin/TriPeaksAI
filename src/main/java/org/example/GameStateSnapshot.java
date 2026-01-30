package org.example;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GameStateSnapshot {

    private List<CartaSnapshot> layout;
    private List<CartaSnapshot> mazzo;
    private CartaSnapshot cartaScarto;
    private List<PosizioneSnapshot> posizioni;
    private int idCartaCounter;


    public static GameStateSnapshot cattura(TriPeaksGame game){
        try{

            GameStateSnapshot snapshot = new GameStateSnapshot();


            Field layoutField = TriPeaksGame.class.getDeclaredField("layout");
            layoutField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Carta> layout = (List<Carta>) layoutField.get(game);


            Field mazzoField = TriPeaksGame.class.getDeclaredField("mazzo");
            mazzoField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Carta> mazzo = (List<Carta>) mazzoField.get(game);

            Field scartoField = TriPeaksGame.class.getDeclaredField("cartaScarto");
            scartoField.setAccessible(true);
            Carta scarto = (Carta) scartoField.get(game);

            Field posizioniField = TriPeaksGame.class.getDeclaredField("posizioni");
            posizioniField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Posizione> posizioni = (List<Posizione>) posizioniField.get(game);

            Field counterField = TriPeaksGame.class.getDeclaredField("idCartaCounter");
            counterField.setAccessible(true);
            snapshot.idCartaCounter = counterField.getInt(game);

            snapshot.layout = new ArrayList<>();
            for(Carta c : layout){
                snapshot.layout.add(CartaSnapshot.from(c));
            }


            snapshot.mazzo = new ArrayList<>();
            for(Carta c : mazzo){
                snapshot.mazzo.add(CartaSnapshot.from(c));
            }

            if(scarto != null){
                snapshot.cartaScarto = CartaSnapshot.from(scarto);
            }

            snapshot.posizioni = new ArrayList<>();
            for(Posizione p : posizioni){
                snapshot.posizioni.add(PosizioneSnapshot.from(p));
            }
        return snapshot;
        } catch (Exception e) {
            throw new RuntimeException("Errore nella cattura dello snapshot ", e);
        }
    }


    public static TriPeaksGame ripristina(GameStateSnapshot snapshot,String solverPath){
        try {
            TriPeaksGame game = new TriPeaksGame(solverPath);

            Field layoutField = TriPeaksGame.class.getDeclaredField("layout");
            layoutField.setAccessible(true);

            Field mazzoField = TriPeaksGame.class.getDeclaredField("mazzo");
            mazzoField.setAccessible(true);

            Field scartoField = TriPeaksGame.class.getDeclaredField("cartaScarto");
            scartoField.setAccessible(true);

            Field posizioniField = TriPeaksGame.class.getDeclaredField("posizioni");
            posizioniField.setAccessible(true);

            Field counterField = TriPeaksGame.class.getDeclaredField("idCartaCounter");
            counterField.setAccessible(true);

            // Ripristina layout
            List<Carta> layout = new ArrayList<>();
            for (CartaSnapshot cs : snapshot.layout) {
                layout.add(cs.toCarta());
            }
            layoutField.set(game, layout);

            // Ripristina mazzo
            List<Carta> mazzo = new ArrayList<>();
            for (CartaSnapshot cs : snapshot.mazzo) {
                mazzo.add(cs.toCarta());
            }
            mazzoField.set(game, mazzo);

            // Ripristina scarto
            if (snapshot.cartaScarto != null) {
                scartoField.set(game, snapshot.cartaScarto.toCarta());
            }

            // Ripristina posizioni
            List<Posizione> posizioni = new ArrayList<>();
            for (PosizioneSnapshot ps : snapshot.posizioni) {
                posizioni.add(ps.toPosizione());
            }
            posizioniField.set(game, posizioni);

            // Ripristina counter
            counterField.setInt(game, snapshot.idCartaCounter);

            return game;

        } catch (Exception e){
            throw new RuntimeException("Errore nel ripristino dello snapshot ", e);
    }
}

    private static class CartaSnapshot{
        int id,valore,posizione;
        String seme;

        static CartaSnapshot from(Carta c){
            CartaSnapshot cs = new CartaSnapshot();
            cs.id = c.getId();
            cs.valore = c.getValore();
            cs.seme = c.getSeme();
            cs.posizione = c.getPosizione();
            return cs;
        }

        Carta toCarta(){
            Carta c = new Carta();
            c.setId(id);
            c.setValore(valore);
            c.setSeme(seme);
            c.setPosizione(posizione);
            return c;
        }
    }


private static class PosizioneSnapshot{
        int id, riga, colonna, coperta;

        static PosizioneSnapshot from(Posizione p){
        PosizioneSnapshot ps = new PosizioneSnapshot();
        ps.id = p.getId();
        ps.riga = p.getRiga();
        ps.colonna = p.getColonna();
        ps.coperta = p.getCoperta();
        return ps;
        }

        Posizione toPosizione(){
            Posizione p = new Posizione();
            p.setId(id);
            p.setRiga(riga);
            p.setColonna(colonna);
            p.setCoperta(coperta);
            return p;
        }
    }
}


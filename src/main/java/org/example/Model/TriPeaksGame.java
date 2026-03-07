package org.example.Model;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.*;

public class TriPeaksGame {
    private Handler handler;
    private List<Carta> layout;      //CARTE SUL TAVOLO
    private List<Carta> mazzo;       //CARTE DA PESCARE
    private Carta cartaScarto;     //CARTA IN CIMA AGLI SCARTI
    private List<Posizione> posizioni;
    private int punteggio;
    private int idCartaCounter;
    private Map<Integer,List<Integer>> coperture;


    public TriPeaksGame(String solverPath){
        try{
            DLV2DesktopService service = new DLV2DesktopService(solverPath);
            this.handler = new DesktopHandler(service);

            ASPMapper.getInstance().registerClass(MossaValida.class);
            ASPMapper.getInstance().registerClass(AzioneGioca.class);
            ASPMapper.getInstance().registerClass(AzionePesca.class);
            ASPMapper.getInstance().registerClass(Carta.class);
            ASPMapper.getInstance().registerClass(Posizione.class);


            this.layout = new ArrayList<>();
            this.mazzo = new ArrayList<>();
            this.posizioni = new ArrayList<>();
            this.punteggio = 0;
            this.idCartaCounter = 0;
            this.coperture = inizializzaCoperture();
        } catch(Exception e){
            System.err.println("Errore nell'inizializzazione di EMBASP: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossibile inizializzare il gioco", e);
        }
    }

    private Map<Integer, List<Integer>> inizializzaCoperture() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(0,Arrays.asList(1,2));
        map.put(3, Arrays.asList(6, 7));
        map.put(4, Arrays.asList(7, 8));
        map.put(5, Arrays.asList(8, 15));
        map.put(9,Arrays.asList(10,11));
        map.put(18, Arrays.asList(19,20));
        map.put(12, Arrays.asList(15, 16));
        map.put(13, Arrays.asList(16, 17));
        map.put(14, Arrays.asList(17, 24));
        map.put(21, Arrays.asList(24, 25));
        map.put(22, Arrays.asList(25, 26));
        map.put(23, Arrays.asList(26, 27));
        map.put(1, Arrays.asList(3, 4));
        map.put(2, Arrays.asList(4, 5));
        map.put(10, Arrays.asList(12, 13));
        map.put(11, Arrays.asList(13, 14));
        map.put(19, Arrays.asList(21, 22));
        map.put(20, Arrays.asList(22, 23));
        return map;
    }



    public void inizia()throws Exception{
        try {
            List<Carta> mazzoCompleto = creaMazzoCompleto();
            Collections.shuffle(mazzoCompleto);
            creaLayout(mazzoCompleto);

            for(int i = 28 ; i < mazzoCompleto.size() ; i++){
                mazzo.add(mazzoCompleto.get(i));
            }

            if(!mazzo.isEmpty()){
                cartaScarto = mazzo.remove(0);
                cartaScarto.setPosizione(-1);
            }

            System.out.println("Gioco inizializzato!");
            System.out.println("Carte nel layout: " + layout.size());
            System.out.println("Carte nel mazzo: " + mazzo.size());

        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del gioco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Carta> creaMazzoCompleto(){
        List<Carta> mazzo = new ArrayList<>();
        String[] semi = {"cuori","quadri","fiori","picche"};

        for(String seme: semi){
            for(int valore = 1; valore <= 13; valore++){
                Carta carta = new Carta();
                carta.setId(idCartaCounter++);
                carta.setValore(valore);
                carta.setSeme(seme);
                carta.setPosizione(-2);
                mazzo.add(carta);
            }
        }

        return mazzo;
    }


    private void creaLayout(List<Carta> mazzo){
        int [][] coordinate = {
                {0,1},
                {1,0},{1,2},
                {2,0},{2,1},{2,2},
                {3,0},{3,1},{3,2},

                {0,4},
                {1,3},{1,5},
                {2,3},{2,4},{2,5},
                {3,3},{3,4},{3,5},

                {0,7},
                {1,6},{1,8},
                {2,6},{2,7},{2,8},
                {3,6},{3,7},{3,8},{3,9}
        };

        for(int i = 0; i < 28; i++){
            Carta carta = mazzo.get(i);
            carta.setPosizione(i);
            layout.add(carta);

            Posizione pos = new Posizione();
            pos.setId(i);
            pos.setRiga(coordinate[i][0]);
            pos.setColonna(coordinate[i][1]);


            boolean iscoperta = coperture.containsKey(i);
            pos.setCoperta(iscoperta ? 1 : 0);

            posizioni.add(pos);
        }
    }

    private AnswerSet eseguiASP() throws Exception {
        handler.removeAll();
        InputProgram program = new ASPInputProgram();

        for (Carta carta : layout) {
            program.addObjectInput(carta);
        }

        for (Posizione pos : posizioni) {
            program.addObjectInput(pos);
        }

        // Aggiunge le relazioni di copertura dalla mappa già esistente
        for (Map.Entry<Integer, List<Integer>> entry : coperture.entrySet()) {
            for (Integer posLibera : entry.getValue()) {
                program.addProgram(String.format("richiede_libera(%d,%d).%n", entry.getKey(), posLibera));
            }
        }

        program.addProgram("carta_scarto(" + cartaScarto.getValore() + ").");

        if (!mazzo.isEmpty()) {
            program.addProgram("puo_pescare.");
        }

        String aspFilePath = "programs/tripeaks.asp";
        if (!new java.io.File(aspFilePath).exists()) {
            System.err.println("File ASP non trovato: " + new java.io.File(aspFilePath).getAbsolutePath());
        }

        program.addFilesPath(aspFilePath);
        handler.addProgram(program);

        Output output = handler.startSync();
        AnswerSets answers = (AnswerSets) output;

        if (answers.getAnswersets().isEmpty()) {
            System.err.println("⚠️ Nessun answer set restituito dal solver!");
            return null;
        }

        return answers.getAnswersets().get(0);
    }

    public Object decidiAzioneASP() {
       try {
           AnswerSet as = eseguiASP();
           if (as == null) return null;

           for(Object atom : as.getAtoms()){
               if(atom instanceof AzioneGioca)return atom;
               if(atom instanceof AzionePesca)return atom;
           }
           System.err.println("⚠️ Nessuna azione trovata nell'answer set!");
           return null;
       } catch (Exception e) {
           System.err.println("❌ Errore in decidiAzioneASP: " + e.getMessage());
           return null;
       }
    }


    public List<MossaValida> getMosseValide() {
       try{
           AnswerSet as = eseguiASP();
           List<MossaValida> mosse = new ArrayList<>();
           if(as == null) return mosse;

           for(Object obj : as.getAtoms()){
               if(obj instanceof MossaValida){
                   mosse.add((MossaValida) obj);
               }
           }
           return mosse;
       } catch (Exception e) {
           System.err.println("❌ Errore in getMosseValide: " + e.getMessage());
           return new ArrayList<>();
       }
    }


    public void giocaMossa(MossaValida mossa){
        Carta cartaDaGiocare = null;
        for(Carta c:layout){
            if(c.getId() == mossa.getIdCarta()){
                cartaDaGiocare = c;
                break;
            }
        }

        if(cartaDaGiocare == null){
            System.err.println("Errore: carta non trovata!");
            return;
        }

        layout.remove(cartaDaGiocare);

        int pos = cartaDaGiocare.getPosizione();
        Posizione posizione = posizioni.get(pos);
        posizione.setCoperta(0);

        scopriCarteSotto(pos);
        cartaScarto = cartaDaGiocare;
        punteggio += 10;

        System.out.println("Giocata carta: " + getNomeCarta(cartaDaGiocare));
    }


    private void scopriCarteSotto(int posizioneRimossa){

        for (Map.Entry<Integer, List<Integer>> entry : coperture.entrySet()) {
            int cartaCoperta = entry.getKey();
            List<Integer> coprenti = entry.getValue();

            if (!coprenti.contains(posizioneRimossa)) continue;

            boolean sulLayout = false;
            for (Carta c : layout) {
                if (c.getPosizione() == cartaCoperta) {
                    sulLayout = true;
                    break;
                }
            }
            if (!sulLayout) continue;

            boolean ancoraCoperta = false;
            for (Integer coprente : coprenti) {
                for (Carta c : layout) {
                    if (c.getPosizione() == coprente) {
                        ancoraCoperta = true;
                        break;
                    }
                }
                if (ancoraCoperta) break;
            }

            if (!ancoraCoperta) {
                posizioni.get(cartaCoperta).setCoperta(0);
            }
        }
    }
    public boolean pescaCarta(){
        if(mazzo.isEmpty()){
            return false;
        }

        cartaScarto = mazzo.remove(0);
        cartaScarto.setPosizione(-1);
        punteggio = Math.max(0, punteggio -5);

        return true;
    }


    public boolean haVinto(){
        return layout.isEmpty();
    }

    public boolean haPerso(){
        return mazzo.isEmpty() && getMosseValide().isEmpty();
    }

    public int getPunteggio(){
        return punteggio;
    }

    public String getCartaScartoString(){
        return getNomeCarta(cartaScarto);
    }

    public int getCarteRimanentiMazzo(){
        return mazzo.size();
    }

    public List<Carta> getLayout() {
        return layout;
    }

    public List<Posizione> getPosizioni() {
        return posizioni;
    }

    private String getNomeCarta(Carta carta) {
        String nomeValore;
        switch (carta.getValore()) {
            case 1: nomeValore = "Asso"; break;
            case 11: nomeValore = "Jack"; break;
            case 12: nomeValore = "Regina"; break;
            case 13: nomeValore = "Re"; break;
            default: nomeValore = String.valueOf(carta.getValore());
        }
        return nomeValore + " di " + carta.getSeme();
    }

    public Carta getCartaScartoObj(){
        return cartaScarto;
    }


    public boolean isVinta() {
        return haVinto();
    }

    public boolean isPersa() {
        return haPerso();
    }

    public boolean isFinita() {
        return isVinta() || isPersa();
    }

    public boolean puoPescare() {
        return !mazzo.isEmpty();
    }


    public void giocaCartaById(int idCarta) {
        for (Carta c : layout) {
            if (c.getId() == idCarta) {
                MossaValida m = new MossaValida();
                m.setIdCarta(idCarta);
                m.setValoreCarta(c.getValore());
                giocaMossa(m);
                return;
            }
        }
        throw new IllegalStateException("Carta non trovata: " + idCarta);
    }

}

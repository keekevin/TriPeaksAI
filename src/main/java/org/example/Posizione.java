package org.example;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("posizione")
public class Posizione {

    @Param(0)
    private int id;

    @Param(1)
    private int riga;

    @Param(2)
    private int colonna;

    @Param(3)
    private String copertaString; // ← Cambiato da boolean a String

    // Campo interno per gestire il valore come boolean
    private boolean coperta;

    public Posizione() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRiga() {
        return riga;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public int getColonna() {
        return colonna;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }

    // Getter e Setter per copertaString (usato da EMBASP)
    public String getCopertaString() {
        return copertaString;
    }

    public void setCopertaString(String copertaString) {
        this.copertaString = copertaString;
        // Converti la stringa in boolean
        this.coperta = "true".equals(copertaString);
    }

    // Getter e Setter per coperta (usato dal resto del codice)
    public boolean isCoperta() {
        return coperta;
    }

    public void setCoperta(boolean coperta) {
        this.coperta = coperta;
        this.copertaString = String.valueOf(coperta);
    }

    @Override
    public String toString() {
        return "Posizione{" +
                "id=" + id +
                ", riga=" + riga +
                ", colonna=" + colonna +
                ", coperta=" + coperta +
                '}';
    }
}
package org.example.Model;

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
    private int coperta; // 1 = coperta, 0 = scoperta

    public Posizione() {}

    public Posizione(int id, int riga, int colonna, boolean coperta) {
        this.id = id;
        this.riga = riga;
        this.colonna = colonna;
        this.coperta = coperta ? 1 : 0;
    }

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

    public int getCoperta() {
        return coperta;
    }

    public void setCoperta(int coperta) {
        this.coperta = coperta;
    }

    public boolean isCoperta() {
        return coperta == 1;
    }


    @Override
    public String toString() {
        return "Posizione{" +
                "id=" + id +
                ", riga=" + riga +
                ", colonna=" + colonna +
                ", coperta=" + (coperta == 1) +
                '}';
    }
}

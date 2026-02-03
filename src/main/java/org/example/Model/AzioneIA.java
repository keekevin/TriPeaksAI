package org.example.Model;

public class AzioneIA {
    public enum Tipo{GIOCA,PESCA}
    private Tipo tipo;
    private int idCarta;

    public AzioneIA(int idCarta, Tipo tipo) {
        this.idCarta = idCarta;
        this.tipo = tipo;
    }

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}

package org.example.Model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("mossa_valida")
public class MossaValida {

    @Param(0)
    private int idCarta;

    @Param(1)
    private int valoreCarta;

    public MossaValida(){}

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }

    public int getValoreCarta() {
        return valoreCarta;
    }

    public void setValoreCarta(int valoreCarta) {
        this.valoreCarta = valoreCarta;
    }

    public MossaValida(int idCarta) {
        this.idCarta = idCarta;
    }


    @Override
    public String toString() {
        return "MossaValida{" +
                "idCarta=" + idCarta +
                ", valoreCarta=" + valoreCarta +
                '}';
    }
}

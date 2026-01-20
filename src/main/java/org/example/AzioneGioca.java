package org.example;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("azione_gioca")
public class AzioneGioca {
    @Param(0)
    private int idCarta;

    public AzioneGioca(){}

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }
}

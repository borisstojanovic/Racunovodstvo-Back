package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class KalkulacijaArtikal extends Artikal {

    @Column
    private Double marzaProcenat;
    @Column
    private Double marza;
    @Column
    private Double prodajnaOsnovica;
    @Column
    private Double porezProcenat;
    @Column
    private Double porez;
    @Column
    private Double prodajnaCena;
    @Column
    private Double osnovica;
    @Column
    private Double ukupnaProdajnaVrednost;
}

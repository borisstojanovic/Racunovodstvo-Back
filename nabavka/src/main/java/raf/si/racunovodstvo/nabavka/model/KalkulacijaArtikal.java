package raf.si.racunovodstvo.nabavka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Kalkulacija")
    private Kalkulacija kalkulacija;
}

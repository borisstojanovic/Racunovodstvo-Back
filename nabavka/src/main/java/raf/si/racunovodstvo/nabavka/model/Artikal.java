package raf.si.racunovodstvo.nabavka.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Inheritance
@Entity
@Getter
@Setter
public abstract class Artikal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artikalId;
    @Column(nullable = false, unique = true)
    private String sifraArtikla;
    @Column(nullable = false)
    private String nazivArtikla;
    @Column(nullable = false)
    private String jedinicaMere;
    @Column(nullable = false)
    private Integer kolicina;
    @Column(nullable = false)
    private Double nabavnaCena;
    @Column(nullable = false)
    private Double rabatProcenat;
    @Column(nullable = false)
    private Double rabat;
    @Column(nullable = false)
    private Double nabavnaCenaPosleRabata;
    @Column(nullable = false)
    private Double ukupnaNabavnaVrednost;


}

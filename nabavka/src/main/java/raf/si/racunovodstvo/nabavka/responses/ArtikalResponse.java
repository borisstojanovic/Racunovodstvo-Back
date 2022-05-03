package raf.si.racunovodstvo.nabavka.responses;

import lombok.Data;

@Data
public class ArtikalResponse {

    private Long artikalId;
    private String sifraArtikla;
    private String nazivArtikla;
    private String jedinicaMere;
    private Integer kolicina;
    private Double nabavnaCena;
    private Double rabatProcenat;
    private Double rabat;
    private Double nabavnaCenaPosleRabata;
    private Double ukupnaNabavnaVrednost;
    private Double marzaProcenat;
    private Double marza;
    private Double prodajnaOsnovica;
    private Double porezProcenat;
    private Double porez;
    private Double prodajnaCena;
    private Double osnovica;
    private Double ukupnaProdajnaVrednost;
}

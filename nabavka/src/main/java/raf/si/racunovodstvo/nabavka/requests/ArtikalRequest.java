package raf.si.racunovodstvo.nabavka.requests;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ArtikalRequest {

    private Long artikalId;
    @NotBlank
    private String sifraArtikla;
    @NotBlank
    private String nazivArtikla;
    @NotBlank
    private String jedinicaMere;
    @NotNull
    private Integer kolicina;
    @NotNull
    private Double nabavnaCena;
    @NotNull
    private Double rabatProcenat;
    @NotNull
    private Double marzaProcenat;
    @NotNull
    private Double prodajnaCena;
    private boolean aktivanZaProdaju;
    private Double porezProcenat;

    @AssertTrue(message = "Polja ne mogu biti prazna")
    public boolean isValid() {
        return !aktivanZaProdaju || porezProcenat != null;
    }
}

package raf.si.racunovodstvo.nabavka.responses;

import lombok.Data;

import java.util.Date;

@Data
public class KonverzijaResponse {
    Long konverzijaId;
    String brojKonverzije;
    Date datum;
    Long dobavljacId;
    Long lokacijaId;
    Double troskoviNabavkeSum;
    Double fakturnaCena;
    Double nabavnaCena;
    String valuta;
    String komentar;
}

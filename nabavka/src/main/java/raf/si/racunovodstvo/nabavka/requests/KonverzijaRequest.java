package raf.si.racunovodstvo.nabavka.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Getter
@Setter
public class KonverzijaRequest {

    private Long id;
    @NotNull(message = "brojKonverzije je obavezna")
    private String brojKonverzije;
    @NotNull(message = "datum je obavezna")
    private Date datum;
    @NotNull(message = "dobavljacId je obavezna")
    private Long dobavljacId;
    @NotNull(message = "lokacijaId je obavezna")
    private Long lokacijaId;
    @NotNull(message = "valuta je obavezna")
    private String valuta;
    private String komentar;

}

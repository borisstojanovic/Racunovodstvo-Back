package raf.si.racunovodstvo.nabavka.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LokacijaRequest {

    private Long lokacijaId;
    private String naziv;
    private String adresa;

    @AssertTrue(message = "Polja ne mogu biti prazna")
    public boolean isValid() {
        return (lokacijaId != null && (Strings.isBlank(naziv) && Strings.isBlank(adresa))) ||
            (lokacijaId == null && (Strings.isNotBlank(naziv) && Strings.isNotBlank(adresa)));
    }
}

package raf.si.racunovodstvo.nabavka.auditor;

import io.jsonwebtoken.lang.Collections;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;

import javax.persistence.PrePersist;

public class BaznaKonverzijaKalkulacijaAuditor {

    @PrePersist
    private void beforeSave(BaznaKonverzijaKalkulacija baznaKonverzijaKalkulacija) {
        if (!Collections.isEmpty(baznaKonverzijaKalkulacija.getTroskoviNabavke())) {
            baznaKonverzijaKalkulacija.getTroskoviNabavke()
                                      .forEach(troskoviNabavke -> troskoviNabavke.setBaznaKonverzijaKalkulacija(baznaKonverzijaKalkulacija));
        }
    }
}

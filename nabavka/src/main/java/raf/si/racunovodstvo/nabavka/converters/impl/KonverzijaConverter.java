package raf.si.racunovodstvo.nabavka.converters.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class KonverzijaConverter implements IConverter<List<Konverzija>, Page<KonverzijaResponse>> {

    public Page<KonverzijaResponse> convert(List<Konverzija> konverzija) {
        List<KonverzijaResponse> responses = new ArrayList<>();
        for (Konverzija currKonverzija : konverzija) {
            KonverzijaResponse response = new KonverzijaResponse();

            response.setKonverzijaId(currKonverzija.getId());
            response.setBrojKonverzije(currKonverzija.getBrojKonverzije());
            response.setDatum(currKonverzija.getDatum());
            response.setKomentar(currKonverzija.getKomentar());
            response.setDobavljacId(currKonverzija.getDobavljacId());
            response.setLokacijaId(currKonverzija.getLokacija().getLokacijaId());
            Double troskoviNabavke = 0.0;
            for(TroskoviNabavke tNabavke : currKonverzija.getTroskoviNabavke()){
                troskoviNabavke+=tNabavke.getCena();
            }
            response.setTroskoviNabavke(troskoviNabavke);
            response.setNabavnaCena(currKonverzija.getNabavnaCena());
            response.setFakturnaCena(currKonverzija.getFakturnaCena());
            response.setValuta(currKonverzija.getValuta());

            responses.add(response);
        }
        return new PageImpl<>(responses);
    }
}
package raf.si.racunovodstvo.nabavka.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.IKonverzijaService;

import java.util.ArrayList;
import java.util.List;

@Component
public class KonverzijaConverter {

    @Autowired
    private IKonverzijaService iKonverzijaService;



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
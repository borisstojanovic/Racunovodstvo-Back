package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.*;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.services.impl.KonverzijaService;

@Component
public class ArtikalConverter implements IConverter<ArtikalRequest, Artikal> {

    private final ModelMapper modelMapper;

    @Autowired
    private KonverzijaService konverzijaService;

    public ArtikalConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Artikal convert(ArtikalRequest source) {
        Artikal converted;
        if (source.isAktivanZaProdaju()) {
            KalkulacijaArtikal mapped = modelMapper.map(source, KalkulacijaArtikal.class);
            calculateCommonFields(mapped);
            calculateKalkulacijaFields(mapped);
            converted = mapped;
            // kalkulacija
            return null;
        } else {

            converted = modelMapper.map(source, KonverzijaArtikal.class);
            calculateCommonFields(converted);
            KonverzijaArtikal konverzijaArtikal = (KonverzijaArtikal) converted;
            Konverzija konverzija = konverzijaService.findById(source.getKonvezijaKalkulacijaId()).get();
            konverzijaArtikal.setKonverzija(konverzija);

            if(konverzijaService.findById(konverzijaArtikal.getKonverzija().getId()).isPresent()){

                Double ukupnaFakturna = konverzija.getFakturnaCena() + konverzijaArtikal.getUkupnaNabavnaVrednost();
                konverzija.setFakturnaCena(ukupnaFakturna);
                Double ukupniTroskoviNabavke = 0.0;
                for(TroskoviNabavke troskoviNabavke : konverzija.getTroskoviNabavke()){
                    ukupniTroskoviNabavke+=troskoviNabavke.getCena();
                }
                konverzija.setNabavnaCena(ukupniTroskoviNabavke+ukupnaFakturna);

                konverzijaService.save(konverzija);
            }

            return konverzijaArtikal;
        }
    }

    private void calculateKalkulacijaFields(KalkulacijaArtikal artikal) {
        artikal.setMarza(artikal.getNabavnaCenaPosleRabata() * artikal.getMarzaProcenat() / 100);
        artikal.setProdajnaOsnovica(artikal.getNabavnaCenaPosleRabata() + artikal.getMarza());
        artikal.setPorez(artikal.getProdajnaOsnovica() * artikal.getPorezProcenat() / 100);
        artikal.setOsnovica(artikal.getProdajnaOsnovica() * artikal.getKolicina());
        artikal.setUkupnaProdajnaVrednost(artikal.getProdajnaCena() * artikal.getKolicina());

    }

    private void calculateCommonFields(Artikal artikal) {
        artikal.setRabat(artikal.getNabavnaCena() * artikal.getRabatProcenat());
        artikal.setNabavnaCenaPosleRabata(artikal.getNabavnaCena() - artikal.getRabat());
        artikal.setUkupnaNabavnaVrednost(artikal.getNabavnaCenaPosleRabata() * artikal.getKolicina());
    }
}

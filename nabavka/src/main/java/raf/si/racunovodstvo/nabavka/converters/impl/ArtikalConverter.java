package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.model.KonverzijaArtikal;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;

@Component
public class ArtikalConverter implements IConverter<ArtikalRequest, Artikal> {

    private final ModelMapper modelMapper;

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
        } else {
            converted = modelMapper.map(source, KonverzijaArtikal.class);
            calculateCommonFields(converted);
        }
        return converted;
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

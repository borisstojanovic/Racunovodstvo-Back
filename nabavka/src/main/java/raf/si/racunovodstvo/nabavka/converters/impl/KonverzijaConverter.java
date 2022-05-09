package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    public KonverzijaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Page<KonverzijaResponse> convert(List<Konverzija> konverzija) {
        List<KonverzijaResponse> responses = new ArrayList<>();
        for (Konverzija currKonverzija : konverzija) {
            KonverzijaResponse response = modelMapper.map(currKonverzija, KonverzijaResponse.class);

            response.setKonverzijaId(currKonverzija.getId());
            if (currKonverzija.getLokacija() != null) {
                response.setLokacijaId(currKonverzija.getLokacija().getLokacijaId());
            }
            Double troskoviNabavke = currKonverzija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
            response.setTroskoviNabavkeSum(troskoviNabavke);

            responses.add(response);
        }
        return new PageImpl<>(responses);
    }
}

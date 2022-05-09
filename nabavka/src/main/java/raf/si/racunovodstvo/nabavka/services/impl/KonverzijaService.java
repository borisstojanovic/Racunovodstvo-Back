package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KonverzijaConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.IKonverzijaService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KonverzijaService implements IKonverzijaService {

    private final KonverzijaRepository konverzijaRepository;
    private final LokacijaRepository lokacijaRepository;
    private final IConverter<List<Konverzija>, Page<KonverzijaResponse>> konverzijaConverter;

    @Autowired
    public KonverzijaService(KonverzijaRepository konverzijaRepository, LokacijaRepository lokacijaRepository, KonverzijaConverter konverzijaConverter) {
        this.konverzijaRepository = konverzijaRepository;
        this.lokacijaRepository = lokacijaRepository;
        this.konverzijaConverter = konverzijaConverter;
    }

    @Override
    public Page<KonverzijaResponse> findAll(Specification<Konverzija> spec, Pageable pageSort) {
        Page<Konverzija> page = konverzijaRepository.findAll(spec, pageSort);
        return konverzijaConverter.convert(page.getContent());
    }


    @Override
    public <S extends Konverzija> S save(S var1) {
        return konverzijaRepository.save(var1);
    }

    @Override
    public Optional<Konverzija> findById(Long id) {
        return konverzijaRepository.findById(id);
    }

    @Override
    public List<Konverzija> findAll() {
        return konverzijaRepository.findAll();
    }

    public void deleteById(Long id) {
        konverzijaRepository.deleteById(id);
    }

    public Konverzija saveKonverzija(KonverzijaRequest konverzijaRequest) {
        Konverzija currKonverzija = new Konverzija();

        currKonverzija.setBrojKonverzije(konverzijaRequest.getBrojKonverzije());
        currKonverzija.setDatum(new Date());
        currKonverzija.setKomentar(konverzijaRequest.getKomentar());
        currKonverzija.setDobavljacId(konverzijaRequest.getDobavljacId());
        currKonverzija.setLokacija(null);
        currKonverzija.setNabavnaCena(0.0);
        currKonverzija.setFakturnaCena(0.0);
        currKonverzija.setValuta(konverzijaRequest.getValuta());

        return konverzijaRepository.save(currKonverzija);
    }

    @Override
    public Konverzija increaseNabavnaCena(Long konverzijaId, Double increaseBy) {
        Optional<Konverzija> optionalKonverzija = findById(konverzijaId);
        if (optionalKonverzija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Konverzija konverzija = optionalKonverzija.get();
        Double ukupnaFakturnaCena = konverzija.getFakturnaCena() + increaseBy;
        konverzija.setFakturnaCena(ukupnaFakturnaCena);
        Double ukupniTroskoviNabavke = konverzija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
        konverzija.setNabavnaCena(ukupniTroskoviNabavke + ukupnaFakturnaCena);
        return konverzijaRepository.save(konverzija);
    }
}

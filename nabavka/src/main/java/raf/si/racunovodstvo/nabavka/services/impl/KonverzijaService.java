package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.converter.KonverzijaConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.IKonverzijaService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class
KonverzijaService implements IKonverzijaService {

    private final KonverzijaRepository konverzijaRepository;
    private final LokacijaRepository lokacijaRepository;

    @Lazy
    private KonverzijaConverter konverzijaConverter;

    @Autowired
    public KonverzijaService(KonverzijaRepository konverzijaRepository, LokacijaRepository lokacijaRepository) {
        this.konverzijaRepository = konverzijaRepository;
        this.lokacijaRepository = lokacijaRepository;
    }

    @Override
    public Page<KonverzijaResponse> findAll(Specification<Konverzija> spec) {
        Page<Konverzija> page = konverzijaRepository.findAll(spec);
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

    public Konverzija saveKonverzija(KonverzijaRequest konverzijaRequest){
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
}

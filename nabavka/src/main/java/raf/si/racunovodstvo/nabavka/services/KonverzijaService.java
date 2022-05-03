package raf.si.racunovodstvo.nabavka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.converter.KonverzijaConverter;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.impl.IKonverzijaService;

import java.util.List;
import java.util.Optional;

@Service
public class
KonverzijaService implements IKonverzijaService {

    private final KonverzijaRepository konverzijaRepository;

    @Lazy
    private KonverzijaConverter konverzijaConverter;

    @Autowired
    public KonverzijaService(KonverzijaRepository konverzijaRepository) {
        this.konverzijaRepository = konverzijaRepository;
    }

    @Override
    public Page<KonverzijaResponse> findAll(Specification<Konverzija> spec) {
        Page<Konverzija> page = konverzijaRepository.findAll(spec);
        return konverzijaConverter.convert(page.getContent());
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

    public Konverzija save(Konverzija baznaKonverzijaKalkulacija){
        return konverzijaRepository.save(baznaKonverzijaKalkulacija);
    }
}

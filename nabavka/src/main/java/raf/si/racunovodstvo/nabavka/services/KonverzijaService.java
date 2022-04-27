package raf.si.racunovodstvo.nabavka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.services.impl.IKonverzijaService;

import java.util.List;
import java.util.Optional;

@Service
public class KonverzijaService implements IKonverzijaService {

    private final KonverzijaRepository konverzijaRepository;

    @Autowired
    public KonverzijaService(KonverzijaRepository konverzijaRepository) {
        this.konverzijaRepository = konverzijaRepository;
    }

    @Override
    public List<BaznaKonverzijaKalkulacija> findAll(Specification<BaznaKonverzijaKalkulacija> spec) {
        return konverzijaRepository.findAll(spec);
    }

    @Override
    public Optional<BaznaKonverzijaKalkulacija> findById(Long id) {
        return konverzijaRepository.findById(id);
    }

    @Override
    public List<BaznaKonverzijaKalkulacija> findAll() {
        return konverzijaRepository.findAll();
    }

    public void deleteById(Long id) {
        konverzijaRepository.deleteById(id);
    }

    public BaznaKonverzijaKalkulacija save(BaznaKonverzijaKalkulacija baznaKonverzijaKalkulacija){
        return konverzijaRepository.save(baznaKonverzijaKalkulacija);
    }
}

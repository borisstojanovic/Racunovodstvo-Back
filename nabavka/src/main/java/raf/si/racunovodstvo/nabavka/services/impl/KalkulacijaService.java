package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.services.IKalkulacijaService;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KalkulacijaService implements IKalkulacijaService {

    private final KalkulacijaRepository kalkulacijaRepository;

    public KalkulacijaService(KalkulacijaRepository kalkulacijaRepository) {
        this.kalkulacijaRepository = kalkulacijaRepository;
    }

    @Override
    public Page<Kalkulacija> findAll(Specification<Kalkulacija> spec, Pageable pageSort) {
        return kalkulacijaRepository.findAll(spec, pageSort);
    }

    @Override
    public Kalkulacija increaseNabavnaAndProdajnaCena(Long kalkulacijaId, Double nabavnaCena, Double prodajnaCena) {
        Optional<Kalkulacija> optionalKalkulacija = findById(kalkulacijaId);
        if (optionalKalkulacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Kalkulacija kalkulacija = optionalKalkulacija.get();

        kalkulacija.setProdajnaCena(kalkulacija.getProdajnaCena() + prodajnaCena);
        Double ukupnaFakturnaCena = kalkulacija.getFakturnaCena() + nabavnaCena;
        kalkulacija.setFakturnaCena(ukupnaFakturnaCena);
        Double ukupniTroskoviNabavke = kalkulacija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
        kalkulacija.setNabavnaCena(ukupniTroskoviNabavke + ukupnaFakturnaCena);
        return kalkulacijaRepository.save(kalkulacija);
    }

    @Override
    public <S extends Kalkulacija> S save(S var1) {
        return kalkulacijaRepository.save(var1);
    }

    @Override
    public Optional<Kalkulacija> findById(Long var1) {
        return kalkulacijaRepository.findById(var1);
    }

    @Override
    public List<Kalkulacija> findAll() {
        return kalkulacijaRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        kalkulacijaRepository.deleteById(var1);
    }
}

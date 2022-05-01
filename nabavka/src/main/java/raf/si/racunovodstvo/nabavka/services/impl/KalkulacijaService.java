package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.services.IKalkulacijaService;

import java.util.List;
import java.util.Optional;

@Service
public class KalkulacijaService implements IKalkulacijaService {

    private KalkulacijaRepository kalkulacijaRepository;

    public KalkulacijaService(KalkulacijaRepository kalkulacijaRepository) {
        this.kalkulacijaRepository = kalkulacijaRepository;
    }

    @Override
    public Object save(Object var1) {
        return null;
    }

    @Override
    public Optional findById(Object var1) {
        return Optional.empty();
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public void deleteById(Object var1) {

    }

    @Override
    public Page<Kalkulacija> findAll(Specification<Kalkulacija> spec, Pageable pageSort) {
        return kalkulacijaRepository.findAll(spec, pageSort);
    }
}

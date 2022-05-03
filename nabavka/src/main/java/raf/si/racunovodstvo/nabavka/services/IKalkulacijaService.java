package raf.si.racunovodstvo.nabavka.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;

public interface IKalkulacijaService extends IService {

    Page<Kalkulacija> findAll(Specification<Kalkulacija> spec, Pageable pageSort);

}

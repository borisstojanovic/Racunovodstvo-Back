package raf.si.racunovodstvo.nabavka.services.impl;


import org.springframework.data.jpa.domain.Specification;

import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;

import java.util.List;

public interface IKonverzijaService extends IService<BaznaKonverzijaKalkulacija, Long>{

    List<BaznaKonverzijaKalkulacija> findAll(Specification<BaznaKonverzijaKalkulacija> spec);

}

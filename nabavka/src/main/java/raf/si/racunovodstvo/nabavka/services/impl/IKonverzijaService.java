package raf.si.racunovodstvo.nabavka.services.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;

import java.util.List;

public interface IKonverzijaService extends IService<Konverzija, Long>{

    Page<KonverzijaResponse> findAll(Specification<Konverzija> spec);

}

package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;


import java.util.List;

@Repository
public interface KonverzijaRepository extends JpaRepository<BaznaKonverzijaKalkulacija, Long> {

    public List<BaznaKonverzijaKalkulacija> findAll();

    List<BaznaKonverzijaKalkulacija> findAll(Specification<BaznaKonverzijaKalkulacija> spec);
}

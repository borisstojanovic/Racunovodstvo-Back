package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.SifraTransakcije;

@Repository
public interface SifraTransakcijeRepository extends JpaRepository<SifraTransakcije, Long> {
}

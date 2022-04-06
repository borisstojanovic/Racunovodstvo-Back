package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Transakcija;

@Repository
public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {
}

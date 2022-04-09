package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.responses.BilansResponse;

import java.util.Date;
import java.util.List;

@Repository
public interface KontnaGrupaRepository extends JpaRepository<KontnaGrupa, Long> {

    @Query("select new rs.raf.demo.responses.BilansResponse(sum(k.duguje), sum(k.potrazuje), kg.brojKonta, kg.nazivKonta) "
        + "from Konto k join k.kontnaGrupa kg "
        + "where :brojKontaOd <= kg.brojKonta and "
        + "(:brojKontaDo >= kg.brojKonta or kg.brojKonta like :brojKontaDo%) and "
        + "(:datumOd is null or k.knjizenje.datumKnjizenja > :datumOd) and(:datumDo is null or k.knjizenje.datumKnjizenja < :datumDo)"
        + "group by kg.brojKonta, kg.nazivKonta")
    List<BilansResponse> findAllForBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo);
}

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
        + "k.knjizenje.datumKnjizenja between :datumOd and :datumDo "
        + "group by kg.brojKonta, kg.nazivKonta")
    List<BilansResponse> findAllForBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo);

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(sum(k.duguje), sum(k.potrazuje), kg.brojKonta , kg.nazivKonta) "
        + "from Konto k join k.kontnaGrupa kg where "
        + "substring(kg.brojKonta, 1, 1) in :startsWith "
        + "and k.knjizenje.datumKnjizenja between :datumOd and :datumDo "
        + "group by kg.brojKonta, kg.nazivKonta")
    List<BilansResponse> findAllStartingWith(List<String> startsWith, Date datumOd, Date datumDo);
}

package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Konto;
import rs.raf.demo.responses.BilansResponse;

import java.util.Date;
import java.util.List;

@Repository
public interface KontoRepository extends JpaRepository<Konto, Long> {


    @Query(value = "select new rs.raf.demo.responses.BilansResponse(sum(k.duguje) , sum(k.potrazuje) , k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, count(k.kontnaGrupa.brojKonta), k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta) > 3 and (:datumOd is null or k.knjizenje.datumKnjizenja > :datumOd) and(:datumDo is null or k.knjizenje.datumKnjizenja < :datumDo)   group by k.kontnaGrupa")
    List<BilansResponse> findOtherGroups(Date datumOd, Date datumDo);

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=3 group by k.kontnaGrupa")
    List<BilansResponse> findThirdGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=2 group by k.kontnaGrupa")
    List<BilansResponse> findSecondGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=1 group by k.kontnaGrupa")
    List<BilansResponse> findFirstGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja,k.duguje,k.potrazuje)" +
            " from Konto k where (k.kontnaGrupa.brojKonta like '0%' or k.kontnaGrupa.brojKonta like '1%' or k.kontnaGrupa.brojKonta like '2%'" +
            "or k.kontnaGrupa.brojKonta like '3%' or k.kontnaGrupa.brojKonta like '4%') and length(k.kontnaGrupa.brojKonta)>3 " +
            "and(:datumOd is null or k.knjizenje.datumKnjizenja > :datumOd) and(:datumDo is null or k.knjizenje.datumKnjizenja < :datumDo)")
    List<BilansResponse> findAllBilansStanja(Date datumOd, Date datumDo);

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja,k.duguje,k.potrazuje)" +
            " from Konto k where (k.kontnaGrupa.brojKonta like '5%' or k.kontnaGrupa.brojKonta like '6%') and length(k.kontnaGrupa.brojKonta)>3 " +
            "and(:datumOd is null or k.knjizenje.datumKnjizenja > :datumOd) and(:datumDo is null or k.knjizenje.datumKnjizenja < :datumDo)")
    List<BilansResponse> findAllBilansUspeha(Date datumOd, Date datumDo);

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=3 and (k.kontnaGrupa.brojKonta like '0%' or k.kontnaGrupa.brojKonta like '1%' or k.kontnaGrupa.brojKonta like '2%' " +
            "or k.kontnaGrupa.brojKonta like '3%' or k.kontnaGrupa.brojKonta like '4%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansStanjaThirdGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=2 and (k.kontnaGrupa.brojKonta like '0%' or k.kontnaGrupa.brojKonta like '1%' or k.kontnaGrupa.brojKonta like '2%' " +
            "or k.kontnaGrupa.brojKonta like '3%' or k.kontnaGrupa.brojKonta like '4%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansStanjaSecondGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=1 and (k.kontnaGrupa.brojKonta like '0%' or k.kontnaGrupa.brojKonta like '1%' or k.kontnaGrupa.brojKonta like '2%' " +
            "or k.kontnaGrupa.brojKonta like '3%' or k.kontnaGrupa.brojKonta like '4%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansStanjaFirstGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=3 and (k.kontnaGrupa.brojKonta like '5%' or k.kontnaGrupa.brojKonta like '6%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansUspehaThirdGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=2 and (k.kontnaGrupa.brojKonta like '5%' or k.kontnaGrupa.brojKonta like '6%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansUspehaSecondGroup();

    @Query(value = "select new rs.raf.demo.responses.BilansResponse(k.kontnaGrupa.brojKonta , k.kontnaGrupa.nazivKonta, k.knjizenje.datumKnjizenja)" +
            " from Konto k where length(k.kontnaGrupa.brojKonta)=1 and (k.kontnaGrupa.brojKonta like '5%' or k.kontnaGrupa.brojKonta like '6%') group by k.kontnaGrupa")
    List<BilansResponse> findBilansUspehaFirstGroup();

}

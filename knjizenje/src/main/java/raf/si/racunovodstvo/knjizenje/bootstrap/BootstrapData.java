package raf.si.racunovodstvo.knjizenje.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.feign.KursnaListaFeignClient;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.*;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.responses.KursResponse;
import raf.si.racunovodstvo.knjizenje.responses.KursnaListaResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final FakturaRepository fakturaRepository;
    private final KontnaGrupaRepository kontnaGrupaRepository;
    private final KontoRepository kontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private final ProfitniCentarRepository profitniCentarRepository;
    private final TroskovniCentarRepository troskovniCentarRepository;
    private final BazniKontoRepository bazniKontoRepository;
    private final PovracajRepository povracajRepository;
    private final TransakcijaRepository transakcijaRepository;
    private final SifraTransakcijeRepository sifraTransakcijeRepository;
    private final KursnaListaFeignClient kursnaListaFeignClient;


    @Autowired
    public BootstrapData(FakturaRepository fakturaRepository,
                         KontoRepository kontoRepository,
                         KontnaGrupaRepository kontnaGrupaRepository,
                         KnjizenjeRepository knjizenjeRepository,
                         TransakcijaRepository transakcijaRepository, SifraTransakcijeRepository sifraTransakcijeRepository,
                         ProfitniCentarRepository profitniCentarRepository,
                         TroskovniCentarRepository troskovniCentarRepository,
                         BazniKontoRepository bazniKontoRepository,
                         PovracajRepository povracajRepository,
                         KursnaListaFeignClient kursnaListaFeignClient
    ) {
        this.fakturaRepository = fakturaRepository;
        this.kontoRepository = kontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.kontnaGrupaRepository = kontnaGrupaRepository;
        this.transakcijaRepository = transakcijaRepository;
        this.sifraTransakcijeRepository = sifraTransakcijeRepository;
        this.profitniCentarRepository = profitniCentarRepository;
        this.troskovniCentarRepository = troskovniCentarRepository;
        this.bazniKontoRepository = bazniKontoRepository;
        this.povracajRepository = povracajRepository;
        this.kursnaListaFeignClient = kursnaListaFeignClient;
    }

    private Faktura getDefaultFaktura() {

        Faktura f1 = new Faktura();
        f1.setBrojFakture("1");
        f1.setIznos(1000.00);
        f1.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        f1.setDatumIzdavanja(new Date());
        f1.setDatumPlacanja(new Date());
        f1.setKurs(117.8);
        f1.setNaplata(1000.00);
        f1.setPorez(10.00);
        f1.setPorezProcenat(1.00);
        f1.setProdajnaVrednost(1000.00);
        f1.setValuta("EUR");
        f1.setBrojDokumenta("1234");
        f1.setRokZaPlacanje(new Date());
        f1.setTipDokumenta(TipDokumenta.FAKTURA);

        return f1;
    }

    private Preduzece getDefaultPreduzece() {
        Preduzece p1 = new Preduzece();
        p1.setNaziv("Test Preduzece");
        p1.setPib("111222333");
        p1.setAdresa("test adresa");
        p1.setGrad("Beograd");

        return p1;
    }

    private Konto createKonto(KontnaGrupa kg, Knjizenje knj, Double duguje, Double potrazuje) {
        Konto konto = new Konto();
        konto.setDuguje(duguje);
        konto.setPotrazuje(potrazuje);
        konto.setKnjizenje(knj);
        konto.setKontnaGrupa(kg);
        return konto;
    }

    private SifraTransakcije getRandomSifraTransakcije() {
        SifraTransakcije st = new SifraTransakcije();
        st.setSifra(1010L);
        st.setNazivTransakcije("1010LLLLL");
        return st;
    }

    private Transakcija getRandomTransakcija() {
        Transakcija tr = new Transakcija();
        tr.setTipDokumenta(TipDokumenta.FAKTURA);
        tr.setIznos(222.33);
        tr.setTipTransakcije(TipTransakcije.UPLATA);
        tr.setDatumTransakcije(new Date());
        return tr;
    }

    private Povracaj createPovracaj(String brojPovracaja, Date datum, Double prodajnaVrednost) {
        Povracaj povracaj = new Povracaj();
        povracaj.setBrojPovracaja(brojPovracaja);
        povracaj.setDatumPovracaja(datum);
        povracaj.setProdajnaVrednost(prodajnaVrednost);

        return povracaj;
    }

    private Date getDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private KursResponse getDefaultForValuta(String valuta) {
        KursResponse toReturn;
        switch (valuta) {
            case "eur":
                toReturn = new KursResponse(117.05, 117.40, 117.80);
                break;
            case "usd":
                toReturn = new KursResponse(111.80, 112.20, 112.60);
                break;
            case "gbp":
                toReturn = new KursResponse(135.81, 136.20, 136.60);
                break;
            case "rsd":
            default:
                toReturn = new KursResponse(1.00, 1.00, 1.00);
        }
        return toReturn;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        ObjectMapper mapper = new ObjectMapper();

        KursnaListaResponse response21522 = kursnaListaFeignClient.getKursnaListaForDate("22.05.2021");
        KursnaListaResponse response21518 = kursnaListaFeignClient.getKursnaListaForDate("18.05.2021");
        KursnaListaResponse response21516 = kursnaListaFeignClient.getKursnaListaForDate("16.05.2021");
        KursnaListaResponse response21512 = kursnaListaFeignClient.getKursnaListaForDate("12.05.2021");
        KursResponse kursResponseGbp21522 =
            mapper.convertValue(response21522.getResult().getOrDefault("gbp", getDefaultForValuta("gbp")), new TypeReference<>() {
            });
        KursResponse kursResponseUsd21518 =
            mapper.convertValue(response21518.getResult().getOrDefault("usd", getDefaultForValuta("usd")), new TypeReference<>() {
            });
        KursResponse kursResponseRsd21516 =
            mapper.convertValue(response21516.getResult().getOrDefault("rsd", getDefaultForValuta("rsd")), new TypeReference<>() {
            });
        KursResponse kursResponseGbp21512 =
            mapper.convertValue(response21512.getResult().getOrDefault("gbp", getDefaultForValuta("gbp")), new TypeReference<>() {
            });

        Faktura fu1 = new Faktura();
        fu1.setBrojFakture("F23/11");
        fu1.setBrojDokumenta(fu1.getBrojFakture());
        fu1.setDatumIzdavanja(getDate(2021, 5, 11));
        fu1.setRokZaPlacanje(getDate(2021, 5, 17));
        fu1.setDatumPlacanja(getDate(2021, 5, 16));
        fu1.setProdajnaVrednost(11300.00);
        fu1.setRabatProcenat(5.00);
        fu1.setPorezProcenat(20.00);
        fu1.setValuta("RSD");
        fu1.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu1.setTipDokumenta(TipDokumenta.FAKTURA);
        fu1.setPreduzeceId(1L);
        fu1.setKurs(kursResponseRsd21516.getSre());
        fu1.setNaplata(0.00);

        Faktura fu2 = new Faktura();
        fu2.setBrojFakture("F23/12");
        fu2.setBrojDokumenta(fu2.getBrojFakture());
        fu2.setDatumIzdavanja(getDate(2021, 5, 11));
        fu2.setRokZaPlacanje(getDate(2021, 5, 17));
        fu2.setDatumPlacanja(getDate(2021, 5, 16));
        fu2.setProdajnaVrednost(12400.00);
        fu2.setRabatProcenat(0.00);
        fu2.setPorezProcenat(20.00);
        fu2.setValuta("RSD");
        fu2.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu2.setTipDokumenta(TipDokumenta.FAKTURA);
        fu2.setPreduzeceId(1L);
        fu2.setKurs(kursResponseRsd21516.getSre());
        fu2.setNaplata(0.00);

        Faktura fu3 = new Faktura();
        fu3.setBrojFakture("F23/13");
        fu3.setBrojDokumenta(fu3.getBrojFakture());
        fu3.setDatumIzdavanja(getDate(2021, 5, 12));
        fu3.setRokZaPlacanje(getDate(2021, 5, 17));
        fu3.setDatumPlacanja(getDate(2021, 5, 16));
        fu3.setProdajnaVrednost(11000.00);
        fu3.setRabatProcenat(0.00);
        fu3.setPorezProcenat(20.00);
        fu3.setValuta("RSD");
        fu3.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu3.setTipDokumenta(TipDokumenta.FAKTURA);
        fu3.setPreduzeceId(1L);
        fu3.setKurs(kursResponseRsd21516.getSre());
        fu3.setNaplata(0.00);

        Faktura fu4 = new Faktura();
        fu4.setBrojFakture("F23/14");
        fu4.setBrojDokumenta(fu4.getBrojFakture());
        fu4.setDatumIzdavanja(getDate(2021, 5, 12));
        fu4.setRokZaPlacanje(getDate(2021, 5, 17));
        fu4.setDatumPlacanja(getDate(2021, 5, 16));
        fu4.setProdajnaVrednost(22000.00);
        fu4.setRabatProcenat(0.00);
        fu4.setPorezProcenat(20.00);
        fu4.setValuta("RSD");
        fu4.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu4.setTipDokumenta(TipDokumenta.FAKTURA);
        fu4.setPreduzeceId(1L);
        fu4.setKurs(kursResponseRsd21516.getSre());
        fu4.setNaplata(0.00);

        Faktura fu5 = new Faktura();
        fu5.setBrojFakture("F23/15");
        fu5.setBrojDokumenta(fu5.getBrojFakture());
        fu5.setDatumIzdavanja(getDate(2021, 5, 12));
        fu5.setRokZaPlacanje(getDate(2021, 5, 18));
        fu5.setDatumPlacanja(getDate(2021, 5, 12));
        fu5.setProdajnaVrednost(750000.00);
        fu5.setRabatProcenat(0.00);
        fu5.setPorezProcenat(20.00);
        fu5.setValuta("GBP");
        fu5.setKomentar("inostranstvo");
        fu5.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu5.setTipDokumenta(TipDokumenta.FAKTURA);
        fu5.setPreduzeceId(2L);
        fu5.setKurs(kursResponseGbp21512.getSre());
        fu5.setNaplata(0.00);

        Faktura fu6 = new Faktura();
        fu6.setBrojFakture("F23/16");
        fu6.setBrojDokumenta(fu6.getBrojFakture());
        fu6.setDatumIzdavanja(getDate(2021, 5, 12));
        fu6.setRokZaPlacanje(getDate(2021, 5, 12));
        fu6.setDatumPlacanja(getDate(2021, 5, 12));
        fu6.setProdajnaVrednost(55000.00);
        fu6.setRabatProcenat(0.00);
        fu6.setPorezProcenat(20.00);
        fu6.setValuta("GBP");
        fu6.setKomentar("inostranstvo");
        fu6.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu6.setTipDokumenta(TipDokumenta.FAKTURA);
        fu6.setPreduzeceId(2L);
        fu6.setKurs(kursResponseGbp21512.getSre());
        fu6.setNaplata(0.00);

        Faktura fu7 = new Faktura();
        fu7.setBrojFakture("F23/17");
        fu7.setBrojDokumenta(fu7.getBrojFakture());
        fu7.setDatumIzdavanja(getDate(2021, 5, 12));
        fu7.setRokZaPlacanje(getDate(2021, 5, 12));
        fu7.setDatumPlacanja(getDate(2021, 5, 12));
        fu7.setProdajnaVrednost(55000.00);
        fu7.setRabatProcenat(0.00);
        fu7.setPorezProcenat(20.00);
        fu7.setValuta("GBP");
        fu7.setKomentar("inostranstvo");
        fu7.setPreduzeceId(1L);
        fu7.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu7.setTipDokumenta(TipDokumenta.FAKTURA);
        fu7.setPreduzeceId(2L);
        fu7.setKurs(kursResponseGbp21512.getSre());
        fu7.setNaplata(0.00);

        Faktura fu8 = new Faktura();
        fu8.setBrojFakture("F24/11");
        fu8.setBrojDokumenta(fu8.getBrojFakture());
        fu8.setDatumIzdavanja(getDate(2021, 5, 12));
        fu8.setRokZaPlacanje(getDate(2021, 5, 12));
        fu8.setDatumPlacanja(getDate(2021, 5, 12));
        fu8.setProdajnaVrednost(750000.00);
        fu8.setRabatProcenat(10.00);
        fu8.setPorezProcenat(20.00);
        fu8.setValuta("GBP");
        fu8.setKomentar("inostranstvo");
        fu8.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu8.setTipDokumenta(TipDokumenta.FAKTURA);
        fu8.setPreduzeceId(2L);
        fu8.setKurs(kursResponseGbp21512.getSre());
        fu8.setNaplata(0.00);

        Faktura fu9 = new Faktura();
        fu9.setBrojFakture("F24/12");
        fu9.setBrojDokumenta(fu9.getBrojFakture());
        fu9.setDatumIzdavanja(getDate(2021, 5, 12));
        fu9.setRokZaPlacanje(getDate(2021, 5, 12));
        fu9.setDatumPlacanja(getDate(2021, 5, 12));
        fu9.setProdajnaVrednost(100000.00);
        fu9.setRabatProcenat(10.00);
        fu9.setPorezProcenat(20.00);
        fu9.setValuta("GBP");
        fu9.setKomentar("inostranstvo");
        fu9.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu9.setTipDokumenta(TipDokumenta.FAKTURA);
        fu9.setPreduzeceId(2L);
        fu9.setKurs(kursResponseGbp21512.getSre());
        fu9.setNaplata(0.00);

        Faktura fi1 = new Faktura();
        fi1.setBrojFakture("F24/13");
        fi1.setBrojDokumenta(fi1.getBrojFakture());
        fi1.setDatumIzdavanja(getDate(2021, 5, 13));
        fi1.setRokZaPlacanje(getDate(2021, 7, 31));
        fi1.setDatumPlacanja(getDate(2021, 7, 31));
        fi1.setProdajnaVrednost(23400.00);
        fi1.setRabatProcenat(5.00);
        fi1.setPorezProcenat(20.00);
        fi1.setValuta("RSD");
        fi1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi1.setTipDokumenta(TipDokumenta.FAKTURA);
        fi1.setPreduzeceId(3L);
        fi1.setKurs(1.00);
        fi1.setNaplata(0.00);

        Faktura fi2 = new Faktura();
        fi2.setBrojFakture("F24/14");
        fi2.setBrojDokumenta(fi2.getBrojFakture());
        fi2.setDatumIzdavanja(getDate(2021, 5, 13));
        fi2.setRokZaPlacanje(getDate(2021, 7, 31));
        fi2.setDatumPlacanja(getDate(2021, 5, 22));
        fi2.setProdajnaVrednost(23400.00);
        fi2.setRabatProcenat(10.00);
        fi2.setPorezProcenat(20.00);
        fi2.setValuta("RSD");
        fi2.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi2.setTipDokumenta(TipDokumenta.FAKTURA);
        fi2.setPreduzeceId(3L);
        fi2.setKurs(1.00);
        fi2.setNaplata(0.00);

        Faktura fi3 = new Faktura();
        fi3.setBrojFakture("F24/15");
        fi3.setBrojDokumenta(fi3.getBrojFakture());
        fi3.setDatumIzdavanja(getDate(2021, 5, 15));
        fi3.setRokZaPlacanje(getDate(2021, 5, 22));
        fi3.setDatumPlacanja(getDate(2021, 5, 22));
        fi3.setProdajnaVrednost(23400.00);
        fi3.setRabatProcenat(5.00);
        fi3.setPorezProcenat(20.00);
        fi3.setValuta("GBP");
        fi3.setKomentar("inostranstvo");
        fi3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi3.setTipDokumenta(TipDokumenta.FAKTURA);
        fi3.setPreduzeceId(2L);
        fi3.setKurs(kursResponseGbp21522.getSre());
        fi3.setNaplata(0.00);

        Faktura fi4 = new Faktura();
        fi4.setBrojFakture("F24/16");
        fi4.setBrojDokumenta(fi4.getBrojFakture());
        fi4.setDatumIzdavanja(getDate(2021, 5, 16));
        fi4.setRokZaPlacanje(getDate(2021, 5, 22));
        fi4.setDatumPlacanja(getDate(2021, 5, 22));
        fi4.setProdajnaVrednost(11000.00);
        fi4.setRabatProcenat(10.00);
        fi4.setPorezProcenat(20.00);
        fi4.setValuta("RSD");
        fi4.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi4.setTipDokumenta(TipDokumenta.FAKTURA);
        fi4.setPreduzeceId(1L);
        fi4.setKurs(1.00);
        fi4.setNaplata(0.00);

        Faktura fi5 = new Faktura();
        fi5.setBrojFakture("F24/17");
        fi5.setBrojDokumenta(fi5.getBrojFakture());
        fi5.setDatumIzdavanja(getDate(2021, 5, 17));
        fi5.setRokZaPlacanje(getDate(2021, 7, 31));
        fi5.setDatumPlacanja(getDate(2021, 5, 17));
        fi5.setProdajnaVrednost(23400.00);
        fi5.setRabatProcenat(5.00);
        fi5.setPorezProcenat(20.00);
        fi5.setValuta("RSD");
        fi5.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi5.setTipDokumenta(TipDokumenta.FAKTURA);
        fi5.setPreduzeceId(3L);
        fi5.setKurs(1.00);
        fi5.setNaplata(0.00);

        Faktura fi6 = new Faktura();
        fi6.setBrojFakture("F24/18");
        fi6.setBrojDokumenta(fi6.getBrojFakture());
        fi6.setDatumIzdavanja(getDate(2021, 5, 18));
        fi6.setRokZaPlacanje(getDate(2021, 10, 31));
        fi6.setDatumPlacanja(getDate(2021, 5, 18));
        fi6.setProdajnaVrednost(85230.00);
        fi6.setRabatProcenat(0.00);
        fi6.setPorezProcenat(20.00);
        fi6.setValuta("USD");
        fi6.setKomentar("inostranstvo");
        fi6.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fi6.setTipDokumenta(TipDokumenta.FAKTURA);
        fi6.setPreduzeceId(4L);
        fi6.setKurs(kursResponseUsd21518.getSre());
        fi6.setNaplata(0.00);

        this.fakturaRepository.save(fu1);
        this.fakturaRepository.save(fu2);
        this.fakturaRepository.save(fu3);
        this.fakturaRepository.save(fu4);
        this.fakturaRepository.save(fu5);
        this.fakturaRepository.save(fu6);
        this.fakturaRepository.save(fu7);
        this.fakturaRepository.save(fu8);
        this.fakturaRepository.save(fu9);
        this.fakturaRepository.save(fi1);
        this.fakturaRepository.save(fi2);
        this.fakturaRepository.save(fi3);
        this.fakturaRepository.save(fi4);
        this.fakturaRepository.save(fi5);
        this.fakturaRepository.save(fi6);

        SifraTransakcije st1 = new SifraTransakcije();
        st1.setSifra(124L);
        st1.setNazivTransakcije("Investicije-ostalo");
        SifraTransakcije st2 = new SifraTransakcije();
        st2.setSifra(288L);
        st2.setNazivTransakcije("Donacije");
        SifraTransakcije st3 = new SifraTransakcije();
        st3.setSifra(363L);
        st3.setNazivTransakcije("Ostali transferi");
        SifraTransakcije st4 = new SifraTransakcije();
        st4.setSifra(163L);
        st4.setNazivTransakcije("Ostali transferi");

        this.sifraTransakcijeRepository.save(st1);
        this.sifraTransakcijeRepository.save(st2);
        this.sifraTransakcijeRepository.save(st3);
        this.sifraTransakcijeRepository.save(st4);

        Transakcija t1 = new Transakcija();
        t1.setBrojTransakcije("T11/23");
        t1.setPreduzeceId(3L);
        t1.setDatumTransakcije(getDate(2021, 5, 22));
        t1.setTipTransakcije(TipTransakcije.ISPLATA);
        t1.setIznos(11700.00);
        t1.setSifraTransakcije(st1);
        t1.setBrojDokumenta(t1.getBrojTransakcije());
        t1.setTipDokumenta(TipDokumenta.TRANSAKCIJA);

        Transakcija t2 = new Transakcija();
        t2.setBrojTransakcije("T11/24");
        t2.setPreduzeceId(2L);
        t2.setDatumTransakcije(getDate(2021, 5, 23));
        t2.setTipTransakcije(TipTransakcije.ISPLATA);
        t2.setIznos(11700.00);
        t2.setSifraTransakcije(st1);
        t2.setKomentar("inostranstvo");
        t2.setBrojDokumenta(t2.getBrojTransakcije());
        t2.setTipDokumenta(TipDokumenta.TRANSAKCIJA);

        Transakcija t3 = new Transakcija();
        t3.setBrojTransakcije("T11/25");
        t3.setDatumTransakcije(getDate(2021, 5, 27));
        t3.setTipTransakcije(TipTransakcije.UPLATA);
        t3.setIznos(40000.00);
        t3.setSadrzaj("Donacija Pavle Marković");
        t3.setSifraTransakcije(st2);
        t3.setPreduzeceId(3L);
        t3.setBrojDokumenta(t3.getBrojTransakcije());
        t3.setTipDokumenta(TipDokumenta.TRANSAKCIJA);

        Transakcija t4 = new Transakcija();
        t4.setBrojTransakcije("T23/33");
        t4.setPreduzeceId(3L);
        t4.setDatumTransakcije(getDate(2021, 5, 27));
        t4.setTipTransakcije(TipTransakcije.ISPLATA);
        t4.setIznos(12000.00);
        t4.setSifraTransakcije(st3);
        t4.setBrojDokumenta(t4.getBrojTransakcije());
        t4.setTipDokumenta(TipDokumenta.TRANSAKCIJA);

        Transakcija t5 = new Transakcija();
        t5.setBrojTransakcije("T11OST");
        t5.setPreduzeceId(3L);
        t5.setDatumTransakcije(getDate(2021, 5, 29));
        t5.setTipTransakcije(TipTransakcije.ISPLATA);
        t5.setIznos(40000.00);
        t5.setSifraTransakcije(st4);
        t5.setBrojDokumenta(t5.getBrojTransakcije());
        t5.setTipDokumenta(TipDokumenta.TRANSAKCIJA);

        this.transakcijaRepository.save(t1);
        this.transakcijaRepository.save(t2);
        this.transakcijaRepository.save(t3);
        this.transakcijaRepository.save(t4);
        this.transakcijaRepository.save(t5);

        Faktura mpf1 = new Faktura();
        mpf1.setBrojFakture("MP12/21");
        mpf1.setBrojDokumenta(mpf1.getBrojFakture());
        mpf1.setDatumIzdavanja(getDate(2021, 4, 5));
        mpf1.setRokZaPlacanje(getDate(2021, 4, 5));
        mpf1.setDatumPlacanja(getDate(2021, 4, 5));
        mpf1.setProdajnaVrednost(5300.00);
        mpf1.setRabatProcenat(0.00);
        mpf1.setPorezProcenat(20.00);
        mpf1.setPreduzeceId(1L);
        mpf1.setValuta("RSD");
        mpf1.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf1.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf1.setKurs(1.00);
        mpf1.setNaplata(0.00);

        Faktura mpf2 = new Faktura();
        mpf2.setPreduzeceId(2L);
        mpf2.setBrojFakture("MP12/22");
        mpf2.setBrojDokumenta(mpf2.getBrojFakture());
        mpf2.setDatumIzdavanja(getDate(2021, 4, 5));
        mpf2.setRokZaPlacanje(getDate(2021, 4, 5));
        mpf2.setDatumPlacanja(getDate(2021, 4, 5));
        mpf2.setProdajnaVrednost(11700.00);
        mpf2.setRabatProcenat(0.00);
        mpf2.setPorezProcenat(20.00);
        mpf2.setValuta("RSD");
        mpf2.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf2.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf2.setKurs(1.00);
        mpf2.setNaplata(0.00);

        Faktura mpf3 = new Faktura();
        mpf3.setPreduzeceId(3L);
        mpf3.setBrojFakture("MP12/23");
        mpf3.setBrojDokumenta(mpf3.getBrojFakture());
        mpf3.setDatumIzdavanja(getDate(2021, 5, 7));
        mpf3.setRokZaPlacanje(getDate(2021, 5, 7));
        mpf3.setDatumPlacanja(getDate(2021, 5, 7));
        mpf3.setProdajnaVrednost(24500.00);
        mpf3.setRabatProcenat(0.00);
        mpf3.setPorezProcenat(20.00);
        mpf3.setValuta("RSD");
        mpf3.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf3.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf3.setKurs(1.00);
        mpf3.setNaplata(0.00);

        Faktura mpf4 = new Faktura();
        mpf4.setPreduzeceId(4L);
        mpf4.setBrojFakture("MP17/21");
        mpf4.setBrojDokumenta(mpf4.getBrojFakture());
        mpf4.setDatumIzdavanja(getDate(2021, 5, 7));
        mpf4.setRokZaPlacanje(getDate(2021, 5, 7));
        mpf4.setDatumPlacanja(getDate(2021, 5, 7));
        mpf4.setProdajnaVrednost(9200.00);
        mpf4.setRabatProcenat(0.00);
        mpf4.setPorezProcenat(20.00);
        mpf4.setValuta("RSD");
        mpf4.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf4.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf4.setKurs(1.00);
        mpf4.setNaplata(0.00);

        Faktura mpf5 = new Faktura();
        mpf5.setPreduzeceId(5L);
        mpf5.setBrojFakture("MP18/22");
        mpf5.setBrojDokumenta(mpf5.getBrojFakture());
        mpf5.setDatumIzdavanja(getDate(2021, 6, 12));
        mpf5.setRokZaPlacanje(getDate(2021, 6, 12));
        mpf5.setDatumPlacanja(getDate(2021, 6, 12));
        mpf5.setProdajnaVrednost(8350.00);
        mpf5.setRabatProcenat(0.00);
        mpf5.setPorezProcenat(20.00);
        mpf5.setValuta("RSD");
        mpf5.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf5.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf5.setKurs(1.00);
        mpf5.setNaplata(0.00);

        this.fakturaRepository.save(mpf1);
        this.fakturaRepository.save(mpf2);
        this.fakturaRepository.save(mpf3);
        this.fakturaRepository.save(mpf4);
        this.fakturaRepository.save(mpf5);

        Povracaj pov1 = new Povracaj();
        pov1.setBrojPovracaja("P01");
        pov1.setDatumPovracaja(getDate(2021, 4, 5));
        pov1.setProdajnaVrednost(5300.00);

        Povracaj pov2 = new Povracaj();
        pov2.setBrojPovracaja("P02");
        pov2.setDatumPovracaja(getDate(2021, 5, 7));
        pov2.setProdajnaVrednost(11700.00);

        Povracaj pov3 = new Povracaj();
        pov3.setBrojPovracaja("P03");
        pov3.setDatumPovracaja(getDate(2021, 5, 7));
        pov3.setProdajnaVrednost(24500.00);

        Povracaj pov4 = new Povracaj();
        pov4.setBrojPovracaja("P04");
        pov4.setDatumPovracaja(getDate(2021, 6, 12));
        pov4.setProdajnaVrednost(2000.00);

        this.povracajRepository.save(pov1);
        this.povracajRepository.save(pov2);
        this.povracajRepository.save(pov3);
        this.povracajRepository.save(pov4);

        TroskovniCentar tc1 = new TroskovniCentar();
        tc1.setSifra("MAG-BG");
        tc1.setNaziv("MAGACINI");
        tc1.setLokacijaId(11L);
        tc1.setOdgovornoLiceId(1L);
        tc1.setUkupniTrosak(0.00);

        TroskovniCentar tc2 = new TroskovniCentar();
        tc2.setSifra("MAG-NBG");
        tc2.setNaziv("MAGACIN - NOVI BEOGRAD");
        tc2.setParentTroskovniCentar(tc1);
        tc2.setLokacijaId(11L);
        tc2.setOdgovornoLiceId(1L);
        tc2.setUkupniTrosak(0.00);

        TroskovniCentar tc3 = new TroskovniCentar();
        tc3.setSifra("MAG-NBG-DC");
        tc3.setNaziv("MAGACIN - NOVI BEOGRAD, DELTA CITY");
        tc3.setParentTroskovniCentar(tc2);
        tc3.setLokacijaId(11L);
        tc3.setOdgovornoLiceId(1L);
        tc3.setUkupniTrosak(0.00);

        TroskovniCentar tc4 = new TroskovniCentar();
        tc4.setSifra("MAG-SV");
        tc4.setNaziv("MAGACIN - SAVSKI VENAC");
        tc4.setParentTroskovniCentar(tc1);
        tc4.setLokacijaId(11L);
        tc4.setOdgovornoLiceId(1L);
        tc4.setUkupniTrosak(0.00);

        TroskovniCentar tc5 = new TroskovniCentar();
        tc5.setSifra("MAG-Z");
        tc5.setNaziv("MAGACIN - ZEMUN");
        tc5.setParentTroskovniCentar(tc1);
        tc5.setLokacijaId(11L);
        tc5.setOdgovornoLiceId(1L);
        tc5.setUkupniTrosak(0.00);

        this.troskovniCentarRepository.save(tc1);
        this.troskovniCentarRepository.save(tc2);
        this.troskovniCentarRepository.save(tc3);
        this.troskovniCentarRepository.save(tc4);
        this.troskovniCentarRepository.save(tc5);

        ProfitniCentar pc1 = new ProfitniCentar();
        pc1.setSifra("MP-BG");
        pc1.setNaziv("MALOPRODAJE");
        pc1.setLokacijaId(11L);
        pc1.setOdgovornoLiceId(1L);
        pc1.setUkupniTrosak(0.00);

        ProfitniCentar pc2 = new ProfitniCentar();
        pc2.setSifra("MP-NBG");
        pc2.setNaziv("MALOPRODAJA - NOVI BEOGRAD");
        pc2.setParentProfitniCentar(pc1);
        pc2.setLokacijaId(11L);
        pc2.setOdgovornoLiceId(1L);
        pc2.setUkupniTrosak(0.00);

        ProfitniCentar pc3 = new ProfitniCentar();
        pc3.setSifra("MP-NBG");
        pc3.setNaziv("MALOPRODAJA - NBG - DELTA CITY");
        pc3.setParentProfitniCentar(pc2);
        pc3.setLokacijaId(11L);
        pc3.setOdgovornoLiceId(1L);
        pc3.setUkupniTrosak(0.00);

        this.profitniCentarRepository.save(pc1);
        this.profitniCentarRepository.save(pc2);
        this.profitniCentarRepository.save(pc3);

        KontnaGrupa kg1 = new KontnaGrupa();
        kg1.setBrojKonta("0");
        kg1.setNazivKonta("Naziv kontne grupe 0");

        this.kontnaGrupaRepository.save(kg1);

        KontnaGrupa kg3t = new KontnaGrupa();
        kg3t.setBrojKonta("3");
        kg3t.setNazivKonta("Naziv kontne grupe 3");
        KontnaGrupa kg30 = new KontnaGrupa();
        kg30.setBrojKonta("30");
        kg30.setNazivKonta("Naziv kontne grupe 30");
        KontnaGrupa kg301 = new KontnaGrupa();
        kg301.setBrojKonta("301");
        kg301.setNazivKonta("Naziv kontne grupe 301");
        KontnaGrupa kg302 = new KontnaGrupa();
        kg302.setBrojKonta("302");
        kg302.setNazivKonta("Naziv kontne grupe 302");
        KontnaGrupa kg306 = new KontnaGrupa();
        kg306.setBrojKonta("306");
        kg306.setNazivKonta("Naziv kontne grupe 306");
        KontnaGrupa kg309 = new KontnaGrupa();
        kg309.setBrojKonta("309");
        kg309.setNazivKonta("Naziv kontne grupe 309");
        KontnaGrupa kg31 = new KontnaGrupa();
        kg31.setBrojKonta("31");
        kg31.setNazivKonta("Naziv kontne grupe 31");
        KontnaGrupa kg311 = new KontnaGrupa();
        kg311.setBrojKonta("311");
        kg311.setNazivKonta("Naziv kontne grupe 311");
        KontnaGrupa kg32 = new KontnaGrupa();
        kg32.setBrojKonta("32");
        kg32.setNazivKonta("Naziv kontne grupe 32");
        KontnaGrupa kg321 = new KontnaGrupa();
        kg321.setBrojKonta("321");
        kg321.setNazivKonta("Naziv kontne grupe 321");
        KontnaGrupa kg33 = new KontnaGrupa();
        kg33.setBrojKonta("33");
        kg33.setNazivKonta("Naziv kontne grupe 33");
        KontnaGrupa kg331 = new KontnaGrupa();
        kg331.setBrojKonta("331");
        kg331.setNazivKonta("Naziv kontne grupe 331");
        KontnaGrupa kg34 = new KontnaGrupa();
        kg34.setBrojKonta("34");
        kg34.setNazivKonta("Naziv kontne grupe 34");
        KontnaGrupa kg341 = new KontnaGrupa();
        kg341.setBrojKonta("341");
        kg341.setNazivKonta("Naziv kontne grupe 341");
        KontnaGrupa kg35 = new KontnaGrupa();
        kg35.setBrojKonta("35");
        kg35.setNazivKonta("Naziv kontne grupe 35");
        KontnaGrupa kg351 = new KontnaGrupa();
        kg351.setBrojKonta("351");
        kg351.setNazivKonta("Naziv kontne grupe 351");
        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg3t,
                                                         kg30,
                                                         kg301,
                                                         kg302,
                                                         kg306,
                                                         kg309,
                                                         kg31,
                                                         kg311,
                                                         kg32,
                                                         kg321,
                                                         kg33,
                                                         kg331,
                                                         kg34,
                                                         kg341,
                                                         kg35,
                                                         kg351));

        KontnaGrupa kg5t = new KontnaGrupa();
        kg5t.setBrojKonta("5");
        kg5t.setNazivKonta("Naziv kontne grupe 5");
        KontnaGrupa kg51 = new KontnaGrupa();
        kg51.setBrojKonta("51");
        kg51.setNazivKonta("Naziv kontne grupe 51");
        KontnaGrupa kg511 = new KontnaGrupa();
        kg511.setBrojKonta("511");
        kg511.setNazivKonta("Naziv kontne grupe 511");
        KontnaGrupa kg52 = new KontnaGrupa();
        kg52.setBrojKonta("52");
        kg52.setNazivKonta("Naziv kontne grupe 52");
        KontnaGrupa kg50 = new KontnaGrupa();
        kg50.setBrojKonta("50");
        kg50.setNazivKonta("Naziv kontne grupe 50");
        KontnaGrupa kg501 = new KontnaGrupa();
        kg501.setBrojKonta("501");
        kg501.setNazivKonta("Naziv kontne grupe 501");
        KontnaGrupa kg521 = new KontnaGrupa();
        kg521.setBrojKonta("521");
        kg521.setNazivKonta("Naziv kontne grupe 521");
        KontnaGrupa kg62 = new KontnaGrupa();
        kg62.setBrojKonta("62");
        kg62.setNazivKonta("Naziv kontne grupe 62");
        KontnaGrupa kg621 = new KontnaGrupa();
        kg621.setBrojKonta("621");
        kg621.setNazivKonta("Naziv kontne grupe 621");
        KontnaGrupa kg60 = new KontnaGrupa();
        kg60.setBrojKonta("60");
        kg60.setNazivKonta("Naziv kontne grupe 60");
        KontnaGrupa kg601 = new KontnaGrupa();
        kg601.setBrojKonta("601");
        kg601.setNazivKonta("Naziv kontne grupe 601");


        KontnaGrupa kg6t = new KontnaGrupa();
        kg6t.setBrojKonta("6");
        kg6t.setNazivKonta("Naziv kontne grupe 6");

        KontnaGrupa kg61 = new KontnaGrupa();
        kg61.setBrojKonta("61");
        kg61.setNazivKonta("Naziv kontne grupe 61");

        KontnaGrupa kg611 = new KontnaGrupa();
        kg611.setBrojKonta("611");
        kg611.setNazivKonta("Naziv kontne grupe 511");

        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg5t,
                                                         kg51,
                                                         kg511,
                                                         kg52,
                                                         kg521,
                                                         kg50,
                                                         kg501,
                                                         kg521,
                                                         kg5t,
                                                         kg51,
                                                         kg511,
                                                         kg62,
                                                         kg621,
                                                         kg60,
                                                         kg601,
                                                         kg6t,
                                                         kg61,
                                                         kg611));
        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg3t, kg30, kg301, kg302, kg306, kg309, kg31, kg32, kg33, kg34, kg35));

        Knjizenje knj1 = new Knjizenje();
        knj1.setBrojNaloga("N123S3");
        knj1.setDatumKnjizenja(new Date());
        knj1.setDokument(fi1);
        Knjizenje knj2 = new Knjizenje();
        knj2.setDatumKnjizenja(new Date());
        knj2.setBrojNaloga("N123FF3");
        knj2.setDokument(fi1);
        Knjizenje knj3 = new Knjizenje();
        knj3.setDatumKnjizenja(new Date());
        knj3.setDokument(fi2);
        Knjizenje knj4 = new Knjizenje();
        knj4.setDatumKnjizenja(new Date());
        knj4.setDokument(fi2);
        knj3.setBrojNaloga("N13S3");
        knj4.setBrojNaloga("N23FF3");
        knj1 = this.knjizenjeRepository.save(knj1);
        knj2 = this.knjizenjeRepository.save(knj2);
        knj3 = this.knjizenjeRepository.save(knj3);
        knj4 = this.knjizenjeRepository.save(knj4);

        Knjizenje knj2020 = new Knjizenje();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 5, 5);
        knj2020.setDatumKnjizenja(calendar.getTime());
        knj2020.setDokument(fi2);
        knj2020.setBrojNaloga("N2020FF");
        this.knjizenjeRepository.save(knj2020);

        Knjizenje knj2021 = new Knjizenje();
        calendar = Calendar.getInstance();
        calendar.set(2021, 5, 5);
        knj2021.setDatumKnjizenja(calendar.getTime());
        knj2021.setDokument(fi2);
        knj2021.setBrojNaloga("N2020FF");
        this.knjizenjeRepository.save(knj2021);

        Konto k30 = createKonto(kg301, knj2020, 1300.0, 848.0);
        Konto k301 = createKonto(kg301, knj2020, 700.0, 940.0);
        Konto k302 = createKonto(kg302, knj2020, 1000.0, 504.0);
        Konto k306 = createKonto(kg306, knj2020, 1003.0, 203.0);
        Konto k309 = createKonto(kg309, knj2020, 200.0, 504.0);
        Konto k31 = createKonto(kg311, knj2020, 2311.0, 2003.0);
        Konto k32 = createKonto(kg321, knj2020, 100.0, 504.0);
        Konto k33 = createKonto(kg331, knj2020, 450.0, 304.0);
        Konto k34 = createKonto(kg341, knj2020, 1030.0, 584.0);
        Konto k35 = createKonto(kg351, knj2020, 1020.0, 704.0);
        Konto k3t2 = createKonto(kg311, knj2021, 1700.0, 1504.0);
        Konto k3012 = createKonto(kg301, knj2021, 1090.0, 1004.0);
        Konto k3022 = createKonto(kg302, knj2021, 1200.0, 1504.0);
        Konto k3062 = createKonto(kg306, knj2021, 1430.0, 1594.0);
        Konto k3092 = createKonto(kg309, knj2021, 1000.0, 504.0);
        Konto k312 = createKonto(kg311, knj2021, 1090.0, 1004.0);
        Konto k322 = createKonto(kg321, knj2021, 1200.0, 1504.0);
        Konto k332 = createKonto(kg331, knj2021, 1430.0, 1594.0);
        Konto k342 = createKonto(kg341, knj2021, 1000.0, 504.0);

        Konto k51 = createKonto(kg511, knj2020, 1300.0, 848.0);
        Konto k511 = createKonto(kg511, knj2020, 700.0, 940.0);
        Konto k52 = createKonto(kg521, knj2020, 1000.0, 504.0);
        Konto k50 = createKonto(kg501, knj2020, 1003.0, 203.0);
        Konto k521 = createKonto(kg521, knj2020, 200.0, 504.0);
        Konto k62 = createKonto(kg621, knj2020, 1030.0, 584.0);
        Konto k60 = createKonto(kg601, knj2020, 1020.0, 704.0);
        Konto k601 = createKonto(kg601, knj2020, 1700.0, 1504.0);
        Konto k5t2 = createKonto(kg511, knj2021, 1299.0, 900.0);
        Konto k512 = createKonto(kg511, knj2021, 1500.0, 848.0);
        Konto k5112 = createKonto(kg511, knj2021, 700.0, 940.0);
        Konto k522 = createKonto(kg521, knj2021, 1000.0, 504.0);
        Konto k502 = createKonto(kg501, knj2021, 1203.0, 203.0);
        Konto k5212 = createKonto(kg521, knj2021, 200.0, 504.0);
        Konto k622 = createKonto(kg621, knj2021, 1030.0, 584.0);
        Konto k602 = createKonto(kg601, knj2021, 1020.0, 704.0);
        Konto k6012 = createKonto(kg601, knj2021, 1700.0, 1504.0);

        this.kontoRepository.saveAll(Arrays.asList(
            k30, k301, k302, k306, k309, k31, k32, k33, k34, k35, k3t2, k3012, k3022, k3062, k3092, k312, k322, k332, k342,
            k51, k511, k52, k50, k521, k62, k60, k601, k5t2, k512, k5112, k522, k502, k5212, k622, k602, k6012));

        log.info("Data loaded!");
    }
}

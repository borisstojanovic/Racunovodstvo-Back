package raf.si.racunovodstvo.nabavka.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.model.KonverzijaArtikal;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.model.enums.TipKalkulacije;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;

import java.util.ArrayList;
import java.util.Date;

@Component
public class BootstrapData implements CommandLineRunner {

    private KalkulacijaRepository kalkulacijaRepository;
    private LokacijaRepository lokacijaRepository;
    private ArtikalRepository artikalRepository;

    public BootstrapData(KalkulacijaRepository kalkulacijaRepository,
                         LokacijaRepository lokacijaRepository,
                         ArtikalRepository artikalRepository) {
        this.kalkulacijaRepository = kalkulacijaRepository;
        this.lokacijaRepository = lokacijaRepository;
        this.artikalRepository = artikalRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Kalkulacija k1 = napraviDefaultKalkulaciju("AAA");
        kalkulacijaRepository.save(k1);

        Kalkulacija k2 = napraviDefaultKalkulaciju("BBB");
        kalkulacijaRepository.save(k2);

        Kalkulacija k3 = napraviDefaultKalkulaciju("CCC");
        kalkulacijaRepository.save(k3);

        KonverzijaArtikal konverzijaArtikal = new KonverzijaArtikal();
        konverzijaArtikal.setSifraArtikla("SIFRA");
        konverzijaArtikal.setKolicina(1);
        konverzijaArtikal.setNabavnaCena(1.0);
        konverzijaArtikal.setNazivArtikla("NAZIV");
        konverzijaArtikal.setJedinicaMere("MERA");
        konverzijaArtikal.setRabat(2.0);
        konverzijaArtikal.setRabatProcenat(10.0);
        konverzijaArtikal.setUkupnaNabavnaVrednost(2.0);
        konverzijaArtikal.setNabavnaCenaPosleRabata(1.0);
        artikalRepository.save(konverzijaArtikal);

        KalkulacijaArtikal kalkulacijaArtikal = new KalkulacijaArtikal();
        kalkulacijaArtikal.setKolicina(1);
        kalkulacijaArtikal.setNabavnaCena(1.0);
        kalkulacijaArtikal.setSifraArtikla("SIFRA2");
        kalkulacijaArtikal.setNazivArtikla("NAZIV2");
        kalkulacijaArtikal.setJedinicaMere("MERA");
        kalkulacijaArtikal.setRabat(2.0);
        kalkulacijaArtikal.setRabatProcenat(10.0);
        kalkulacijaArtikal.setUkupnaNabavnaVrednost(2.0);
        kalkulacijaArtikal.setNabavnaCenaPosleRabata(1.0);
        kalkulacijaArtikal.setMarza(3.0);
        kalkulacijaArtikal.setMarzaProcenat(20.0);
        kalkulacijaArtikal.setOsnovica(19.0);
        kalkulacijaArtikal.setPorez(10.0);
        kalkulacijaArtikal.setPorezProcenat(10.0);
        kalkulacijaArtikal.setProdajnaCena(30.0);
        kalkulacijaArtikal.setUkupnaProdajnaVrednost(45.0);
        kalkulacijaArtikal.setProdajnaOsnovica(40.0);
        artikalRepository.save(kalkulacijaArtikal);
    }

    private Kalkulacija napraviDefaultKalkulaciju(String brojKalkulacije) {
        Lokacija l1 = new Lokacija();
        l1.setAdresa("adresa");
        l1.setNaziv("naziv");
        l1 = lokacijaRepository.save(l1);

        Kalkulacija kalkulacija = new Kalkulacija();
        kalkulacija.setBrojKalkulacije(brojKalkulacije);
        kalkulacija.setTipKalkulacije(TipKalkulacije.VELEPRODALA);
        kalkulacija.setTroskoviNabavke(new ArrayList<>());
        kalkulacija.setLokacija(l1);
        kalkulacija.setDatum(new Date());
        kalkulacija.setDobavljacId(1L);
        kalkulacija.setArtikli(new ArrayList<>());
        kalkulacija.setFakturnaCena(100D);
        kalkulacija.setNabavnaCena(200D);
        kalkulacija.setProdajnaCena(300D);
        kalkulacija.setValuta("RSD");
        return kalkulacija;
    }
}

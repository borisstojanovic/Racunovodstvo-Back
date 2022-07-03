package raf.si.racunovodstvo.preduzece.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.model.enums.PolZaposlenog;
import raf.si.racunovodstvo.preduzece.model.enums.RadnaPozicija;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.*;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;
import raf.si.racunovodstvo.preduzece.services.impl.PlataService;
import raf.si.racunovodstvo.preduzece.services.impl.ZaposleniService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final PreduzeceRepository preduzeceRepository;
    private final ZaposleniRepository zaposleniRepository;
    private final ZaposleniService zaposleniService;
    private final StazRepository stazRepository;
    private final PlataRepository plataRepository;
    private final PlataService plataService;
    private final KoeficijentRepository koeficijentRepository;
    private final ObracunZaposleniService obracunZaposleniService;

    public BootstrapData(PreduzeceRepository preduzeceRepository,
                         ZaposleniRepository zaposleniRepository,
                         ZaposleniService zaposleniService, StazRepository stazRepository,
                         PlataRepository plataRepository,
                         PlataService plataService,
                         KoeficijentRepository koeficijentRepository,
                         ObracunZaposleniService obracunZaposleniService) {
        this.preduzeceRepository = preduzeceRepository;
        this.zaposleniRepository = zaposleniRepository;
        this.zaposleniService = zaposleniService;
        this.stazRepository = stazRepository;
        this.plataRepository = plataRepository;
        this.plataService = plataService;
        this.koeficijentRepository = koeficijentRepository;
        this.obracunZaposleniService = obracunZaposleniService;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        log.info("Loading Data...");

        Preduzece p1 = new Preduzece();
        p1.setNaziv("ThinkStudio");
        p1.setPib("102979306");
        p1.setRacun("908‑10501‑97");
        p1.setAdresa("Držićeva 11");
        p1.setGrad("Beograd");
        p1.setTelefon("0112324444");
        p1.setEmail("office@thinkstudio.com");
        p1.setWebAdresa("thinkstudio.com");
        p1.setIsActive(true);

        Preduzece p2 = new Preduzece();
        p2.setNaziv("Universal UK");
        p2.setPib("123848392");
        p2.setRacun("55743513");
        p2.setAdresa("22 Old Gloucester Street");
        p2.setGrad("London");
        p2.setTelefon("0113928422");
        p2.setEmail("london@universal.co.uk");
        p2.setWebAdresa("universal.co.uk");
        p2.setIsActive(true);
        p2.setKomentar("UK");

        Preduzece p3 = new Preduzece();
        p3.setNaziv("Blue Marble Inc");
        p3.setPib("194382931");
        p3.setRacun("908‑11501‑07");
        p3.setAdresa("Bulevar Mihajla Pupina 13");
        p3.setGrad("Beograd");
        p3.setTelefon("7975777666");
        p3.setEmail("office@bluemarble.com");
        p3.setWebAdresa("bluemarble.com");
        p3.setIsActive(true);

        Preduzece p4 = new Preduzece();
        p4.setNaziv("BP Production");
        p4.setPib("132355513");
        p4.setRacun("123-442-1134");
        p4.setAdresa("7115 3rd Ave");
        p4.setGrad("New York City");
        p4.setTelefon("555-1234");
        p4.setEmail("contact@bp.com");
        p4.setWebAdresa("bp.com");
        p4.setIsActive(true);
        p4.setKomentar("SAD");

        Preduzece p5 = new Preduzece();
        p5.setNaziv("Mark Cinema");
        p5.setPib("129438322");
        p5.setRacun("908‑14501‑28");
        p5.setAdresa("Bulevar Zorana Đinđića 11");
        p5.setGrad("Beograd");
        p5.setTelefon("0119303492");
        p5.setEmail("markcinema@gmail.com");
        p5.setWebAdresa("markcinema.rs");
        p5.setIsActive(false);

        Preduzece p6 = new Preduzece();
        p6.setNaziv("Fashion World");
        p6.setPib("193849293");
        p6.setRacun("908‑28311‑28");
        p6.setAdresa("Semjuela Beketa 55");
        p6.setGrad("Beograd");
        p6.setTelefon("011948293");
        p6.setEmail("office@fashionworld.com");
        p6.setWebAdresa("fashionworld.com");
        p6.setIsActive(false);

        this.preduzeceRepository.save(p1);
        this.preduzeceRepository.save(p2);
        this.preduzeceRepository.save(p3);
        this.preduzeceRepository.save(p4);
        this.preduzeceRepository.save(p5);
        this.preduzeceRepository.save(p6);

        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setKoeficijentPoreza(1d);
        koeficijent.setNezaposlenost1(2d);
        koeficijent.setNezaposlenost2(10d);
        koeficijent.setPenzionoOsiguranje1(5d);
        koeficijent.setPenzionoOsiguranje2(50d);
        koeficijent.setNajnizaOsnovica(1d);
        koeficijent.setNajvisaOsnovica(1d);
        koeficijent.setZdravstvenoOsiguranje1(5d);
        koeficijent.setZdravstvenoOsiguranje2(5d);
        koeficijent.setPoreskoOslobadjanje(23.4);
        koeficijentRepository.save(koeficijent);

        Zaposleni z1 = new Zaposleni();
        z1.setIme("Darko");
        z1.setPrezime("Stanković");
        z1.setImeRoditelja("Miodrag");
        z1.setPocetakRadnogOdnosa(getDate(2018, 1, 22));
        z1.setJmbg("0311988710341");
        z1.setPol(PolZaposlenog.MUSKO);
        z1.setDatumRodjenja(getDate(1988, 11, 13));
        z1.setAdresa("Držićeva 5");
        z1.setGrad("Beograd");
        z1.setBrojRacuna("908‑10501‑97");
        z1.setStepenObrazovanja("5");
        z1.setBrojRadneKnjizice(62834L);
        z1.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z1.setRadnaPozicija(RadnaPozicija.MENADZER);
        z1.setPreduzece(p1);
        zaposleniRepository.save(z1);

        Plata pl1 = this.plataService.save(new PlataRequest(1L, 70000.00, z1.getPocetakRadnogOdnosa(), z1.getZaposleniId()));
        pl1.setZaposleni(z1);

        Zaposleni z2 = new Zaposleni();
        z2.setIme("Marko");
        z2.setPrezime("Jovanović");
        z2.setImeRoditelja("Pavle");
        z2.setPocetakRadnogOdnosa(getDate(2018, 1, 22));
        z2.setJmbg("0502999710381");
        z2.setPol(PolZaposlenog.MUSKO);
        z2.setDatumRodjenja(getDate(1999, 2, 5));
        z2.setAdresa("Bulevar Nikole Tesle 33");
        z2.setGrad("Beograd");
        z2.setBrojRacuna("908‑10308‑97");
        z2.setStepenObrazovanja("5");
        z2.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z1.setRadnaPozicija(RadnaPozicija.RADNIK);
        z2.setKomentar("omladinska");
        z2.setPreduzece(p2);
        zaposleniRepository.save(z2);

        Plata pl2 = this.plataService.save(new PlataRequest(2L, 70000.00, z2.getPocetakRadnogOdnosa(), z2.getZaposleniId()));
        pl2.setZaposleni(z2);

        Zaposleni z3 = new Zaposleni();
        z3.setIme("Bojana");
        z3.setPrezime("Šolak");
        z3.setImeRoditelja("Marko");
        z3.setPocetakRadnogOdnosa(getDate(2017, 5, 15));
        z3.setJmbg("0904978710699");
        z3.setPol(PolZaposlenog.ZENSKO);
        z3.setDatumRodjenja(getDate(1978, 4, 9));
        z1.setRadnaPozicija(RadnaPozicija.DIREKTOR);
        z3.setAdresa("Trg Republike 4");
        z3.setGrad("Beograd");
        z3.setBrojRacuna("903‑14308‑97");
        z3.setStepenObrazovanja("6");
        z3.setBrojRadneKnjizice(33456L);
        z3.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z3.setPreduzece(p3);
        zaposleniRepository.save(z3);

        Plata pl3 = this.plataService.save(new PlataRequest(3L, 115300.00, z3.getPocetakRadnogOdnosa(), z3.getZaposleniId()));
        pl3.setZaposleni(z3);

        Zaposleni z4 = new Zaposleni();
        z4.setIme("Darko");
        z4.setPrezime("Ognjenović");
        z4.setImeRoditelja("Aleksa");
        z4.setPocetakRadnogOdnosa(getDate(2019, 4, 15));
        z4.setJmbg("0101995710121");
        z4.setPol(PolZaposlenog.MUSKO);
        z4.setDatumRodjenja(getDate(1995, 1, 1));
        z1.setRadnaPozicija(RadnaPozicija.RADNIK);
        z4.setAdresa("Masarikova 11");
        z4.setGrad("Beograd");
        z4.setBrojRacuna("903‑33308‑97");
        z4.setStepenObrazovanja("6");
        z4.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z4.setPreduzece(p4);
        zaposleniRepository.save(z4);

        Plata pl4 = this.plataService.save(new PlataRequest(4L, 230000.00, z4.getPocetakRadnogOdnosa(), z4.getZaposleniId()));
        pl4.setZaposleni(z4);

        Zaposleni z5 = new Zaposleni();
        z5.setIme("Dimitrije");
        z5.setPrezime("Zdravković");
        z5.setImeRoditelja("Kosta");
        z5.setPocetakRadnogOdnosa(getDate(2021, 1, 1));
        z5.setJmbg("0711987710241");
        z5.setPol(PolZaposlenog.MUSKO);
        z5.setDatumRodjenja(getDate(1987, 11, 27));
        z1.setRadnaPozicija(RadnaPozicija.RADNIK);
        z5.setAdresa("Bulevar Zorana Đinđića 1");
        z5.setGrad("Beograd");
        z5.setBrojRacuna("933‑47345‑92");
        z5.setStepenObrazovanja("6");
        z5.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z5.setPreduzece(p5);
        zaposleniRepository.save(z5);

        Plata pl5 = this.plataService.save(new PlataRequest(5L, 110000.00, z5.getPocetakRadnogOdnosa(), z5.getZaposleniId()));
        pl5.setZaposleni(z5);

        this.zaposleniRepository.save(z1);
        this.zaposleniRepository.save(z2);
        this.zaposleniRepository.save(z3);
        this.zaposleniRepository.save(z4);
        this.zaposleniRepository.save(z5);

        Staz staz = new Staz();
        staz.setPocetakRada(simpleDateFormat.parse("22-01-2018"));
        staz.setKrajRada(null);
        staz.setZaposleni(z1);
        stazRepository.save(staz);

        Staz staz2 = new Staz();
        staz2.setPocetakRada(simpleDateFormat.parse("22-01-2018"));
        staz2.setKrajRada(null);
        staz2.setZaposleni(z2);
        stazRepository.save(staz2);

        Staz staz3 = new Staz();
        staz3.setPocetakRada(simpleDateFormat.parse("15-05-2017"));
        staz3.setKrajRada(null);
        staz3.setZaposleni(z3);
        stazRepository.save(staz3);

        Staz staz4 = new Staz();
        staz4.setPocetakRada(simpleDateFormat.parse("15-05-2019"));
        staz4.setKrajRada(null);
        staz4.setZaposleni(z4);
        stazRepository.save(staz4);

        Staz staz5 = new Staz();
        staz5.setPocetakRada(simpleDateFormat.parse("01-01-2021"));
        staz5.setKrajRada(null);
        staz5.setZaposleni(z5);
        stazRepository.save(staz5);

       obracunZaposleniService.makeObracun(new Date(), 1);

        log.info("Data loaded!");
    }

    private Date getDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

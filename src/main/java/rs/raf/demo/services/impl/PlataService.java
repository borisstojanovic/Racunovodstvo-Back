package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Konto;
import rs.raf.demo.model.Plata;
import rs.raf.demo.model.Zaposleni;
import rs.raf.demo.repositories.PlataRepository;
import rs.raf.demo.requests.PlataRequest;
import rs.raf.demo.services.IService;
import rs.raf.demo.utils.SearchUtil;

import javax.persistence.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlataService implements IService<Plata, Long> {
    private final PlataRepository plataRepository;
    @Autowired
    private IService<Zaposleni, Long> zaposleniService;
    private final KoeficijentService koeficijentService;

    private SearchUtil<Plata> searchUtil;

    public PlataService(PlataRepository plataRepository, KoeficijentService koeficijentService) {
        this.plataRepository = plataRepository;
        this.koeficijentService = koeficijentService;
        this.searchUtil = new SearchUtil<Plata>();
    }

    @Override
    public <S extends Plata> S save(S var1) {
        return this.plataRepository.save(var1);
    }

    public Plata save(PlataRequest plataRequest) {
        Optional<Zaposleni> optionalZaposleni = zaposleniService.findById(plataRequest.getZaposleniId());

        if(!optionalZaposleni.isPresent()) {
            throw new EntityNotFoundException();
        }

        Zaposleni zaposleni = optionalZaposleni.get();
        updateDatumDoNaStarojPlati(zaposleni);
        Plata plata = new Plata();
        plata.setNetoPlata(plataRequest.getNetoPlata());
        plata.setDatumOd(plataRequest.getDatum());
        plata.setZaposleni(zaposleni);
        plata.izracunajDoprinose(this.koeficijentService.getCurrentKoeficijent());
        return this.plataRepository.save(plata);
    }

    private void updateDatumDoNaStarojPlati(Zaposleni zaposleni) {
        List<Plata> plate = plataRepository.findAll()
                                           .stream()
                                           .filter(plata -> plata.getZaposleni().getZaposleniId().equals(zaposleni.getZaposleniId()) && plata.getDatumDo() == null)
                                           .collect(Collectors.toList());

        Plata plata = plate.get(0);
        plata.setDatumDo(new Date());
        plataRepository.save(plata);
    }

    @Override
    public Optional<Plata> findById(Long var1) {
        return this.plataRepository.findByPlataId(var1);
    }

    @Override
    public List<Plata> findAll() {
        return this.plataRepository.findAll();
    }

    public List<Plata> findAll(Specification<Plata> spec) {
        return this.plataRepository.findAll(spec);
    }

    @Override
    public void deleteById(Long var1) {
        this.plataRepository.deleteById(var1);
    }

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId) {
        return this.plataRepository.findByZaposleniZaposleniId(zaposleniId);
    }
}

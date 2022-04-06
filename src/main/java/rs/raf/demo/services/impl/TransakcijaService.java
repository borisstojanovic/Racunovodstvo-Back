package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.repositories.TransakcijaRepository;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class TransakcijaService implements IService {

    private final TransakcijaRepository transakcijaRepository;

    public TransakcijaService(TransakcijaRepository transakcijaRepository) {
        this.transakcijaRepository = transakcijaRepository;
    }

    @Override
    public Object save(Object var1) {
        return null;
    }

    @Override
    public Optional findById(Object var1) {
        return Optional.empty();
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public void deleteById(Object var1) {

    }
}

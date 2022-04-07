package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.repositories.DokumentRepository;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService implements IService<Dokument, Long> {

    private final DokumentRepository dokumentRepository;

    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    @Override
    public Dokument save(Dokument id) {
        return dokumentRepository.save(id);
    }

    @Override
    public Optional<Dokument> findById(Long id) {
        return dokumentRepository.findById(id);
    }

    @Override
    public List<Dokument> findAll() {
        return dokumentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        dokumentRepository.deleteById(id);
    }
}

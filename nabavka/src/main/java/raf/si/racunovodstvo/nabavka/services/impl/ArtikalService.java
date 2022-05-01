package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;
import raf.si.racunovodstvo.nabavka.services.IArtikalService;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class ArtikalService implements IArtikalService {

    private final ArtikalRepository artikalRepository;
    private final IConverter<Artikal, ArtikalResponse> artikalReverseConverter;
    private final IConverter<ArtikalRequest, Artikal> artikalConverter;

    public ArtikalService(ArtikalRepository artikalRepository,
                          IConverter<Artikal, ArtikalResponse> artikalReverseConverter,
                          IConverter<ArtikalRequest, Artikal> artikalConverter) {
        this.artikalRepository = artikalRepository;
        this.artikalReverseConverter = artikalReverseConverter;
        this.artikalConverter = artikalConverter;
    }

    @Override
    public Page<ArtikalResponse> findAll(Pageable pageable) {
        return artikalRepository.findAll(pageable).map(artikalReverseConverter::convert);
    }

    @Override
    public ArtikalResponse save(ArtikalRequest artikalRequest) {
        Artikal converted = artikalConverter.convert(artikalRequest);
        return artikalReverseConverter.convert(artikalRepository.save(converted));
    }

    @Override
    public ArtikalResponse update(ArtikalRequest artikalRequest) {
        Optional<Artikal> optionalArtikal = artikalRepository.findById(artikalRequest.getArtikalId());
        if (optionalArtikal.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Artikal converted = artikalConverter.convert(artikalRequest);
        converted.setArtikalId(artikalRequest.getArtikalId());
        return artikalReverseConverter.convert(artikalRepository.save(converted));
    }

    @Override
    public <S extends Artikal> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Artikal> findById(String var1) {
        return Optional.empty();
    }

    @Override
    public List<Artikal> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {
        Optional<Artikal> optionalArtikal = artikalRepository.findById(var1);
        if (optionalArtikal.isEmpty()) {
            throw new EntityNotFoundException();
        }
        artikalRepository.deleteById(var1);
    }
}

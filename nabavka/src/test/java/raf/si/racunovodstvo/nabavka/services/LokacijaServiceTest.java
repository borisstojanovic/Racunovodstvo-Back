package raf.si.racunovodstvo.nabavka.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;
import raf.si.racunovodstvo.nabavka.services.impl.LokacijaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LokacijaServiceTest {

    @InjectMocks
    private LokacijaService lokacijaService;
    @Mock
    private LokacijaRepository lokacijaRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void save() {
        Lokacija lokacija = new Lokacija();
        given(lokacijaRepository.save(lokacija)).willReturn(lokacija);

        assertEquals(lokacija, lokacijaService.save(lokacija));
    }

    @Test
    void findById() {
        Lokacija lokacija = new Lokacija();
        given(lokacijaRepository.findByLokacijaId(MOCK_ID)).willReturn(Optional.of(lokacija));

        assertEquals(lokacija, lokacijaService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Lokacija> lokacijaList = new ArrayList<>();
        given(lokacijaRepository.findAll()).willReturn(lokacijaList);

        assertEquals(lokacijaList, lokacijaService.findAll());
    }

    @Test
    void deleteById() {
        lokacijaService.deleteById(MOCK_ID);
        then(lokacijaRepository).should(times(1)).deleteById(MOCK_ID);
    }
}
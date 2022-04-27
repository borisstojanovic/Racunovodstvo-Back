package raf.si.racunovodstvo.nabavka.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KonverzijaServiceTest {

    @InjectMocks
    private KonverzijaService konverzijaService;

    @Mock
    private KonverzijaRepository konverzijaRepository;

    @org.junit.jupiter.api.Test
    void findAll() {
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
    }

    @org.junit.jupiter.api.Test
    void save() {
    }
}
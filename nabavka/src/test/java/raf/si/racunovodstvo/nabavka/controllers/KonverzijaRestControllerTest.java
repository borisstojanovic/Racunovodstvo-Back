package raf.si.racunovodstvo.nabavka.controllers;

import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmCompositeKeyManyToOneType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.responses.PreduzeceResponse;
import raf.si.racunovodstvo.nabavka.services.impl.KonverzijaService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KonverzijaRestControllerTest {

    @InjectMocks
    private KonverzijaRestController konverzijaRestController;
    @Mock
    private KonverzijaService konverzijaService;
    @Mock
    private RestTemplate restTemplate;

    private static final Long MOCK_ID = 1L;

    @Test
    void search() throws IOException {
        String search = "knjizenjeId:1";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        ResponseEntity<?> responseEntity = konverzijaRestController.search(search, page, size, sort, new String());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void createKonverzija() throws IOException {
        KonverzijaRequest konverzija = new KonverzijaRequest();
        konverzija.setDobavljacId(1L);
        given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).willReturn(ResponseEntity.ok("{\"preduzeceId\":1}"));
        ResponseEntity<?> responseEntity = konverzijaRestController.createKonverzija(konverzija, new String());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteKonverzija() {
        Konverzija konverzija = new Konverzija();
        konverzija.setId(MOCK_ID);
        given(konverzijaService.findById(MOCK_ID)).willReturn(Optional.of(konverzija));
        ResponseEntity<?> responseEntity = konverzijaRestController.deleteKonverzija(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
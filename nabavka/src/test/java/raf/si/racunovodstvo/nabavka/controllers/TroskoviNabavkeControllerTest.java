package raf.si.racunovodstvo.nabavka.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.services.impl.TroskoviNabavkeService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TroskoviNabavkeControllerTest {

    @InjectMocks
    private TroskoviNabavkeController troskoviNabavkeController;
    @Mock
    private TroskoviNabavkeService troskoviNabavkeService;

    private static final Long MOCK_ID = 1L;

    private TroskoviNabavke getTrosakNabavke(){
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        troskoviNabavke.setTroskoviNabavkeId(MOCK_ID);
        troskoviNabavke.setNaziv("Naziv");
        troskoviNabavke.setCena(100.0);
        return troskoviNabavke;
    }

    @Test
    void createTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.createTroskoviNabavke(getTrosakNabavke());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateTroskoviNabavke() {
        given(troskoviNabavkeService.findById(MOCK_ID)).willReturn(Optional.of(getTrosakNabavke()));

        ResponseEntity<?> responseEntity = troskoviNabavkeController.updateTroskoviNabavke(getTrosakNabavke());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateTroskoviNabavkeException() {

        given(troskoviNabavkeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> troskoviNabavkeController.updateTroskoviNabavke(getTrosakNabavke()));
    }

    @Test
    void getTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.getTroskoviNabavke();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteTroskoviNabavke() {
        given(troskoviNabavkeService.findById(MOCK_ID)).willReturn(Optional.of(getTrosakNabavke()));
        ResponseEntity<?> responseEntity = troskoviNabavkeController.deleteTroskoviNabavke(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteTroskoviNabavkeException() {
        assertThrows(EntityNotFoundException.class, () -> troskoviNabavkeController.deleteTroskoviNabavke(MOCK_ID));
    }


}
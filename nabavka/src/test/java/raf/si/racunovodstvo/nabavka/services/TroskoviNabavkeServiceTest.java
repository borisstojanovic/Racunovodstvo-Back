package raf.si.racunovodstvo.nabavka.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.TroskoviNabavkeRepository;
import raf.si.racunovodstvo.nabavka.services.impl.TroskoviNabavkeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TroskoviNabavkeServiceTest {

    @InjectMocks
    private TroskoviNabavkeService troskoviNabavkeService;
    @Mock
    private TroskoviNabavkeRepository troskoviNabavkeRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void save() {
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        given(troskoviNabavkeRepository.save(troskoviNabavke)).willReturn(troskoviNabavke);

        assertEquals(troskoviNabavke, troskoviNabavkeService.save(troskoviNabavke));
    }

    @Test
    void findById() {
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        given(troskoviNabavkeRepository.findByTroskoviNabavkeId(MOCK_ID)).willReturn(Optional.of(troskoviNabavke));

        assertEquals(troskoviNabavke, troskoviNabavkeService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<TroskoviNabavke> troskoviNabavkeList = new ArrayList<>();
        given(troskoviNabavkeRepository.findAll()).willReturn(troskoviNabavkeList);

        assertEquals(troskoviNabavkeList, troskoviNabavkeService.findAll());
    }

    @Test
    void deleteById() {
        troskoviNabavkeService.deleteById(MOCK_ID);
        then(troskoviNabavkeRepository).should(times(1)).deleteById(MOCK_ID);
    }
}
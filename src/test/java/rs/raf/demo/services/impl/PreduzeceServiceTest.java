package rs.raf.demo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.repositories.PreduzeceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PreduzeceServiceTest {

    @InjectMocks
    private PreduzeceService preduzeceService;

    @Mock
    private PreduzeceRepository preduzeceRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void testSave() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceRepository.save(preduzece)).willReturn(preduzece);

        assertEquals(preduzece, preduzeceService.save(preduzece));
    }

    @Test
    void testFindById() {
        Optional<Preduzece> optionalPreduzece = Optional.of(new Preduzece());
        given(preduzeceRepository.findByPreduzeceId(MOCK_ID)).willReturn(optionalPreduzece);

        assertEquals(optionalPreduzece, preduzeceService.findById(MOCK_ID));
    }

    @Test
    void testFindAll() {
        List<Preduzece> preduzeceList = new ArrayList<>();
        given(preduzeceRepository.findAll()).willReturn(preduzeceList);

        assertEquals(preduzeceList, preduzeceService.findAll());
    }

    @Test
    void testDeleteById() {
        preduzeceService.deleteById(MOCK_ID);

        then(preduzeceRepository).should(times(1)).deleteById(MOCK_ID);
    }
}

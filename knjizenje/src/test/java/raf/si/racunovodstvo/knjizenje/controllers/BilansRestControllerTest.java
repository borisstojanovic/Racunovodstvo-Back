package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.services.BilansService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BilansRestControllerTest {

    @InjectMocks
    private BilansRestController bilansRestController;
    @Mock
    private BilansService bilansService;

    @Test
    void getBilans() {
        List<Date> datumiOd  = new ArrayList<>();
        List<Date> datumiDo  = new ArrayList<>();
        datumiOd.add(new Date());
        datumiDo.add(new Date());

        String brojKontaOd = "1";
        String brojKontaDo = "2";

        List<BilansResponse> bilansResponseList = new ArrayList<>();
        bilansResponseList.add(new BilansResponse(500.0, 500.0, 1L, "", ""));

        given(bilansService.findBilans(any(List.class), any(List.class), any(List.class))).willReturn(bilansResponseList);
        given(bilansService.findBrutoBilans(any(String.class), any(String.class), any(Date.class), any(Date.class))).willReturn(bilansResponseList);

        ResponseEntity<?> responseEntity1 = bilansRestController.getBilansStanja(datumiOd, datumiDo);
        assertEquals(responseEntity1.getStatusCodeValue(), 200);

        ResponseEntity<?> responseEntity2 = bilansRestController.getBilansUspeha(datumiOd, datumiDo);
        assertEquals(responseEntity2.getStatusCodeValue(), 200);

        ResponseEntity<?> responseEntity3 = bilansRestController.getBrutoBilans(brojKontaOd, brojKontaDo, datumiOd.get(0), datumiDo.get(0));
        assertEquals(responseEntity3.getStatusCodeValue(), 200);
    }

    @Test
    void getBilansNotFound() {
        List<Date> datumiOd  = new ArrayList<>();
        List<Date> datumiDo  = new ArrayList<>();
        datumiOd.add(new Date());
        datumiDo.add(new Date());

        String brojKontaOd = "1";
        String brojKontaDo = "2";

        ResponseEntity<?> responseEntity1 = bilansRestController.getBilansStanja(datumiOd, datumiDo);
        assertEquals(responseEntity1.getStatusCodeValue(), 404);

        ResponseEntity<?> responseEntity2 = bilansRestController.getBilansUspeha(datumiOd, datumiDo);
        assertEquals(responseEntity2.getStatusCodeValue(), 404);

        ResponseEntity<?> responseEntity3 = bilansRestController.getBrutoBilans(brojKontaOd, brojKontaDo, datumiOd.get(0), datumiDo.get(0));
        assertEquals(responseEntity3.getStatusCodeValue(), 404);
    }
}
package raf.si.racunovodstvo.knjizenje.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "eureka.client.enabled=false"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransakcijaIntegrationTest {

    private final static String URI = "/api/transakcije";

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final TipTransakcije MOCK_TIP_TRANSAKCIJE = TipTransakcije.ISPLATA;
    private static final String MOCK_BROJ_TRANSAKCIJE = "123";
    private static final double MOCK_IZNOS = 22.3;
    private static final Long MOCK_ID = 1L;
    private static final String MOCK_BROJ_DOKUMENTA = "1111";
    private static final TipDokumenta MOCK_TIP_DOKUMENTA = TipDokumenta.TRANSAKCIJA;
    private static final Date MOCK_DATUM = new Date();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper();

        Transakcija tr = new Transakcija();
        tr.setTipTransakcije(MOCK_TIP_TRANSAKCIJE);
        tr.setIznos(MOCK_IZNOS);
        tr.setBrojTransakcije(MOCK_BROJ_TRANSAKCIJE);
        tr.setDokumentId(MOCK_ID);
        tr.setBrojDokumenta(MOCK_BROJ_DOKUMENTA);
        tr.setTipDokumenta(MOCK_TIP_DOKUMENTA);
        tr.setDatumTransakcije(MOCK_DATUM);
        transakcijaRepository.save(tr);
    }

    @Test
    @Order(1)
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete(URI + "/556545")).andExpect(status().isNotFound());
    }
}
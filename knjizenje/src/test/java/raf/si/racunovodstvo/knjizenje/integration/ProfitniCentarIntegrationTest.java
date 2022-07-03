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
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "eureka.client.enabled=false"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfitniCentarIntegrationTest {

    private final static String URI = "/api/profitni_centri";

    @Autowired
    private ProfitniCentarRepository profitniCentarRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String MOCK_SIFRA = "MOCK_SIFRA";
    private static final String MOCK_NAZIV = "MOCK_NAZIV";
    private static final Long MOCK_LOKACIJAID = 11L;
    private static final Long MOCK_LICEID = 1L;
    private static final Double MOCK_TROSAK = 200.00;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper();

        ProfitniCentar pc1 = new ProfitniCentar();
        pc1.setSifra(MOCK_SIFRA);
        pc1.setNaziv(MOCK_NAZIV);
        pc1.setLokacijaId(MOCK_LOKACIJAID);
        pc1.setOdgovornoLiceId(MOCK_LICEID);
        pc1.setUkupniTrosak(MOCK_TROSAK);
        profitniCentarRepository.save(pc1);
        var x = pc1.getId();
        System.out.println("le ludi " + x);
    }

    @Test
    @Order(1)
    void findAllTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI + "/11").header("Authorization", "sssss")).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        ProfitniCentar dva = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ProfitniCentar>() {});
        assertEquals(MOCK_SIFRA, dva.getSifra());
    }
}

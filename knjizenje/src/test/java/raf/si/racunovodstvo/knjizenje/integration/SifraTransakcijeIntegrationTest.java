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
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "eureka.client.enabled=false"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SifraTransakcijeIntegrationTest {

    private final static String URI = "/api/sifraTransakcije";

    @Autowired
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long MOCK_SIFRA = 111L;
    private static final String MOCK_NAZIV_TRANSAKCIJE = "MOCK_NAZIV_TRANSAKCIJE";
    private static final Long MOCK_ID = 1L;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper();

        SifraTransakcije st = new SifraTransakcije();
        st.setSifra(MOCK_SIFRA);
        st.setNazivTransakcije(MOCK_NAZIV_TRANSAKCIJE);
        st.setSifraTransakcijeId(MOCK_ID);
        sifraTransakcijeRepository.save(st);
    }

    @Test
    @Order(1)
    void findAllTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI).header("Authorization", "sssss")).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        List<SifraTransakcijeResponse> lista = objectMapper.convertValue(map.get("content"), new TypeReference<>() {});
        assertTrue(lista.stream().anyMatch(sifraTransakcijeResponse -> sifraTransakcijeResponse.getSifra().equals(MOCK_SIFRA)));
    }

    @Test
    @Order(2)
    void getAllTest() throws Exception {
        mockMvc.perform(get(URI).header("Authorization", "Bearer ")).andExpect(status().isOk()).andReturn();
    }

    @Test
    @Order(3)
    void getByIdTest() throws Exception {
        Long sifraTranskacijeId = sifraTransakcijeRepository.findById(MOCK_ID).get().getSifraTransakcijeId();
        mockMvc.perform(get(URI).param("userId", sifraTranskacijeId.toString()).header("Authorization", "Bearer "))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete(URI + "/556545")).andExpect(status().isNotFound());
    }
}
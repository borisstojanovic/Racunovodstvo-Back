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
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "eureka.client.enabled=false"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KnjizenjeIntegrationTest {

    private final static String URI = "/api/knjizenje";

    @Autowired
    private KnjizenjeRepository knjizenjeRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String MOCK_BR_NALOGA = "MOCK_BROJ_NALOGA";
    private static final Date MOCK_DATUM = new Date();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper();

        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setBrojNaloga(MOCK_BR_NALOGA);
        knjizenje.setDatumKnjizenja(MOCK_DATUM);
        var x = knjizenjeRepository.save(knjizenje);
        System.out.println("leee " + x.getKnjizenjeId());
    }

    @Test
    @Order(1)
    void findAllTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/knjizenje/5").header("Authorization", "sssss")).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        Knjizenje dva = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Knjizenje>() {});
        assertEquals(MOCK_BR_NALOGA, dva.getBrojNaloga());
    }
}

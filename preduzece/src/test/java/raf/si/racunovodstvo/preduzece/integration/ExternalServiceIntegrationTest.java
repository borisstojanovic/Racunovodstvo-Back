package raf.si.racunovodstvo.preduzece.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.preduzece.integration.test_model.LoginRequest;
import raf.si.racunovodstvo.preduzece.integration.test_model.LoginResponse;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaradeConfigRequest;
import raf.si.racunovodstvo.preduzece.responses.KursnaListaResponse;
import raf.si.racunovodstvo.preduzece.responses.ObracunZaradeConfigResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalServiceIntegrationTest extends BaseIT {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private String token;
    private Long obracunId;

    private final static String URI_OBRACUN_CONFIG = "/api/obracun_zarade_config";
    private final static String URI_OBRADI_OBRACUN = "/api/obracun/obradi";
    private final static String URI_KURSNA_LISTA = "/api/kursna_lista";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObracunRepository obracunRepository;

    private MockMvc mockMvc;

    private static final String MOCK_NAZIV = "TESTNI OBRACUN";
    private static final Date MOCK_DATUM_OBRACUNA = new Date();

    @BeforeAll
    void setup() {
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = loginResponse.getJwt();

        Obracun obracun = new Obracun();
        obracun.setNaziv(MOCK_NAZIV);
        obracun.setObradjen(false);
        obracun.setDatumObracuna(MOCK_DATUM_OBRACUNA);
        obracun.setSifraTransakcije(1L);
        obracun.setObracunZaposleniList(new ArrayList<>());
        obracun = obracunRepository.save(obracun);
        obracunId = obracun.getObracunId();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    }

    @Test
    void setConfig() throws Exception {
        ObracunZaradeConfigRequest obracunZaradeConfigRequest = new ObracunZaradeConfigRequest();
        obracunZaradeConfigRequest.setDayOfMonth(1);
        obracunZaradeConfigRequest.setSifraTransakcijeId(1L);
        String requestJson = mapper.writeValueAsString(obracunZaradeConfigRequest);

        String result =
            mockMvc.perform(post(URI_OBRACUN_CONFIG).contentType(APPLICATION_JSON)
                                                    .content(requestJson)
                                                    .header("Authorization", "Bearer " + token))
                   .andExpect(status().isOk())
                   .andReturn()
                   .getResponse()
                   .getContentAsString();
        ObracunZaradeConfigResponse response = mapper.readValue(result, new TypeReference<>() {
        });
        assertEquals(1L, response.getSifraTransakcije().getSifraTransakcijeId());
    }

    @Test
    void getKursnaLista() throws Exception {
        String result = mockMvc.perform(get(URI_KURSNA_LISTA)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        KursnaListaResponse response = mapper.readValue(result, new TypeReference<>() {
        });
        assertNotNull(response.getResult());
    }

    @Test
    void obradiObracun() throws Exception {
        String result = mockMvc.perform(get(URI_OBRADI_OBRACUN + "/" + obracunId).header("Authorization", "Bearer " + token))
                               .andExpect(status().isOk())
                               .andReturn()
                               .getResponse()
                               .getContentAsString();
        System.out.println(result);
        List<Obracun> response = mapper.readValue(result, new TypeReference<>() {
        });
        System.out.println(response);
        Optional<Obracun> optionalObracun = response.stream().filter(obracun -> obracun.getObracunId().equals(obracunId)).findFirst();
        assertTrue(optionalObracun.isPresent());
        assertTrue(optionalObracun.get().isObradjen());
    }

    @SneakyThrows
    private <R> R postRest(String url, Object req, Class<R> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(req), headers);
        ResponseEntity<R> response = restTemplate.postForEntity(url, request, clazz);
        return response.getBody();
    }
}

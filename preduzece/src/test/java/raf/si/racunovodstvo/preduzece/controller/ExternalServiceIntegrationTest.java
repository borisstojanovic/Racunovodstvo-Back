package raf.si.racunovodstvo.preduzece.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"eureka.client.enabled=false"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
    //@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExternalServiceIntegrationTest {

    //private static final ObjectMapper mapper = new ObjectMapper();
    //private final RestTemplate restTemplate = new RestTemplate();
    private String token;

    private final static String URI = "/api/obracun_zarade_config";

    //@Autowired
    //private WebApplicationContext wac;

    private MockMvc mockMvc;
/*
    @BeforeAll
    void setup() {
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        System.out.println("HERE");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        System.out.println("HERE2");
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        System.out.println("HERE3");
        token = loginResponse.getJwt();
        System.out.println(token);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        System.out.println(token);
    }

 */
/*
    @AfterAll
    static void afterAll() {
        System.out.println("HERE AFTER ALL");
        eurekaContainer.stop();
        knjizenjeContainer.stop();
        userContainer.stop();
        gatewayContainer.stop();
        redisContainer.stop();
        mySQLSlaveContainer.stop();
        mySQLSlaveContainer1.stop();
        mySQLMasterContainer.stop();
    }

 */

    @Test
    void setConfig() {
        System.out.println("INSIDE TEST");
        assertTrue(token == null);
        /*
        System.out.println("INSIDE TEST");
        ObracunZaradeConfigRequest obracunZaradeConfigRequest = new ObracunZaradeConfigRequest();
        obracunZaradeConfigRequest.setDayOfMonth(1);
        obracunZaradeConfigRequest.setSifraTransakcijeId(1L);
        System.out.println("AFTER NEW");
        String requestJson = mapper.writeValueAsString(obracunZaradeConfigRequest);
        System.out.println("AFTER MAPPING");

        String result = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(requestJson).header("Authorization", token))
                               .andExpect(status().isOk())
                               .andReturn()
                               .getResponse()
                               .getContentAsString();
        System.out.println(result);
        //ObracunZaradeConfigResponse response = mapper.readValue(result, new TypeReference<>() {
        //});
        //assertEquals(1L, response.getSifraTransakcije().getSifraTransakcijeId());

         */
    }

    @SneakyThrows
    private <R> R postRest(String url, Object req, Class<R> clazz) {
        System.out.println("INSIDE POST");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        System.out.println("POSTING " + req);

        //HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(req), headers);
        //ResponseEntity<R> response = restTemplate.postForEntity(url, request, clazz);
        System.out.println("AFTER POST");
        //return response.getBody();
        return null;
    }
}

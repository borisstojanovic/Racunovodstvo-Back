package raf.si.racunovodstvo.user.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.repositories.PermissionRepository;
import raf.si.racunovodstvo.user.repositories.UserRepository;
import raf.si.racunovodstvo.user.utils.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisabledIfEnvironmentVariable(named = "WORKSPACE", matches = "CI")
class UserIntegrationTest extends BaseIT {

    private final static String URI = "/api/users";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    private static final String MOCK_UID = "MOCK_UID_2";
    private static final String MOCK_EMAIL = "MOCK_EMAIL_2";
    private static final String MOCK_PASSWORD = "MOCK_PASSWORD_2";
    private String jwtToken;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        User user = new User();
        user.setUsername(MOCK_UID);
        user.setEmail(MOCK_EMAIL);
        user.setPassword(passwordEncoder.encode(MOCK_PASSWORD));
        user.setPermissions(permissionRepository.findAll());
        userRepository.save(user);
        jwtToken = jwtUtil.generateToken(user.getUsername());
    }

    @Test
    @Order(1)
    void getAllTest() throws Exception {
        mockMvc.perform(get(URI + "/all").header("Authorization", "Bearer " + jwtToken)).andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void getAllUnauthenticatedTest() throws Exception {
        mockMvc.perform(get(URI + "/all")).andExpect(status().isForbidden());
    }

    @Test
    @Order(1)
    void getByIdTest() throws Exception {
        Long userId = userRepository.findByUsername(MOCK_UID).get().getUserId();
        mockMvc.perform(get(URI).param("userId", userId.toString()).header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void getByIdNotFoundTest() throws Exception {
        mockMvc.perform(get(URI).param("userId", "-1").header("Authorization", "Bearer " + jwtToken)).andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    void getLoginUserTest() throws Exception {
        mockMvc.perform(get(URI + "/loginuser").header("Authorization", "Bearer " + jwtToken)).andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void getLoginUserUnauthenticatedTest() throws Exception {
        mockMvc.perform(get(URI + "/loginuser")).andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    void deleteUserNotFoundTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + -1).header("Authorization", "Bearer " + jwtToken)).andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    void deleteUserTest() throws Exception {
        Long userId = userRepository.findByUsername(MOCK_UID).get().getUserId();
        mockMvc.perform(delete(URI + "/" + userId).header("Authorization", "Bearer " + jwtToken)).andExpect(status().isNoContent());
        assertTrue(userRepository.findByUsername(MOCK_UID).isEmpty());
    }
}

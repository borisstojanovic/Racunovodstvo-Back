package raf.si.racunovodstvo.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import raf.si.racunovodstvo.user.requests.LoginRequest;
import raf.si.racunovodstvo.user.utils.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;

    @Test
    void login() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        authController.login(request);
    }

//    @Test
//    void loginException() {
//        LoginRequest request = new LoginRequest();
//        request.setUsername("username");
//        request.setPassword("password");
//
//        Mockito.doThrow(AuthenticationException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        authController.login(request);
//    }

    @Test
    void access() {
        ResponseEntity<?> responseEntity = authController.access();
        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }
}
package raf.si.racunovodstvo.nabavka.integration.test_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}

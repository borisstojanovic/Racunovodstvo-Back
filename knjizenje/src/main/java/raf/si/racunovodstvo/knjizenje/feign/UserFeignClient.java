package raf.si.racunovodstvo.knjizenje.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;

@FeignClient(value = "${service.user.url}")
public interface UserFeignClient {

    @GetMapping("/api/users/loginuser")
    ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/access")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);
}

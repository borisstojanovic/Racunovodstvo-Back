package raf.si.racunovodstvo.nabavka.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("${service.user.url}")
public interface UserFeignClient {

    @GetMapping("/auth/access")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);
}

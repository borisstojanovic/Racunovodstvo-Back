package raf.si.racunovodstvo.nabavka.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import raf.si.racunovodstvo.nabavka.responses.PreduzeceResponse;

@FeignClient(value = "${service.preduzece.url}")
public interface PreduzeceFeignClient {

    @GetMapping("/api/preduzece/{id}")
    ResponseEntity<PreduzeceResponse> getPreduzeceById(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String token);
}

package rs.raf.demo.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.requests.BilansDateRequest;
import rs.raf.demo.services.impl.BilansService;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/bilans")
public class BilansRestController {

    private final BilansService bilansService;

    public BilansRestController(BilansService bilansService) {
        this.bilansService = bilansService;
    }


    @GetMapping(value = "/bilansStanja", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBilansStanja(@RequestParam List<Date> datumiOd, @RequestParam List<Date> datumiDo) {
        if (bilansService.findBilansStanja(datumiOd, datumiDo).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bilansService.findBilansStanja(datumiOd, datumiDo));
        }
    }

    @GetMapping(value = "/bilansUspeha", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBilansUspeha(@RequestParam List<Date> datumiOd, @RequestParam List<Date> datumiDo) {
        if (bilansService.findBilansUspeha(datumiOd, datumiDo).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bilansService.findBilansUspeha(datumiOd, datumiDo));
        }
    }

    @GetMapping(value = "/brutoBilans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBrutoBilans(@RequestParam String brojKontaOd,
                                            @RequestParam String brojKontaDo,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumOd,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumDo) {
        if (bilansService.findBrutoBilans(brojKontaOd, brojKontaDo, datumOd, datumDo).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bilansService.findBrutoBilans(brojKontaOd, brojKontaDo, datumOd, datumDo));
        }
    }
}

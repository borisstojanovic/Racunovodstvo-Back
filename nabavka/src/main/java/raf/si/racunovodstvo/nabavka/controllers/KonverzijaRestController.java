package raf.si.racunovodstvo.nabavka.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.KonverzijaService;
import raf.si.racunovodstvo.nabavka.services.impl.IKonverzijaService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/konverzije")
public class KonverzijaRestController {

    private final IKonverzijaService iKonverzijaService;

    private RestTemplate restTemplate;

    private final SearchUtil<Konverzija> searchUtil;

    private String URL = "http://preduzece/api/preduzece/%d";

    public KonverzijaRestController(KonverzijaService iKonverzijaService, RestTemplate restTemplate) {
        this.iKonverzijaService = iKonverzijaService;
        this.restTemplate = restTemplate;
        this.searchUtil = new SearchUtil<>();
    }

    private Preduzece getPreduzeceById(Long id, String token) throws IOException {
        if(id == null){
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity request = new HttpEntity(headers);

        // baca izuzetak ako nije ispravak token
        ResponseEntity<String> response = restTemplate.exchange(String.format(URL, id), HttpMethod.GET, request, String.class);
        String result = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, Preduzece.class);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search, @RequestHeader(name="Authorization") String token) throws IOException {
        Specification<Konverzija> spec = this.searchUtil.getSpec(search);
        Page<KonverzijaResponse> result = iKonverzijaService.findAll(spec);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFaktura(@Valid @RequestBody Konverzija konverzija, @RequestHeader(name="Authorization") String token) throws IOException {
        if(getPreduzeceById(konverzija.getDobavljacId(), token)== null){
            throw new PersistenceException(String.format("Ne postoji preduzece sa id-jem %s",konverzija.getDobavljacId()));
        }
        return ResponseEntity.ok(iKonverzijaService.save(konverzija));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        Optional<Konverzija> optionalKonverzija = iKonverzijaService.findById(id);
        if (optionalKonverzija.isPresent()){
            iKonverzijaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
}

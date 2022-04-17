package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.services.impl.PlataService;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class PlataRestController {

    private final PlataService plataService;
    private final SearchUtil<Plata> searchUtil;

    public PlataRestController(PlataService plataService) {
        this.plataService = plataService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(value = "/zaposleni/{id}/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataForZaposleni(@PathVariable("id") Long zaposleniId) {
        return ResponseEntity.ok(this.plataService.findByZaposleniZaposleniId(zaposleniId));
    }

    @GetMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlata(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        Specification<Plata> spec = this.searchUtil.getSpec(search);
        return ResponseEntity.ok(this.plataService.findAll(spec));
    }

    @GetMapping(value = "/plata/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPlata() {
        return ResponseEntity.ok(this.plataService.findAll());
    }

    @GetMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataById(@PathVariable("id") Long plataId) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent())
            return ResponseEntity.ok(optionalPlata.get());
        throw new EntityNotFoundException();
    }

    @PostMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newPlata(@Valid @RequestBody PlataRequest plata) {
        try {
            return ResponseEntity.ok(this.plataService.save(plata));
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    @PutMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPlata(@Valid @RequestBody PlataRequest plata) {
        Optional<Plata> optionalPlata = this.plataService.findById(plata.getPlataId());
        if (optionalPlata.isPresent()) {
            try {
                return ResponseEntity.ok(this.plataService.save(plata));
            } catch (Exception e) {
                throw new EntityNotFoundException();
            }
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePlata(@PathVariable("id") Long plataId) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent()) {
            this.plataService.deleteById(plataId);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException();
    }
}

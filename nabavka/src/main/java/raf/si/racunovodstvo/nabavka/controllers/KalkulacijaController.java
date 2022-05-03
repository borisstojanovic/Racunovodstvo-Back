package raf.si.racunovodstvo.nabavka.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.services.IKalkulacijaService;
import raf.si.racunovodstvo.nabavka.services.impl.KalkulacijaService;
import raf.si.racunovodstvo.nabavka.utils.ApiUtil;
import raf.si.racunovodstvo.nabavka.utils.SearchUtil;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/kalkulacije")
public class KalkulacijaController {

    private final IKalkulacijaService kalkulacijaService;

    private final SearchUtil<Kalkulacija> searchUtil;

    public KalkulacijaController(KalkulacijaService kalkulacijaService) {
        this.kalkulacijaService = kalkulacijaService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchKalkulacija(
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
        @RequestParam(defaultValue = "brojKalkulacije") String[] sort
    ) {
        if (search.isEmpty()) {
            return ResponseEntity.ok(this.kalkulacijaService.findAll());
        }

        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        Specification<Kalkulacija> spec = this.searchUtil.getSpec(search);
        return ResponseEntity.ok(this.kalkulacijaService.findAll(spec, pageSort));
    }
}

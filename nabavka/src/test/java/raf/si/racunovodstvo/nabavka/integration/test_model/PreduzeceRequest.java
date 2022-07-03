package raf.si.racunovodstvo.nabavka.integration.test_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreduzeceRequest {

    private Long preduzeceId;
    private String naziv;
    private String pib;
    private String racun;
    private String adresa;
    private String grad;
    private String telefon;
    private String email;
    private String fax;
    private String webAdresa;
    private String komentar;
    private Boolean isActive;
}


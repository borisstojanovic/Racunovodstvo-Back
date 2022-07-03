package raf.si.racunovodstvo.preduzece.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObracunZaradeConfigResponse {

    private int dayOfMonth;
    private SifraTransakcijeResponse SifraTransakcije;
}


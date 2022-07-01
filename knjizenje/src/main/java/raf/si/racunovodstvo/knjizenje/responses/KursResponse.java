package raf.si.racunovodstvo.knjizenje.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KursResponse {

    private Double kup;
    private Double sre;
    private Double pro;
}

package raf.si.racunovodstvo.knjizenje.responses;

import lombok.Data;

import java.util.Map;

@Data
public class KursnaListaResponse {

    private Map<String, Object> result;
    private Integer code;
    private String status;
}

package rs.raf.demo.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class BilansDateRequest {
    private Date datumOd;
    private Date datumDo;
}

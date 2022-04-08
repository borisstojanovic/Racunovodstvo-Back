package rs.raf.demo.requests;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.model.KontnaGrupa;

import java.util.Date;

@Getter
@Setter
public class BilansRequest {
    private KontnaGrupa kontoDo;
    private KontnaGrupa kontoOd;
    private Date datumOd;
    private Date datumDo;

    public BilansRequest( KontnaGrupa kontoOd, KontnaGrupa kontoDo, Date datumOd, Date datumDo) {
        this.kontoDo = kontoDo;
        this.kontoOd = kontoOd;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
    }
}

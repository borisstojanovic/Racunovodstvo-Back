package rs.raf.demo.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BilansResponse {

    private Double duguje;
    private Double potrazuje;
    private String brojKonta;
    private String nazivKonta;
    private Long brojStavki;
    private Double saldo;
    private Date datumKnjizenja;

    public BilansResponse(Double duguje, Double potrazuje, String brojKonta, String nazivKonta, Long brojStavki, Date datumKnjizenja) {
        this.duguje = duguje;
        this.potrazuje = potrazuje;
        this.brojKonta = brojKonta;
        this.nazivKonta = nazivKonta;
        this.brojStavki = brojStavki;
        this.datumKnjizenja = datumKnjizenja;
    }
    public BilansResponse(String brojKonta, String nazivKonta, Date datumKnjizenja){
        this.brojKonta = brojKonta;
        this.nazivKonta = nazivKonta;
        this.potrazuje = 0.0;
        this.duguje = 0.0;
        this.brojStavki = 0L;
        this.datumKnjizenja = datumKnjizenja;
    }
    public BilansResponse(String brojKonta, String nazivKonta, Date datumKnjizenja, Double duguje, Double potrazuje){
        this.brojKonta = brojKonta;
        this.nazivKonta = nazivKonta;
        this.potrazuje = potrazuje;
        this.duguje = duguje;
        this.brojStavki = 0L;
        this.datumKnjizenja = datumKnjizenja;
    }
    public BilansResponse(Double duguje, Double potrazuje, String brojKonta, String nazivKonta) {
        this.duguje = duguje;
        this.potrazuje = potrazuje;
        this.brojKonta = brojKonta;
        this.nazivKonta = nazivKonta;
        this.brojStavki = 1L;
        this.saldo = duguje - potrazuje;
    }

    @Override
    public String toString() {
        return "BilansResponse{" +
                "duguje=" + duguje +
                ", potrazuje=" + potrazuje +
                ", brojKonta='" + brojKonta + '\'' +
                ", nazivKonta='" + nazivKonta + '\'' +
                ", brojStavki=" + brojStavki +
                ", saldo=" + saldo +
                '}';
    }
}

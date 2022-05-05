package raf.si.racunovodstvo.nabavka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class KonverzijaArtikal extends Artikal {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Konverzija")
    private Konverzija konverzija;
}

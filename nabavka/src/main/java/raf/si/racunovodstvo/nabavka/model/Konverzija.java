package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "konverzija")
@Getter
@Setter
public class Konverzija extends BaznaKonverzijaKalkulacija {

    @Column(nullable = false, unique = true)
    private String brojKonverzije;
    @OneToMany(mappedBy = "baznaKonverzijaKalkulacija", cascade = CascadeType.ALL)
    private List<KonverzijaArtikal> artikli;
}

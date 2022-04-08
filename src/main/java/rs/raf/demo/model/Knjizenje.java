package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Knjizenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long knjizenjeId;
    @Column(nullable = false)
    private Date datumKnjizenja;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dokument")
    private Dokument dokument;
    @JsonIgnore
    @OneToMany(mappedBy = "knjizenje",cascade = CascadeType.ALL)
    private List<Konto> konto;
}

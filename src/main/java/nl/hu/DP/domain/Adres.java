package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "adres")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Adres {

    @Id
    @Column(name = "adres_id")
    @EqualsAndHashCode.Include
    private Long adresId;

    @Column(name = "postcode", nullable = false)
    private String postcode;

    @Column(name = "huisnummer", nullable = false)
    private String huisnummer;

    @Column(name = "straat", nullable = false)
    private String straat;

    @Column(name = "woonplaats", nullable = false)
    private String woonplaats;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reiziger_id", nullable = false, unique = true)
    private Reiziger reiziger;

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
        if (reiziger.getAdres() != this) {
            reiziger.setAdres(this);
        }
    }
}
package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reiziger")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reiziger {

    @Id
    @Column(name = "reiziger_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "voorletters", nullable = false)
    private String voorletters;

    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column(name = "achternaam", nullable = false)
    private String achternaam;

    @Column(name = "geboortedatum")
    @Temporal(TemporalType.DATE)
    private Date geboortedatum;

    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Adres adres;

    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    public void setAdres(Adres adres) {
        this.adres = adres;
        if (adres.getReiziger() != this) {
            adres.setReiziger(this);
        }
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaarten.add(ovChipkaart);
        ovChipkaart.setReiziger(this);
    }
}
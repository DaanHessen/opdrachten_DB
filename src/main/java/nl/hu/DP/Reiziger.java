package nl.hu.DP;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Reiziger {
    @Id
    private Long reiziger_id;

    @Column(name = "voorletters")
    private String voorletters;

    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column(name = "achternaam")
    private String achternaam;

    @Column(name = "geboortedatum")
    private Date geboortedatum;

    @OneToOne(mappedBy = "reiziger")
    private Adres adres;

    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL)
    private List<OVChipkaart> ovChipkaarten;

    public Reiziger() {}

    // Constructor
    public Reiziger(String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    // Getters en Setters
    public Long getId() { return reiziger_id; }
    public void setId(Long reiziger_id) { this.reiziger_id = reiziger_id; }

    public Adres getAdres() { return adres; }

    public void setOVChipkaarten(List<OVChipkaart> ovChipkaarten) { this.ovChipkaarten = ovChipkaarten; }

    public void setNaam(String naam) { this.setNaam(naam); }
}
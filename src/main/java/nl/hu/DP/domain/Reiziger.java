package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"reiziger_id"})
public class Reiziger {
    private Long reiziger_id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovChipkaarten;

    public Reiziger() { this.ovChipkaarten = new ArrayList<>(); }

    public Long getId() { return reiziger_id; }
    public void setId(Long reiziger_id) { this.reiziger_id = reiziger_id; }

    public Reiziger(String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.ovChipkaarten = new ArrayList<>();
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.add(ovChipkaart);
            ovChipkaart.setReiziger(this);
        }
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.remove(ovChipkaart);
            ovChipkaart.setReiziger(null);
        }
    }
}

package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(of = {"productNummer"})
public class Product {

    private Long productNummer;
    private String naam;
    private String beschrijving;
    private Long prijs;
    private List<OVChipkaart> ovChipkaarten;

    public Product() {
        this.ovChipkaarten = new ArrayList<>();
    }

    public Product(Long productNummer, String naam, String beschrijving, Long prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaarten = new ArrayList<>();
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.add(ovChipkaart);
            ovChipkaart.addProduct(this);
        }
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.remove(ovChipkaart);
            ovChipkaart.removeProduct(this);
        }
    }
}
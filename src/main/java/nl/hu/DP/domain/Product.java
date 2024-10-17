package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @Column(name = "product_nummer")
    @EqualsAndHashCode.Include
    private Long productNummer;

    @Column(name = "naam", nullable = false)
    private String naam;

    @Column(name = "beschrijving", nullable = false)
    private String beschrijving;

    @Column(name = "prijs", nullable = false)
    private Long prijs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "product_nummer"),
            inverseJoinColumns = @JoinColumn(name = "kaart_nummer")
    )
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.add(ovChipkaart);
            ovChipkaart.getProducten().add(this);
        }
    }
}
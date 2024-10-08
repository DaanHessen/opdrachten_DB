package nl.hu.DP;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OVChipkaart {
    @Id
    private Long kaart_nummer;

    @Column(name = "geldig_tot")
    private String geldig_tot;

    @Column(name = "klasse")
    private Long klasse;

    @Column(name = "saldo")
    private Long saldo;

    @ManyToOne
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    @ManyToMany(mappedBy = "ovChipkaarten")
    private List<Product> producten;

    public OVChipkaart() {
    }

    public OVChipkaart(String geldig_tot, Long klasse, Long saldo, Reiziger reiziger) {
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.producten = new ArrayList<>();
    }

    public Long getKaartnummer() {
        return kaart_nummer;
    }

    public void setKaartnummer(Long kaartnummer) {
        this.kaart_nummer = kaartnummer;
    }

    public String getGeldigTot() {
        return geldig_tot;
    }

    public void setGeldigTot(String geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public void setKlasse(Long klasse) {
        this.klasse = klasse;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public void addProduct(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
            product.addOVChipkaarts(this);
        }
    }

    public void removeProduct(Product product) {
        if (producten.contains(product)) {
            producten.remove(product);
            product.removeOVChipkaarts(this);
        }
    }
}

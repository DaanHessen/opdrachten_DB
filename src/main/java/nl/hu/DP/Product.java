package nl.hu.DP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Long getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(Long productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Long getPrijs() {
        return prijs;
    }

    public void setPrijs(Long prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return Objects.equals(productNummer, product.productNummer);
    }

    @Override
    public int hashCode() {
        return productNummer != null ? productNummer.hashCode() : 0;
    }
}
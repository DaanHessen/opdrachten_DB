package nl.hu.DP;

public class Product {
    private Long productNummer;
    private String naam;
    private String beschrijving;
    private Long prijs;
    private OVChipkaart ovChipkaart; // Single reference

    // Constructors
    public Product() {}

    public Product(Long productNummer, String naam, String beschrijving, Long prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    // Getters and Setters
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

    public OVChipkaart getOvChipkaarts() { return ovChipkaart; }

    public void setOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaart = ovChipkaart;
    }

    public void setOvChipkaarts(OVChipkaart ovChipkaart) {
    }

    public void addOVChipkaarts(OVChipkaart ovChipkaart) {
    }

    public void removeOVChipkaarts(OVChipkaart ovChipkaart) {
    }
}

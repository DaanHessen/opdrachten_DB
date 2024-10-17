package nl.hu.DP;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private long kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private long reizigerId;
    private Reiziger reiziger; // Assuming a Reiziger class exists
    private List<Product> producten = new ArrayList<>(); // Assuming a Product class exists

    public OVChipkaart() {}

    public OVChipkaart(long kaartnummer, Date geldigTot, int klasse, double saldo, long reizigerId) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerId = reizigerId;
    }

    public OVChipkaart(long kaartnummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this(kaartnummer, geldigTot, klasse, saldo, reiziger.getId());
        this.reiziger = reiziger;
    }

    public long getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(long kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public long getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(long reizigerId) {
        this.reizigerId = reizigerId;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
        if (reiziger != null) {
            this.reizigerId = reiziger.getId();
        }
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public void removeProduct(Product product) {
        this.producten.remove(product);
    }

    public void addProduct(Product product) {
        this.producten.add(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OVChipkaart that = (OVChipkaart) o;

        return kaartnummer == that.kaartnummer;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(kaartnummer);
    }
}

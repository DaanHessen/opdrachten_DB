package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"kaartnummer"})
public class OVChipkaart {
    private long kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private long reizigerId;
    private Reiziger reiziger;
    private List<Product> producten = new ArrayList<>();

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

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
        if (reiziger != null) {
            this.reizigerId = reiziger.getId();
        }
    }

    public void addProduct(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
            product.addOVChipkaart(this);
        }
    }

    public void removeProduct(Product product) {
        if (producten.contains(product)) {
            producten.remove(product);
            product.removeOVChipkaart(this);
        }
    }
}

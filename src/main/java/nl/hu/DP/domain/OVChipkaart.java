package nl.hu.DP.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OVChipkaart {

    @Id
    @Column(name = "kaart_nummer")
    @EqualsAndHashCode.Include
    private Long kaartNummer;

    @Column(name = "geldig_tot", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date geldigTot;

    @Column(name = "klasse", nullable = false)
    private Integer klasse;

    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;

    @ManyToMany(mappedBy = "ovChipkaarten", fetch = FetchType.LAZY)
    private List<Product> producten = new ArrayList<>();

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
        if (!reiziger.getOvChipkaarten().contains(this)) {
            reiziger.getOvChipkaarten().add(this);
        }
    }
}
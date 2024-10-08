package nl.hu.DP;

import javax.persistence.*;

@Entity
public class Adres {
    @Id
    private Long adres_id;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "huisnummer")
    private String huisnummer;

    @Column(name = "straat")
    private String straat;

    @Column(name = "woonplaats")
    private String woonplaats;

    @OneToOne
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    public Adres() {}

    // Constructor
    public Adres(String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    // Getters en Setters
    public Long getId() { return adres_id; }
    public void setId(Long adres_id) { this.adres_id = adres_id; }
    public String getPostcode() { return postcode; }
    public Reiziger getReiziger() { return reiziger; }
}
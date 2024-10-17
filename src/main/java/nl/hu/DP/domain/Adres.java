package nl.hu.DP.domain;

import lombok.Data;

@Data
public class Adres {

    private Long adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Reiziger reiziger;

    public Adres() {}

    public Adres(String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public Long getId() { return adres_id; }
    public void setId(Long adres_id) { this.adres_id = adres_id; }
}
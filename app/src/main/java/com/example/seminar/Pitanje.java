package com.example.seminar;

import java.util.Map;

public class Pitanje {
    private String pitanje;
    private Map<String, String> odgovori; // Mapa za spremanje odgovora (ključ: odgovor)

    private String tocanOdgovor;

    public Pitanje() {

    }

    public Pitanje(String pitanje, Map<String, String> odgovori, String tocanOdgovor) {
        this.pitanje = pitanje;
        this.odgovori = odgovori;
        this.tocanOdgovor = tocanOdgovor;
    }

    public String getPitanje() {
        return pitanje;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public Map<String, String> getOdgovori() {
        return odgovori;
    }

    public void setOdgovori(Map<String, String> odgovori) {
        this.odgovori = odgovori;
    }

    public String getTocanOdgovor() {
        return tocanOdgovor;
    }

    public void setTocanOdgovor(String tocanOdgovor) {
        this.tocanOdgovor = tocanOdgovor;
    }
}

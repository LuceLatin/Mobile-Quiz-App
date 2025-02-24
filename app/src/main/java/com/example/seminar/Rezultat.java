package com.example.seminar;

public class Rezultat {
    private int brojTocnihOdgovora;
    private int ukupnoPitanja;
    private int postotakTocnih;
    private long preostaloVrijeme;
    private long ukupnoVrijeme;

    private int brojOdigranihIgara;

    private String email;

    public String getName() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rezultat() {

    }

    public int getScore() {
        return (int) ((float) brojTocnihOdgovora / ukupnoPitanja * 100);
    }

    public Rezultat(int brojTocnihOdgovora, int ukupnoPitanja, int postotakTocnih, long preostaloVrijeme, long ukupnoVrijeme) {
        this.brojTocnihOdgovora = brojTocnihOdgovora;
        this.ukupnoPitanja = ukupnoPitanja;
        this.postotakTocnih = postotakTocnih;
        this.preostaloVrijeme = preostaloVrijeme;
        this.ukupnoVrijeme = ukupnoVrijeme;
    }

    public int getBrojTocnihOdgovora() {
        return brojTocnihOdgovora;
    }

    public int getUkupnoPitanja() {
        return ukupnoPitanja;
    }

    public int getPostotakTocnih() {
        return postotakTocnih;
    }

    public long getPreostaloVrijeme() {
        return preostaloVrijeme;
    }

    public long getUkupnoVrijeme() {
        return ukupnoVrijeme;
    }

    public int getBrojOdigranihIgara() {
        return brojOdigranihIgara;
    }

    public void setBrojOdigranihIgara(int brojOdigranihIgara) {
        this.brojOdigranihIgara = brojOdigranihIgara;
    }
}

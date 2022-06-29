package com.example.urkundendrucker;

public class Teilnehmer {

    private String vollerName;
    private Turnier turnier;
    private double laufzeit = - 1;
    private int platzierung = -1;

    @Override
    public String toString() {
        return vollerName + " Trat an in Turnier " + turnier.getTurnierName() + " mit einer Zeit von " + laufzeit;
    }

    public String getVollerName() {
        return vollerName;
    }

    public void setVollerName(String vollerName) {
        this.vollerName = vollerName;
    }

    public Turnier getTurnier() {
        return turnier;
    }

    public void setTurnier(Turnier turnier) {
        this.turnier.getTeilnehmerListe().remove(this);
        this.turnier = turnier;
        this.turnier.getTeilnehmerListe().add(this);
    }

    public double getLaufzeit() {
        return laufzeit;
    }

    public void setLaufzeit(double laufzeit) {
        this.laufzeit = laufzeit;
    }

    public Teilnehmer(String vollerName, Turnier turnier) {
        this(vollerName, turnier, -1);
    }

    public Teilnehmer(String vollerName, Turnier turnier, double laufzeit) {

        this.vollerName = vollerName;
        this.turnier = turnier;
        this.laufzeit = laufzeit;
        this.turnier.getTeilnehmerListe().add(this);
    }

    public int getPlatzierung() {
        return platzierung;
    }

    public void setPlatzierung(int platzierung) {
        this.platzierung = platzierung;
    }

}

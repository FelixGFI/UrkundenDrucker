package com.example.urkundendrucker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Turnier {

    private String turnierName;
    private String sportart;
    private LocalDate datum;
    private ArrayList<Teilnehmer> teilnehmerListe;

    public String getTurnierName() {
        return turnierName;
    }

    public String getSportart() {
        return sportart;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public ArrayList<Teilnehmer> getTeilnehmerListe() {
        return teilnehmerListe;
    }

    public void setTurnierName(String turnierName) {
        this.turnierName = turnierName;
    }

    public void setSportart(String sportart) {
        this.sportart = sportart;
    }

    @Override
    public String toString() {
        //TODO set German Date Format
        return  turnierName + " in Sportart " + sportart + " am " + datum;
    }

    public Turnier(String turnierName, String sportart, LocalDate datum) {
        this(turnierName, sportart, datum, new ArrayList<>());
    }

    public Turnier(String turnierName, String sportart, LocalDate datum, ArrayList<Teilnehmer> teilnehmerListe) {
        this.turnierName = turnierName;
        this.sportart = sportart;
        this.datum = datum;
        this.teilnehmerListe = teilnehmerListe;
    }

    public void berechnePlatzierung() {



        if(!teilnehmerListe.isEmpty()) {
            int currentPlatzierung = 1;
            ArrayList<Teilnehmer> nochZuPruefendeTeilnehmer = new ArrayList<>();
            nochZuPruefendeTeilnehmer.addAll(teilnehmerListe);
            ArrayList<Teilnehmer> abgearbeiteteTeilnehmer = new ArrayList<>();
            ArrayList<Teilnehmer> teilnehmerWithSamePlatzierung = new ArrayList<>();
            Teilnehmer currenTeilnehmerWithBestTime = nochZuPruefendeTeilnehmer.get(0);
            Teilnehmer currentTeilnehmerWithSecondBestTime = null;
            boolean thereAreTeilnehmerStillWithoutPlatzierung = true;



            while(thereAreTeilnehmerStillWithoutPlatzierung) {
                teilnehmerWithSamePlatzierung.add(currenTeilnehmerWithBestTime);
                for (Teilnehmer teilnehmerToCheck : nochZuPruefendeTeilnehmer) {
                    if(!abgearbeiteteTeilnehmer.contains(teilnehmerToCheck) && teilnehmerToCheck != currenTeilnehmerWithBestTime) {
                        if(teilnehmerToCheck.getLaufzeit() < currenTeilnehmerWithBestTime.getLaufzeit()) {
                            currentTeilnehmerWithSecondBestTime = currenTeilnehmerWithBestTime;
                            currenTeilnehmerWithBestTime = teilnehmerToCheck;
                            teilnehmerWithSamePlatzierung = new ArrayList<>();
                            teilnehmerWithSamePlatzierung.add(teilnehmerToCheck);
                        } else if (teilnehmerToCheck.getLaufzeit() == currenTeilnehmerWithBestTime.getLaufzeit()) {
                            teilnehmerWithSamePlatzierung.add(teilnehmerToCheck);
                        }
                    }
                }
                for (Teilnehmer teilnehmerWithCurrentPlatzierung : teilnehmerWithSamePlatzierung) {
                    teilnehmerWithCurrentPlatzierung.setPlatzierung(currentPlatzierung);
                    abgearbeiteteTeilnehmer.add(teilnehmerWithCurrentPlatzierung);
                    nochZuPruefendeTeilnehmer.remove(teilnehmerWithCurrentPlatzierung);
                }

                currentPlatzierung = currentPlatzierung + teilnehmerWithSamePlatzierung.size();
                teilnehmerWithSamePlatzierung = new ArrayList<>();

                if(nochZuPruefendeTeilnehmer.isEmpty()) {
                    thereAreTeilnehmerStillWithoutPlatzierung = false;
                } else if(nochZuPruefendeTeilnehmer.size() == 1) {
                    nochZuPruefendeTeilnehmer.get(0).setPlatzierung(currentPlatzierung);
                    thereAreTeilnehmerStillWithoutPlatzierung = false;
                } else {
                    currenTeilnehmerWithBestTime = nochZuPruefendeTeilnehmer.get(0);
                }





            }
        }
    }
}


package com.gritta.doctorsdiary;

/**
 * Created by Gritta on 12.03.2018.
 */

public class Notiz{
    private String datum;
    private int wohlbefinden;
    private double gewicht, schlaf;
    private int t,ca,fe,b, tage;
    private String zusatz;
    private long id;
    private int datum_int;


    public Notiz(long id, String datum, double gewicht, int wohlbefinden, double schlaf, int tage, int t, int ca, int fe, int b, String zusatz){
        this.datum = datum;
        this.gewicht = gewicht;
        this.wohlbefinden = wohlbefinden;
        this.schlaf = schlaf;
        this.tage = tage;
        this.t = t;
        this.ca = ca;
        this.b = b;
        this.fe = fe;
        this.zusatz = zusatz;
        this.id = id;
        this.datum_int = datToInt(datum);

    }

    @Override
    public String toString(){
        return datum;
    }

    public int datToInt(String d) {
        String dat1 = d;
        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "", datumString = "";
        int datumInt = 0;

        int i = 0;
        while (dat1.charAt(i) != '.') {
            d1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (d1.length() < 2) d1 = "0" + d1;
        while (dat1.charAt(i) != '.') {
            m1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (m1.length() < 2) m1 = "0" + m1;
        while (i < dat1.length()) {
            y1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (y1.length() < 4) y1 = "20" + y1;
        datumString = y1 + m1 + d1;
        datumInt = Integer.parseInt(datumString);
        return datumInt;
    }

    public double getSchlaf() {
        return schlaf;
    }

    public void setSchlaf(double schlaf) {
        this.schlaf = schlaf;
    }

    public int getTage() {
        return tage;
    }

    public void setTage(int tage) {
        this.tage = tage;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    public int getFe() {
        return fe;
    }

    public void setFe(int fe) {
        this.fe = fe;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getDatum_int() {
        return datum_int;
    }

    public void setDatum_intg(int datum_string) {
        this.datum_int = datum_string;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public int getWohlbefinden() {
        return wohlbefinden;
    }

    public void setWohlbefinden(int wohlbefinden) {
        this.wohlbefinden = wohlbefinden;
    }

    public String getZusatz() {
        return zusatz;
    }

    public void setZusatz(String zusatz) {
        this.zusatz = zusatz;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

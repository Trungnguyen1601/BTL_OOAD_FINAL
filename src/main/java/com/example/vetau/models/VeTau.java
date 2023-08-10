package com.example.vetau.models;


import com.example.vetau.models.Chuyen_tau;

public class VeTau {
    private String idVetau;
    private Chuyen_tau chuyenTau;
    private String idToatau;
    private int vitringoi;
    private int giave;
    private String tinhtrangve;
    private String loaive;

    public VeTau() {

    }

    public VeTau(String idVetau, Chuyen_tau idChuyentau, String idToatau, int vitringoi, int giave, String tinhtrangve, String loaive) {
        this.idVetau = idVetau;
        this.chuyenTau = idChuyentau;
        this.idToatau = idToatau;
        this.vitringoi = vitringoi;
        this.giave = giave;
        this.tinhtrangve = tinhtrangve;
        this.loaive = loaive;
    }

    public String getIdVetau() {
        return idVetau;
    }

    public void setIdVetau(String idVetau) {
        this.idVetau = idVetau;
    }

    public Chuyen_tau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(Chuyen_tau chuyenTau) {
        this.chuyenTau = chuyenTau;
    }

    public String getIdToatau() {
        return idToatau;
    }

    public void setIdToatau(String idToatau) {
        this.idToatau = idToatau;
    }

    public int getVitringoi() {
        return vitringoi;
    }

    public void setVitringoi(int vitringoi) {
        this.vitringoi = vitringoi;
    }

    public int getGiave() {
        return giave;
    }

    public void setGiave(int giave) {
        this.giave = giave;
    }

    public String getTinhtrangve() {
        return tinhtrangve;
    }

    public void setTinhtrangve(String tinhtrangve) {
        this.tinhtrangve = tinhtrangve;
    }

    public String getLoaive() {
        return loaive;
    }

    public void setLoaive(String loaive) {
        this.loaive = loaive;
    }

    public String getID_Chuyentau()
    {
        return this.chuyenTau.getID_train();
    }
}

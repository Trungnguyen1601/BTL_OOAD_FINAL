package com.example.vetau.models;

import javafx.scene.control.Button;

import java.util.Date;

public class Chuyen_tau {
    private String ID_train;
    private Ga_tau Gadi;
    private Ga_tau Gaden;
    private Tau tau;
    private String Giodi;

    private Date ngaydi;
    private Date ngayden;

    public Chuyen_tau()
    {

    }
    public Chuyen_tau(String ID_train, Ga_tau gadi, Ga_tau gaden, Tau tau_1, String giodi, Date ngaydi, Date ngayden) {
        this.ID_train = ID_train;
        Gadi = gadi;
        Gaden = gaden;
        tau = tau_1;
        Giodi = giodi;
        this.ngaydi = ngaydi;
        this.ngayden = ngayden;
    }

    public Chuyen_tau(String ID_train, Ga_tau gadi, Ga_tau gaden, Tau tau, String giodi) {
        this.ID_train = ID_train;
        Gadi = gadi;
        Gaden = gaden;
        tau = tau;
        Giodi = giodi;

    }

    public String getID_train() {
        return ID_train;
    }

    public void setID_train(String ID_train) {
        this.ID_train = ID_train;
    }

    public Ga_tau getGadi() {
        return Gadi;
    }

    public void setGadi(Ga_tau gadi) {
        Gadi = gadi;
    }

    public Ga_tau getGaden() {
        return Gaden;
    }

    public void setGaden(Ga_tau gaden) {
        Gaden = gaden;
    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau) {
        this.tau = tau;
    }

    public String getGiodi() {
        return Giodi;
    }

    public void setGiodi(String giodi) {
        Giodi = giodi;
    }

    public Date getNgaydi() {
        return ngaydi;
    }

    public void setNgaydi(Date ngaydi) {
        this.ngaydi = ngaydi;
    }

    public Date getNgayden() {
        return ngayden;
    }

    public void setNgayden(Date ngayden) {
        this.ngayden = ngayden;
    }
    public String getTenGaDi()
    {
        return this.Gadi.getTen_Gatau();
    }
    public String getTenGaDen()
    {
        return this.Gaden.getTen_Gatau();
    }

    public String getIDTau()
    {
        if (tau != null) {
            return tau.getIDTau();
        }
        return "TAU1";
    }

}

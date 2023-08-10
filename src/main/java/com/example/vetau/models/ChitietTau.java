package com.example.vetau.models;

import com.example.vetau.helpers.Check_Status;
import javafx.scene.control.Button;

public class ChitietTau {
    private Tau tau;
    private String ID_toa;
    private int Soluongghe;
    private String Loaitoa;
    private boolean Trangthai;

    public boolean isTrangthai() {
        return Trangthai;
    }
    public String isTrangthaiToa_String() {
        return Check_Status.ReturnString_Check(Trangthai);
    }
    public String isTrangthaiTau_String()
    {
        return Check_Status.ReturnString_Check(tau.isTrangthai());
    }

    public void setTrangthai(boolean trangthai) {
        Trangthai = trangthai;
    }

    public ChitietTau(Tau tau, String ID_toa, int soluongghe, String loaitoa, boolean trangthai) {
        this.tau = tau;
        this.ID_toa = ID_toa;
        Soluongghe = soluongghe;
        Loaitoa = loaitoa;
        Trangthai = trangthai;
    }

    public ChitietTau()
    {

    }

    public ChitietTau(Tau tau, String ID_toa, int soluongghe, String loaitoa) {
        this.tau = tau;
        this.ID_toa = ID_toa;
        Soluongghe = soluongghe;
        Loaitoa = loaitoa;

    }
    public ChitietTau(Tau tau1,String ID_toa)
    {
        this.tau = tau1;
        this.ID_toa = ID_toa;

    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau1) {
        this.tau = tau1;
    }

    public String getID_toa() {
        return ID_toa;
    }

    public void setID_toa(String ID_toa) {
        this.ID_toa = ID_toa;
    }

    public int getSoluongghe() {
        return Soluongghe;
    }

    public String getSLghe(){
        return String.valueOf(Soluongghe);
    }

    public void setSoluongghe(int soluongghe) {
        Soluongghe = soluongghe;
    }

    public String getLoaitoa() {
        return Loaitoa;
    }

    public void setLoaitoa(String loaitoa) {
        Loaitoa = loaitoa;
    }
    public String getID_Tau()
    {
        return tau.getIDTau();
    }
    public int getSoluongtoa()
    {
        return tau.getSoluongtoa();
    }
    public void setID_Tau(String ID_tau)
    {
        tau.setIDTau(ID_tau);
    }
    public void setSoluongtoa(int SLtoa)
    {
        tau.setSoluongtoa(SLtoa);
    }


}

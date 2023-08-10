package com.example.vetau.models;

import java.util.Date;

public class Chitiet_VeTau {
    private VeTau Vetau;
    private Passenger KhachHang;
    private Date ngaydatve;
    private int giamuave;

    public Chitiet_VeTau() {

    }

    public Chitiet_VeTau(VeTau id_vetau, Passenger id_khachhang, Date ngaydatve, int giamuave) {
        this.Vetau = id_vetau;
        this.KhachHang = id_khachhang;
        this.ngaydatve = ngaydatve;
        this.giamuave = giamuave;
    }

    public VeTau getVetau() {
        return Vetau;
    }

    public void setVetau(VeTau vetau) {
        Vetau = vetau;
    }

    public Passenger getKhachHang() {
        return KhachHang;
    }

    public void setKhachHang(Passenger khachHang) {
        KhachHang = khachHang;
    }

    public Date getNgaydatve() {
        return ngaydatve;
    }

    public void setNgaydatve(Date ngaydatve) {
        this.ngaydatve = ngaydatve;
    }

    public int getGiamuave() {
        return giamuave;
    }

    public void setGiamuave(int giamuave) {
        this.giamuave = giamuave;
    }

    public String getID_VeTau()
    {
        return this.Vetau.getIdVetau();
    }

}

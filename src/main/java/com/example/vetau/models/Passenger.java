package com.example.vetau.models;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class Passenger {
    private String ID_Passenger;
    private String ho_va_Ten;
    private String cccd;
    private String gender;
    private Date date;
    private String address;
    private String phone;
    private String email;
    private int Level;
    private Account account;

    public Passenger(String ID_Passenger, String ho_va_Ten, String phone,Account account, String email) {
        this.ID_Passenger = ID_Passenger;
        this.ho_va_Ten = ho_va_Ten;
        this.phone = phone;
        this.email = email;
        this.account = account;
    }

    public Passenger(String ID_Passenger, String ho_va_Ten, String cccd, String gender, Date date, String address, String phone, String email, int level, Account account) {
        this.ID_Passenger = ID_Passenger;
        this.ho_va_Ten = ho_va_Ten;
        this.cccd = cccd;
        this.gender = gender;
        this.date = date;
        this.address = address;
        this.phone = phone;
        this.email = email;
        Level = level;
        this.account = account;
    }

    public Passenger(String ID_Passenger, String ho_va_Ten, String cccd, String gender, Date date, String address, String phone, String email, int level) {
        this.ID_Passenger = ID_Passenger;
        this.ho_va_Ten = ho_va_Ten;
        this.cccd = cccd;
        this.gender = gender;
        this.date = date;
        this.address = address;
        this.phone = phone;
        this.email = email;
        Level = level;

    }

    public String getID_Passenger() {
        return ID_Passenger;
    }

    public void setID_Passenger(String ID_Passenger) {
        this.ID_Passenger = ID_Passenger;
    }

    public String getHo_va_Ten() {
        return ho_va_Ten;
    }

    public void setHo_va_Ten(String ho_va_Ten) {
        this.ho_va_Ten = ho_va_Ten;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDate() {
        return date;
    }
    public LocalDate getLocalDate() {

        return date.toLocalDate();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }
    public String getUsername() {
        return account.getUsername();
    }
    public String getPassword() {
        return account.getPassword();
    }

}

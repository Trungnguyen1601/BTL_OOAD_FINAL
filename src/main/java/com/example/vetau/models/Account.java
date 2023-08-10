package com.example.vetau.models;

public class Account {
    private String ID_Account;
    private String Username;
    private String Password;
    private String Role;

    public Account() {

    }

    public Account(String ID_Account, String username, String password, String role) {
        this.ID_Account = ID_Account;
        Username = username;
        Password = password;
        Role = role;
    }

    public Account(String ID_Account, String username, String password) {
        this.ID_Account = ID_Account;
        Username = username;
        Password = password;
    }

    public String getID_Account() {
        return ID_Account;
    }

    public void setID_Account(String ID_Account) {
        this.ID_Account = ID_Account;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

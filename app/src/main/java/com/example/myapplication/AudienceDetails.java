package com.example.myapplication;

public class AudienceDetails {
    private String Name;
    private String Phone;
    public AudienceDetails(String Name, String Phone) {
        this.Name            = Name;
        this.Phone           = Phone;

    }
    public String getAudienceName() {
        return Name;
    }
    public String getPhone() {return Phone;}
}

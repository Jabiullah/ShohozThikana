package com.example.bashakoi;

public class User {
    private int servicer_id;
    private String servicer_phone, servicer_email, company_name, company_id;

    public User(int servicer_id, String servicer_phone, String servicer_email, String company_name, String company_id) {
        this.servicer_id = servicer_id;
        this.servicer_phone = servicer_phone;
        this.servicer_email = servicer_email;
        this.company_name = company_name;
        this.company_id = company_id;
    }

    public int getId() {
        return servicer_id;
    }

    public String getPhone() {
        return servicer_phone;
    }

    public String getEmail() {return servicer_email;}

    public String getCompany() {return company_name;}

    public String getCompanyId() {return company_id;}
}

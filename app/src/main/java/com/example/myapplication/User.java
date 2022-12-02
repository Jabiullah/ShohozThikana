package com.example.myapplication;

public class User {
    private int user_id;
    private String user_phone, user_email;

    public User(int user_id, String user_phone, String user_email) {
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.user_email = user_email;
    }

    public int getId() {
        return user_id;
    }

    public String getPhone() {
        return user_phone;
    }

    public String getEmail() {
        return user_email;
    }
}

package com.example.dell.expensemanager;

public class Personal_Detail_Navigation_Header {

    private String name, email, password, contact_no;

    public Personal_Detail_Navigation_Header(String name, String email, String password, String contact_no) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact_no = contact_no;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getContact_no() {
        return contact_no;
    }
}

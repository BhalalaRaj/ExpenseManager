package com.example.dell.expensemanager;

public class FirebaseData {
    String amount, category, detail, payment_method, time;
    int date, month, year;

    public FirebaseData(String ammount, String category, String detail, String payment_method, String s, String s1) {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDate() {

        return date;
    }


    public FirebaseData(String amount, String category, String detail, String payment_method , String time, int date,int month, int year) {
        this.amount = amount;
        this.category = category;
        this.detail = detail;
        this.payment_method = payment_method;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}

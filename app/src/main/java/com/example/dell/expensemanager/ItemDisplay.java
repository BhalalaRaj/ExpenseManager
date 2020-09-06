package com.example.dell.expensemanager;

public class ItemDisplay {
    private String title;
    private String price;
    private String date;

    public ItemDisplay(String title, String price, String date) {
        this.title = title;
        this.price = price;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.example.kimsujin.ssg_project;

public class RecommendItem {

    private String item_name;
    private String sales_price;

    public RecommendItem(String item_name, String sales_price) {
        this.item_name = item_name;
        this.sales_price = sales_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }
}

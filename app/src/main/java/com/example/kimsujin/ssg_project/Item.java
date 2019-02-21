package com.example.kimsujin.ssg_project;


public class Item {
    private String pum_id;
    private String pum_name;
    private String rate;

    public Item(String pum_id, String pum_name, String rate) {
        this.pum_id = pum_id;
        this.pum_name = pum_name;
        this.rate = rate;
    }

    public String getPum_id() {
        return pum_id;
    }

    public void setPum_id(String pum_id) {
        this.pum_id = pum_id;
    }

    public String getPum_name() {
        return pum_name;
    }

    public void setPum_name(String pum_name) {
        this.pum_name = pum_name;
    }

    public String getRate() { return rate; }

    public void setRate(String rate) { this.rate = rate; }

}

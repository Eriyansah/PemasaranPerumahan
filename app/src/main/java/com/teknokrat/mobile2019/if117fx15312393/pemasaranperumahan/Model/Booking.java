package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model;

public class Booking  {

    private String pid, pname, price, location;

    public Booking() {
    }

    public Booking(String pid, String pname, String price, String location) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.location = location;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

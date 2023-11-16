package com.example.capstone_seefood;

public class MenuModel {

    String name, price;
    int gambarmakanan;

    public MenuModel(String name, String price, int makanan)
    {
        this.name = name;
        this.price = price;
        this.gambarmakanan = makanan;
    }

    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }

    public int getMakanan()
    {
        return gambarmakanan;
    }
}

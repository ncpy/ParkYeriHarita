package com.example.parkyeriharita;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class MyItems implements Serializable {
    private String name;
    private String aktif_saat;
    private int fiyat, bos_sayisi;
    private LinkedHashMap<String[], LinkedHashMap<String, Integer>> all_info;

    public MyItems(String name, String aktif_saat, int fiyat, int bos_sayisi, LinkedHashMap<String[], LinkedHashMap<String, Integer>> all_info) {
        this.name = name;
        this.aktif_saat = aktif_saat;
        this.fiyat = fiyat;
        this.bos_sayisi = bos_sayisi;
        this.all_info = all_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAktif_saat() {
        return aktif_saat;
    }

    public void setAktif_saat(String aktif_saat) {
        this.aktif_saat = aktif_saat;
    }

    public int getBos_sayisi() {
        return bos_sayisi;
    }

    public void setBos_sayisi(int desc) {
        this.bos_sayisi = desc;
    }

    public int getFiyat() {
        return fiyat;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    public LinkedHashMap<String[], LinkedHashMap<String, Integer>> getAll_info() {
        return all_info;
    }

    public void setAll_info(LinkedHashMap<String[], LinkedHashMap<String, Integer>> all_info) {
        this.all_info = all_info;
    }

}

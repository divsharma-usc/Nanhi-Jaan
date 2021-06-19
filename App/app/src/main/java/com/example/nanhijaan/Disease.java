package com.example.nanhijaan;

public class Disease  {
    private String disease_name;
    private int thumbnail;

    public Disease(String disease_name, int thumbnail) {
        this.disease_name = disease_name;
        this.thumbnail = thumbnail;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}

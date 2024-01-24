package com.example.splash;

public class DataClass {
    private String dataTitle;
    private int dataImage;
    private int dataDesc;

    public String getDataTitle() {
        return dataTitle;
    }

    public int getDataImage() {
        return dataImage;
    }

    public int getDataDesc() {
        return dataDesc;
    }

    public DataClass(String dataTitle, int dataDesc, String dataLang, int dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;

    }
}


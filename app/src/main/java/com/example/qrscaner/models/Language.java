package com.example.qrscaner.models;

import java.util.ArrayList;
import java.util.List;

public class Language {
    private int id;
    private String country;
    private String lang;


    public Language(int id, String country, String lang) {
        this.id = id;
        this.country = country;
        this.lang = lang;
    }

    public Language() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static List<Language> languageList() {
        List<Language> languageList = new ArrayList<>();
        languageList.add(new Language(0, "Viet Nam", "vn"));
        languageList.add(new Language(1, "English", "en"));
//        languageList.add(new Language(2, "日本", "ja"));

        return languageList;
    }

    public static String[] listStrCountry() {
        List<Language> languageList = languageList();
        String[] strings = new String[languageList.size()];

        for (int i = 0; i < languageList.size(); i++) {
            strings[i] = languageList.get(i).country;
        }
        return strings;
    }
}

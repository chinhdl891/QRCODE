package com.example.qrscaner.models;

import java.util.ArrayList;
import java.util.List;

public class LanguageOption {
    private int id;
    private String country;
    private String lang;


    public LanguageOption(int id, String country, String lang) {
        this.id = id;
        this.country = country;
        this.lang = lang;
    }

    public LanguageOption() {
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

    public static List<LanguageOption> languageList() {
        List<LanguageOption> languageOptionList = new ArrayList<>();
        languageOptionList.add(new LanguageOption(0, "Việt Nam", "vn"));
        languageOptionList.add(new LanguageOption(1, "English", "en"));
//        languageList.add(new Language(2, "日本", "ja"));

        return languageOptionList;
    }

    public static String[] listStrCountry() {
        List<LanguageOption> languageOptionList = languageList();
        String[] strings = new String[languageOptionList.size()];

        for (int i = 0; i < languageOptionList.size(); i++) {
            strings[i] = languageOptionList.get(i).country;
        }
        return strings;
    }
}

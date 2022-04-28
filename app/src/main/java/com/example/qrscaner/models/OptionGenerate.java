package com.example.qrscaner.models;

import java.util.List;

public class OptionGenerate {
    private int id;
    private List<GenerateItem> generateItems;
    private String name;


    public OptionGenerate(int id, List<GenerateItem> generateItems, String name) {
        this.id = id;
        this.generateItems = generateItems;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GenerateItem> getGenerateItems() {
        return generateItems;
    }

    public void setGenerateItems(List<GenerateItem> generateItems) {
        this.generateItems = generateItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

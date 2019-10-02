package com.example.tresenraya;

public class Player {
    private String mName;
    private String mColor;
    private int mValue;

    public Player(String name, String  color, int valor) {
        mName = name;
        mColor = color;
        mValue = valor;
    }

    String getName() {return mName;}
    void setName(String name) {
        mName = name;
    }
    String getColor() {return mColor;}
    void setColor(String color) {mColor = color;}
    int getValue() {
        return mValue;
    }
}

package com.example.tresenraya;

public class Player {
    private String mName;
    private String mColor;

    public Player(String name, String  color) {
        mName = name;
        mColor = color;
    }

    String getName() {return mName;}
    void setName(String name) {
        mName = name;
    }
    String getColor() {return mColor;}
    void setColor(String color) {mColor = color;}
}

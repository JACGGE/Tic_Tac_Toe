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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMcolor() {
        return mColor;
    }

    public void setMcolor(String color) {
        mColor = color;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
    }
}

package com.example.tresenraya;

import android.graphics.Color;

public class Jugador {
    private String mName;
    private String mColor;
    private int mValor;

    public Jugador(String name, String  color, int valor) {
        mName = name;
        mColor = color;
        mValor = valor;
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

    public int getMvalor() {
        return mValor;
    }

    public void setMvalor(int mvalor) {
        this.mValor = mvalor;
    }
}

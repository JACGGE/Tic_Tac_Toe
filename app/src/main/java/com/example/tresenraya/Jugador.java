package com.example.tresenraya;

public class Jugador {
    String mName;
    int mColor;
    int mValor;

    public Jugador(String name, int color, int valor) {
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

    public int getMcolor() {
        return mColor;
    }

    public void setMcolor(int color) {
        color = mColor;
    }

    public int getMvalor() {
        return mValor;
    }

    public void setMvalor(int mvalor) {
        this.mValor = mvalor;
    }
}
